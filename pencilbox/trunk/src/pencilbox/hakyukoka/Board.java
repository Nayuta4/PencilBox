package pencilbox.hakyukoka;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.AbstractStep.EditType;
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
	public Area getArea(int r, int c) {
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
			fireUndoableEditUpdate(new CellEditStep(EditType.NUMBER, p, prev, n));
		setState(p, n);
		changeNumber1(p, prev, n);
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
		if (getState(p) > 0) {
			changeAnswerNumber(p, Board.UNKNOWN);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
		changeNumber1(p, prev, n);
	}

	private void changeNumber1(Address p, int prev, int n) {
		updateMulti(p, prev, n);
		if (getArea(p) != null)
			updateMulti2(p, prev, n);
		if (prev == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getBefore());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getBefore());
			}
		} else if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				removeCell(s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				addCell(s.getP0(), s.getPos());
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getAfter());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getAfter());
			}
		} else if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				addCell(s.getP0(), s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				removeCell(s.getPos());
			}
		}
	}
	/**
	 * マスp を p0 と同じ領域にする。ただし p0が NOWHWERならば新しい領域を作る
	 * @param p0
	 * @param p
	 */
	void addCell(Address p0, Address p) {
		if (Address.NOWHERE.equals(p0)) { 
			Area a = new Area();
			addCellToArea(p, a);
		} else {
			Area a = getArea(p0);
			if (a != null) {
				addCellToArea(p, a);
			}
		}
	}
	/**
	 * マス p を領域から取り除く。
	 * @param p
	 */
	void removeCell(Address p) {
		Area a = getArea(p);
		if (a != null) {
			removeCellFromArea(p, a);
		}
	}
	/**
	 * マスを領域に追加する
	 * @param p 追加するマスの座標
	 * @param a 追加される領域
	 */
	public void addCellToArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 0) {
				p0 = a.getTopCell(Address.NOWHERE);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.ADDED));
		}
		if (a.isEmpty()) {
			areaList.add(a);
		}
		setArea(p, a);
		a.add(p);
//		initArea(a);
	}
	/**
	 * マスを領域から取り除く
	 * @param p 取り除くマスの座標
	 * @param a 取り除かれる領域
	 */
	public void removeCellFromArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 1) {
				p0 = a.getTopCell(p);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.REMOVED));
		}
		setArea(p, null);
		a.remove(p);
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
//			initArea(a);
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
	 * 領域すべてのマスを追加する
	 * @param newArea
	 */
	public void addWholeArea(Area newArea) {
		Address[] cells = newArea.toArray(new Address[0]);
		for (Address p : cells) {
			addCellToArea(p, newArea);
		}
	}
	/**
	 * 領域のすべてのマスを領域から除いて領域を削除する
	 * @param oldArea
	 */
	public void removeWholeArea(Area oldArea) {
		Address[] cells = oldArea.toArray(new Address[0]);
		for (Address p : cells) {
			removeCellFromArea(p, oldArea);
		}
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
	 * 現在の盤面について距離内の重複数を表すmulti配列を求める
	 */
	void initMulti() {
		for (Address p : cellAddrs()) {
			int n = getNumberOrState(p);
			if (n > 0) {
				multi[p.r()][p.c()] = 1;
				updateMulti1(p, n, +1, 0);
			} else {
				multi[p.r()][p.c()] = 0;
			}
		}
	}

	/**
	 * マスの数字が変更されたときに，それに応じて距離内の重複数を表すmulti配列を更新する
	 * @param p0 数字の変更されたマスの座標
	 * @param prev 変更前の数字
	 * @param num 変更後の数字
	 */
	void updateMulti(Address p0, int prev, int num) {
		int r0 = p0.r(), c0 = p0.c();
		if (multi[r0][c0]>1) {
			updateMulti1(p0, prev, 0, -1);
		}
		if (num > 0) {
			multi[r0][c0] = 1;
			updateMulti1(p0, num, +1, +1);
		} else if (num <= 0) {
			multi[r0][c0]=0;
		}
	}
	/**
	 * p0の数字の変更に応じて重複数を数えるmulti[][]配列を更新する
	 * 範囲の数字を見て，num と同じ数字のマスがあったらp0の超複数をm, そのマスの超複数をk変更する。
	 * @param p0 状態を変更したマスの座標
	 * @param num 調べる数字
	 * @param m 自分の重複数更新数
	 * @param k 相手の重複数更新数
	 */
	private void updateMulti1(Address p0, int num, int m, int k) {
		for (int d=0; d<4; d++) {
			Address p = p0;
			for (int l=1; l<=num; l++) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (getNumberOrState(p) == num) {
					multi[p.r()][p.c()] += k;
					multi[p0.r()][p0.c()] += m;
				}
			}
		}
	}
	/**
	 * 現在の盤面の領域内の重複数を表すmulti2配列を求める。
	 */
	void initMulti2() {
		for (Address p : cellAddrs()) {
			int n = getNumberOrState(p);
			if (n>0 && getArea(p)!=null) {
				multi2[p.r()][p.r()] = 1;
				updateMulti21(p, n, +1, 0);
			} else {
				multi2[p.r()][p.c()] = 0;
			}
		}
	}
	/**
	 * マスの数字が変更されたときに，それに応じて領域内の重複数を表すmulti2配列を更新する
	 * @param p0 数字の変更されたマスの行座標
	 * @param prev 変更前の数字
	 * @param num 変更後の数字
	 */
	void updateMulti2(Address p0, int prev, int num) {
		int r0=p0.r(), c0=p0.c();
		if (multi2[r0][c0]>1) {
			updateMulti21(p0, prev, 0, -1);
		}
		if (num > 0) {
			multi2[r0][c0] = 1;
			updateMulti21(p0, num, +1, +1);
		} else if (num <= 0) {
			multi2[r0][c0]=0;
		}
	}
	/**
	 * p0の数字の変更に応じて重複数を数えるmulti2[][]配列を更新する
	 * 範囲の数字を見て，num と同じ数字のマスがあったらp0の超複数をm, そのマスの超複数をk変更する。
	 * @param p0 状態を変更したマスの座標
	 * @param num 調べる数字
	 * @param m 自分の重複数更新数
	 * @param k 相手の重複数更新数
	 */
	private void updateMulti21(Address p0, int num, int m, int k) {
		for (Address p : getArea(p0)) {
			if (p.equals(p0))
				continue;
			if (getNumberOrState(p) == num) { // 変更後の数字と同じ数字があったら，
				multi2[p.r()][p.c()] += k; // 変更後の数字と同じ数字があったら，
				multi2[p0.r()][p0.c()] += m; // 変更されたマスの重複数もm増やす
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
