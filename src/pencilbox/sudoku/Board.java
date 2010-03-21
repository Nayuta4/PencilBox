package pencilbox.sudoku;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;


/**
 * 「数独」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;

	private int maxNumber; // 問題の最大数字
	private int[][] state; // 問題の数字:1, 解答すべき数字:0,
	private int[][] number;
	private int unit = 0;

	private int[][] multi; // 重複数

	private DigitPatternHint hint;

	protected void setup(){
		super.setup();
		maxNumber = rows();
		for (int s=1; s<10; s++) {
			if (s*s == maxNumber) {
				unit = s;
				break;
			}
		}
		if (unit == 0)
			throw new RuntimeException("不正な大きさ"); //$NON-NLS-1$
		state = new int[maxNumber][maxNumber];
		number = new int[maxNumber][maxNumber];
		multi = new int[maxNumber][maxNumber];
		hint = new DigitPatternHint();
		hint.setupHint(this);
	}
	
	public void clearBoard() {
		super.clearBoard();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (!isStable(r,c)) {
					number[r][c] = 0;
				}
			}
		}
		initMulti();
		hint.initHint();
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
	/**
	 * @return ボックスの1辺のマス数。通常の数独では3
	 */
	public int getUnit() {
		return unit;
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
	 * @param c Colmun coordinate of the cell.
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
	 * @param c Colmun coordinate of the cell.
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
	 * @param c Colmun coordinate of the cell.
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
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * マスに数字が入っていないかどうか
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns true if the cell is empty.
	 */
	public boolean isUnknown(int r, int c) {
		return number[r][c] == 0;
	}

	public void initBoard() {
		initMulti();
		hint.initHint();
	}
	/**
	 * 引数の座標の可能数字のビットパターンを取得
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(int r, int c){
		return hint.getPattern(r,c);
	}
	/**
	 * [r,c] に n を配置可能かどうか
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @param n number to check
	 * @return true if n can placed at cell [r,c]
	 */
	boolean canPlace(int r, int c, int n) {
		return getNumber(r,c) == 0 && hint.canPlace(r, c, n);
	}
	/**
	 * マスに数字を入力し，アドゥリスナーに通知する
	 * @param pos マス座標
	 * @param n 入力する数字
	 */
	public void enterNumberA(Address pos, int n) {
		if (n < 0 || n > maxNumber) return;
		if (n == getNumber(pos)) return;
		fireUndoableEditUpdate(
			new CellNumberEditStep(pos, getNumber(pos), n));
		changeNumber(pos, n);
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
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void changeNumber(int r, int c, int n) {
		if (getNumber(r,c) == n)
			return;
		updateMulti(r, c, n);
		hint.updateHint(r, c, n);
		setNumber(r, c, n);
	}
	
	public void changeNumber(Address pos, int n) {
		changeNumber(pos.r(), pos.c(), n);
	}
	/**
	 * マスと同じ行，列，ボックスに，そのマスの数字と重複する数字があるかどうかを調べる
	 * @param r 行座標
	 * @param c 列座標
	 * @return　重複数字があれば true
	 */
	public boolean isMultipleNumber(int r, int c) {
		return multi[r][c]>1;
	}
	
	/**
	 * 現在の盤面状態から，multi[][] 配列を初期化する
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
		for (int c = 0; c < cols(); c++) {
			if (c==c0) continue;
			if (getNumber(r0,c) == num) {
				multi[r0][c0]++;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (r==r0) continue;
			if (getNumber(r,c0) == num) {
				multi[r0][c0]++;
			}
		}
		int unit = getUnit();
		for (int r = r0-r0%unit; r < r0-r0%unit+unit; r++) {
			for (int c = c0-c0%unit; c < c0-c0%unit+unit; c++) {
				if (r==r0 && c==c0)
					continue;
				if (getNumber(r,c) == num) {
					multi[r0][c0]++;
				}
			}
		}
	}
	/**
	 *	[r0, c0]に変化があったときにmulti配列を更新する
	 * @param r0 状態を変更したマスの行座標
	 * @param c0 状態を変更したマスの列座標
	 * @param num 変更後のマスの数字
	 */
	void updateMulti(int r0, int c0, int num) {
		int prevNum = getNumber(r0, c0);
		if (multi[r0][c0]>1) {
			decreaseMulti(r0, c0, prevNum);
		}
		if (num>0) {
			increaseMulti(r0, c0, num);
		}
	}
	/**
	 * [r0, c0] に数字num(>0)を入れるときに，multi[][]を更新する
	 * @param r0 状態を変更したマスの行座標
	 * @param c0 状態を変更したマスの列座標
	 * @param num 変更後のマスの数字
	 */
	private void increaseMulti(int r0, int c0, int num) {
		multi[r0][c0] = 1;
		for (int c = 0; c < cols(); c++) {
			if (c==c0)
				continue;
			if (getNumber(r0,c) == num) {
				multi[r0][c]++;
				multi[r0][c0]++;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (r==r0) continue;
			if (getNumber(r,c0) == num) {
				multi[r][c0]++;
				multi[r0][c0]++;
			}
		}
		int unit = getUnit();
		for (int r = r0-r0%unit; r < r0-r0%unit+unit; r++) {
			for (int c = c0-c0%unit; c < c0-c0%unit+unit; c++) {
				if (r==r0 && c==c0)
					continue;
				if (getNumber(r,c) == num) {
					multi[r][c]++;
					multi[r0][c0]++;
				}
			}
		}
	}
	/**
	 * [r0, c0] の数字prevNum(>0)を消す，multi[][]を更新する
	 * @param r0 状態を変更したマスの行座標
	 * @param c0 状態を変更したマスの列座標
	 * @param prevNum 変更前のマスに入っていた数字
	 */
	private void decreaseMulti(int r0, int c0, int prevNum) {
		for (int c = 0; c < cols(); c++) {
			if (c==c0)
				continue;
			if (getNumber(r0,c) == prevNum) {
				multi[r0][c]--;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (r==r0)
				continue;
			if (getNumber(r,c0) == prevNum) {
				multi[r][c0]--;
			}
		}
		int unit = getUnit();
		for (int r = r0-r0%unit; r < r0-r0%unit+unit; r++) {
			for (int c = c0-c0%unit; c < c0-c0%unit+unit; c++) {
				if (r==r0 && c==c0)
					continue;
				if (getNumber(r,c) == prevNum) {
					multi[r][c]--;
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isMultipleNumber(r,c))
					result |= 1;;
				if (getNumber(r, c) == 0)
					result |= 2;
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		if (result == 2)
			return Messages.getString("sudoku.AnswerCheckMessage2"); //$NON-NLS-1$
		if ((result & 1) == 1)
			return Messages.getString("sudoku.AnswerCheckMessage1"); //$NON-NLS-1$
		else
			return ""; //$NON-NLS-1$
	}
}
