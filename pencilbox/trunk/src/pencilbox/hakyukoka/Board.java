package pencilbox.hakyukoka;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;


/**
 * 「波及効果」盤面クラス
 * ヒント機能つき
 */
public class Board extends BoardBase {
	
	public static final int BLANK = 0;
	public static final int UNKNOWN = 0;
	public static final int UNDETERMINED = -2;
	
	private List<Area> areaList;
//	private int maxNumber = 9; // 最大数字9とする
	private int[][] state; // 解答の数字
	private int[][] number; // 問題の数字
	private Area[][] area;

	private int[][] multi;  // 距離不足での重複数を記録する
	private int[][] multi2;  // 同領域内での重複数を記録する
	private DigitPatternHint hint;       // 使用可能数字パターン

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
		multi = new int[rows()][cols()];
		multi2 = new int[rows()][cols()];
		hint = new DigitPatternHint();
		hint.setupHint(this);
	}

	public void clearBoard() {
		for (Address p : cellAddrs()) {
			setState(p, 0);
		}
		initBoard();
	}
	/**
	 * そのマスは問題として数字を与えられたマスかどうか
	 * @param r マスの行座標
	 * @param c マスの列座標
	 * @return 問題数字のマスなら true, 解答すべきマスなら false
	 */
	public boolean isStable(int r, int c) {
		return number[r][c] != Board.BLANK;
	}

	public boolean isStable(Address pos) {
		return isStable(pos.r(), pos.c());
	}
	/**
	 * Get state of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the state.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * Set state to a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param st The state to set.
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * Get number of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the number.
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}

	public int getNumberOrState(Address p) {
		return isStable(p) ? getNumber(p) : getState(p);
	}
	/**
	 * そのマスの所属する領域を取得する
	 * そのマスが領域に属していない場合は null を返す
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c ) {
		return area[r][c];
	}
	
	public Area getArea(Address pos) {
		return getArea(pos.r(), pos.c());
	}
	/**
	 * 盤上のマスに，そのマスの所属する領域を設定する
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}

	public void setArea(Address pos, Area a) {
		setArea(pos.r(), pos.c(), a);
	}
	/**
	 * 配置数字が誤りの場合 true を返す
	 * その数字の距離以内に同じ数字があるか，
	 * 領域内に同じ数字があるか，
	 * 領域のマス数よりも大きい数字であれば，trueを返す
	 * @param p マスの座標
	 * @return そのマスの数字が誤りであれば true を返す
	 */
	public boolean isError(Address p) {
		return isTooClose(p) || isMultipleNumber(p) || isTooLarge(p);
	}
	/**
	 * そのマスから，マスの数字距離以内に同じ数字があるかどうかを調べる
	 * @param p マスの座標
	 * @return マスの数字距離以内に同じ数字がある場合 true を返す
	 */
	public boolean isTooClose(Address p) {
		return multi[p.r()][p.c()] > 1;
	}
	/**
	 * そのマスと同じ領域内に同じ数字があるかどうかを調べる
	 * @param p マスの座標
	 * @return 領域内に同じ数字がある場合 true を返す
	 */
	public boolean isMultipleNumber(Address p) {
		return multi2[p.r()][p.c()] > 1;
	}
	/**
	 * そのマスの数字が，領域面積を超えていないかどうかを調べる
	 * @param p マスの座標
	 * @return 領域面積を超えた数字の場合 true を返す
	 */
	public boolean isTooLarge(Address p) {
		return getArea(p)!=null && getNumberOrState(p) > getArea(p).size() ;
	}
	/**
	 * 引数の座標の可能数字のビットパターンを取得
	 * @param r row coordinate
	 * @param c column coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(Address p){
		return hint.getPattern(p);
	}
	/**
	 * その場所にある数字をルールに違反せずに配置可能かどうかを調べる
	 * @param p マスの座標
	 * @param n 配置できるかどうかを調べる数字
	 * @return 配置可能なら true　を返す
	 */
	boolean canPlace(Address p, int n) {
		return hint.canPlace(p, n);
	}

	/**
	 * マスに解答数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeAnswerNumber(Address p, int n) {
		int prev = getState(p);
		if (n == prev) 
			return;
		if (isStable(p)) {
			changeFixedNumber(p, Board.BLANK);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellNumberEditStep(p, prev, n));
		updateMulti(p, n);
		if (getArea(p) != null)
			updateMulti2(p, n);
		setState(p, n);
		if (prev == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
//		changeNumber1(p, prev, n);
	}
	/**
	 * マスに問題数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeFixedNumber(Address p, int n) {
		int prev = getNumber(p);
		if (n == prev)
			return;
		if (getState(p) != Board.UNKNOWN) {
			changeAnswerNumber(p, Board.UNKNOWN);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, n));
		updateMulti(p, n);
		if (getArea(p) != null)
			updateMulti2(p, n);
		setNumber(p, n);
		if (prev == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
//		changeNumber1(p, prev, n);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellNumberEditStep) {
			CellNumberEditStep s = (CellNumberEditStep) step;
			changeAnswerNumber(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeFixedNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellNumberEditStep) {
			CellNumberEditStep s = (CellNumberEditStep) step;
			changeAnswerNumber(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeFixedNumber(s.getPos(), s.getAfter());
		}
	}

	/**
	 * 新しい領域を追加する
	 * @param newArea
	 */
	public void addArea(Area newArea) {
		for (Address p : newArea) {
			setArea(p, newArea);
		}
		areaList.add(newArea);
	}

	/**
	 * 領域を削除する
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Address p : oldArea) {
			if (getArea(p) == oldArea)
				setArea(p, null);
		}
		areaList.remove(oldArea);
	}
	/**
	 * マスを領域に追加する
	 * @param r 追加するマスの行座標
	 * @param c 追加するマスの列座標
	 * @param area 追加される領域
	 */
	public void addCellToArea(int r, int c, Area area) {
		if (area.isEmpty()) {
			areaList.add(area);
		}
		setArea(r, c, area);
		area.add(r, c);
//		initArea(area);
	}

	public void addCellToArea(Address pos, Area area) {
		addCellToArea(pos.r(), pos.c(), area);
	}
	/**
	 * マスを領域から取り除く
	 * @param r 取り除くマスの行座標
	 * @param c 取り除くマスの列座標
	 * @param area 取り除かれる領域
	 */
	public void removeCellFromArea(int r, int c, Area area) {
		setArea(r, c, null);
		area.remove(r, c);
		if (area.isEmpty()) {
			areaList.remove(area);
		} else {
//			initArea(area);
		}
	}

	public void removeCellFromArea(Address pos, Area area) {
		removeCellFromArea(pos.r(), pos.c(), area);
	}
	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}

	/**
	 * @return Returns the number.
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}

	public void initBoard() {
		initMulti();
		initMulti2();
		hint.initHint();
	}
	/**
	 * multi[][] 初期化
	 */
	void initMulti() {
		for (Address p : cellAddrs()) {
			if(getNumber(p)>0)
				initMulti1(p,getNumber(p));
		}
	}

	private void initMulti1(Address p0, int num) {
		int r0=p0.r(), c0=p0.c();
		multi[r0][c0] = 1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			for (int k=1; k<=num; k++) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (getNumber(p) == num) {
					multi[r0][c0]++;
				}
			}
		}
	}

	/**
	 * マスの数字が変更されたときに，それに応じて距離内の重複数を表すmulti配列を更新する
	 * @param p0 数字の変更されたマスの座標
	 * @param num 変更後の数字
	 */
	void updateMulti(Address p0, int num) {
		int r0 = p0.r();
		int c0 = p0.c();
		int prev = getNumberOrState(p0);
		if (multi[r0][c0]>1) {
			for (int d=0; d<4; d++) {
				Address p = p0;
				for (int k=1; k<=prev; k++) {
					p = Address.nextCell(p, d);
					if (!isOn(p))
						break;
					if (getNumber(p) == prev) {
						multi[p.r()][p.c()]--;
					}
				}
			}
		}
		if (num==0)
			multi[r0][c0]=0;
		else if (num>0) {
			multi[r0][c0] = 1;
			for (int d=0; d<4; d++) {
				Address p = p0;
				for (int k=1; k<=num; k++) {
					p = Address.nextCell(p, d);
					if (!isOn(p))
						break;
					if (getNumber(p) == num) {
						multi[p.r()][p.c()]++;
						multi[p0.r()][p0.c()]++;
					}
				}
			}
		}
//			printMulti();
	}
	void initMulti2() {
		for (Address p : cellAddrs()) {
			if(getNumber(p)>0 && getArea(p)!=null)
				initMulti21(p,getNumberOrState(p));
		}
	}

	private void initMulti21(Address p0, int num) {
		multi2[p0.r()][p0.r()] = 1;
		for (Address p : getArea(p0)) {
			if (p.equals(p0))
				continue;
			if (getNumber(p) == num) {
				multi2[p0.r()][p0.r()]++;
			}
		}
	}
	/**
	 * マスの数字が変更されたときに，それに応じて領域内の重複数を表すmulti2配列を更新する
	 * @param p0 数字の変更されたマスの行座標
	 * @param num 変更後の数字
	 */
	void updateMulti2(Address p0, int num) {
		int r0=p0.r();
		int c0=p0.c();
		int prevNum = getNumber(p0);
		if (multi2[r0][c0]>1) {
			for (Address p : getArea(p0)) {
				if (p.equals(p0))
					continue;
				if (getNumber(p) == prevNum) {
					multi2[p.r()][p.c()]--;
				}
			}
		}
		if (num==0)
			multi2[r0][c0]=0;
		else if (num>0) {
			multi2[r0][c0] = 1;
			for (Address p : getArea(p0)) {
				if (p.equals(p0))
					continue;
				if (getNumber(p) == num) {
					multi2[p.r()][p.c()]++;
					multi2[r0][c0]++;
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumberOrState(p) > 0 && isMultipleNumber(p))
				result |= 2;
			if (isTooLarge(p))
				result |= 4;
			if (isTooClose(p))
				result |= 8;
			if (getNumberOrState(p) <= 0)
				result |= 1;
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		if (result==1)
			return Messages.getString("hakyukoka.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result&2) == 2)
			message.append(Messages.getString("hakyukoka.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result&4) == 4)
			message.append(Messages.getString("hakyukoka.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result&8) == 8)
			message.append(Messages.getString("hakyukoka.AnswerCheckMessage4")); //$NON-NLS-1$
		return message.toString();
	}
}
