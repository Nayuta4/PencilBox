package pencilbox.hakyukoka;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;


/**
 * 「波及効果」盤面クラス
 * ヒント機能つき
 */
public class Board extends BoardBase {
	
	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;
	static final int UNDETERMINED = -2;
	
	private List<Area> areaList;
//	private int maxNumber = 9; // 最大数字9とする
	private int[][] state; // 問題の数字:1, 解答すべき数字:0,
	private int[][] number;
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
			if (!isStable(p))
				setNumber(p, 0);
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
		return state[r][c] == STABLE;
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
	public int getNumber(int r, int c ) {
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
	public void setArea(int r, int c,  Area a) {
		area[r][c] = a;
	}

	public void setArea(Address pos,  Area a) {
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
		return getArea(p)!=null && getNumber(p) > getArea(p).size() ;
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
	 * マスに数字を入力し，アドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力する数字
	 */
	public void changeNumber(Address p, int n) {
		if (n < 0) 
			return;
		if (n == getNumber(p)) 
			return;
		fireUndoableEditUpdate(
			new CellNumberEditStep(p, getNumber(p), n));
		changeNumber1(p, n);
	}

	private void changeNumber1(Address p, int n) {
		int prevNum = getNumber(p);
		updateMulti(p, n);
		if (getArea(p) != null)
			updateMulti2(p, n);
		setNumber(p, n);
		if (prevNum == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
	}
	
	public void changeFixedNumber(Address p, int n) {
		if (n == Board.UNKNOWN)
			setState(p, Board.UNSTABLE);
		else
			setState(p, Board.STABLE);
		if (n == Board.UNDETERMINED)
			n = 0;
		changeNumber1(p, n);
	}

	public void undo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		if (isStable(s.getPos()))
			return;
		changeNumber(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		if (isStable(s.getPos()))
			return;
		changeNumber(s.getPos(), s.getAfter());
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if(getNumber(r,c)>0)
					initMulti1(r,c,getNumber(r,c));
			}
		}
	}

	private void initMulti1(int r0, int c0, int num) {
		multi[r0][c0] = 1;
		for (int c = c0-num; c <= c0+num; c++) {
			if (c==c0 || !isOn(r0,c)) continue;
			if (getNumber(r0,c) == num) {
				multi[r0][c0]++;
			}
		}
		for (int r = r0-num; r <= r0+num; r++) {
			if (r==r0 || !isOn(r,c0)) continue;
			if (getNumber(r,c0) == num) {
				multi[r0][c0]++;
			}
		}
	}

	/**
	 * マスの数字が変更されたときに，それに応じて距離内の重複数を表すmulti配列を更新する
	 * @param p0 数字の変更されたマスの行標
	 * @param num 変更後の数字
	 */
	void updateMulti(Address p0, int num) {
		int r0 = p0.r();
		int c0 = p0.c();
		int prevNum = getNumber(r0, c0);
		if (multi[r0][c0]>1) {
			for (int c = c0-prevNum; c <= c0+prevNum; c++) {
				if (c==c0 || !isOn(r0,c))
					continue;
				if (getNumber(r0,c) == prevNum) {
					multi[r0][c]--;
				}
			}
			for (int r = r0-prevNum; r <= r0+prevNum; r++) {
				if (r==r0 || !isOn(r,c0))
					continue;
				if (getNumber(r,c0) == prevNum) {
					multi[r][c0]--;
				}
			}
		}
		if (num==0)
			multi[r0][c0]=0;
		else if (num>0) {
			multi[r0][c0] = 1;
			for (int c = c0-num; c <= c0+num; c++) {
				if (c==c0 || !isOn(r0,c))
					continue;
				if (getNumber(r0,c) == num) {
					multi[r0][c]++;
					multi[r0][c0]++;
				}
			}
			for (int r = r0-num; r <= r0+num; r++) {
				if (r==r0 || !isOn(r,c0))
					continue;
				if (getNumber(r,c0) == num) {
					multi[r][c0]++;
					multi[r0][c0]++;
				}
			}
		}
//			printMulti();
	}
	void initMulti2() {
		for (int r = rows() - 1; r >= 0; r--) {
			for (int c = cols() - 1; c >= 0; c--) {
				if(getNumber(r,c)>0 && getArea(r,c)!=null)
					initMulti21(r,c,getNumber(r,c));
			}
		}
	}
	private void initMulti21(int r0, int c0, int num) {
		multi2[r0][c0] = 1;
		for (Address pos : getArea(r0,c0)) {
			if (pos.equals(r0,c0))
				continue;
			if (getNumber(pos.r(),pos.c()) == num) {
				multi2[r0][c0]++;
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
		int prevNum = getNumber(r0, c0);
		if (multi2[r0][c0]>1) {
			for (Address pos : getArea(r0,c0)) {
				if (pos.equals(r0,c0))
					continue;
				if (getNumber(pos.r(),pos.c()) == prevNum) {
					multi2[pos.r()][pos.c()]--;
				}
			}
		}
		if (num==0)
			multi2[r0][c0]=0;
		else if (num>0) {
			multi2[r0][c0] = 1;
			for (Address pos : getArea(r0,c0)) {
				if (pos.equals(r0,c0))
					continue;
				if (getNumber(pos.r(),pos.c()) == num) {
					multi2[pos.r()][pos.c()]++;
					multi2[r0][c0]++;
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumber(p) > 0 && isMultipleNumber(p))
				result |= 2;
			if (isTooLarge(p))
				result |= 4;
			if (isTooClose(p))
				result |= 8;
			if (getNumber(p) == 0)
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
