package pencilbox.sudoku;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;


/**
 * 「数独」盤面クラス
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0; // 解答数字がない状態
	static final int BLANK = 0; // 問題数字がない状態
	static final int UNDETERMINED = -2; // 問題数字が未定数字の状態

	private int maxNumber; // 問題の最大数字
	private int[][] state; // 解答の数字
	private int[][] number; // 問題の数字
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
		for (Address p : cellAddrs()) {
			if (!isStable(p)) {
				setState(p, 0);
			}
		}
		initMulti();
		hint.initHint();
	}

	public int getMaxNumber() {
		return maxNumber;
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

	public int getNumber(Address p) {
		return number[p.r()][p.c()];
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

	public int getNumberOrState(int r, int c) {
		return isStable(r,c) ? getNumber(r,c) : getState(r,c);
	}

	public void initBoard() {
		initMulti();
		hint.initHint();
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
	 * [r,c] に n を配置可能かどうか
	 * @param r row coordinate
	 * @param c column coordinate
	 * @param n number to check
	 * @return true if n can placed at cell [r,c]
	 */
	boolean canPlace(Address p, int n) {
		return getNumber(p) == 0 && hint.canPlace(p, n);
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
		changeNumber1(p, prev, n);
		setState(p, n);
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
		changeNumber1(p, prev, n);
		setNumber(p, n);
	}

	private void changeNumber1(Address p, int prev, int n) {
		updateMulti(p, prev, n);
		hint.updateHint(p, n);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getBefore());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getBefore());
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
		}
	}

	/**
	 * マスと同じ行，列，ボックスに，そのマスの数字と重複する数字があるかどうかを調べる
	 * @param p 座標
	 * @return　重複数字があれば true
	 */
	public boolean isMultipleNumber(Address p) {
		return multi[p.r()][p.c()]>1;
	}

	/**
	 * 現在の盤面状態から，multi[][] 配列を初期化する
	 */
	void initMulti() {
		for (Address p : cellAddrs()) {
			int n = getNumberOrState(p);
			if(n>0) {
				multi[p.r()][p.c()] = 1;
				updateMulti1(p, n, +1, 0);
			}
		}
	}

	/**
	 *	p0に変化があったときにmulti配列を更新する
	 * @param p0 状態を変更したマスの座標
	 * @param prev 変更前の数字
	 * @param n 変更後の数字
	 */
	void updateMulti(Address p0, int prev, int n) {
		int r0=p0.r(), c0=p0.c();
		// prevと同じ数字を探して重複数を-1する
		if (multi[r0][c0]>1) {
			updateMulti1(p0, prev, 0, -1);
		}
		// nと同じ数字を探して重複数を+1する，マス自身の重複数も+1する
		if (n>0) {
			multi[r0][c0] = 1;
			updateMulti1(p0, n, +1, +1);
		} else {
			multi[r0][c0] = 0;
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
		int r0=p0.r(), c0=p0.c();
		for (int c = 0; c < cols(); c++) {
			if (c==c0)
				continue;
			if (getNumberOrState(r0,c) == num) {
				multi[r0][c] += k;
				multi[r0][c0] += m;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (r==r0)
				continue;
			if (getNumberOrState(r,c0) == num) {
				multi[r][c0] += k;
				multi[r0][c0] += m;
			}
		}
		int unit = getUnit();
		for (int r = r0-r0%unit; r < r0-r0%unit+unit; r++) {
			for (int c = c0-c0%unit; c < c0-c0%unit+unit; c++) {
				if (r==r0 && c==c0)
					continue;
				if (getNumberOrState(r,c) == num) {
					multi[r][c] += k;
					multi[r0][c0] += m;
				}
			}
		}
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (isMultipleNumber(p))
				result |= 1;
			if (getNumberOrState(p) <= 0)
				result |= 2;
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

	public void exchangeNumbers(int v1, int v2) {
		exchangeNumbers(new int[] {v1, v2}, new int[] {v2, v1});
	}

	public void exchangeNumbers(int[] a, int[] b) {
		Board board = this;
		board.startCompoundUndo();
		for (Address p : board.cellAddrs()) {
			if (board.isStable(p)) {
				int n = board.getNumber(p);
				for (int k = 0; k < a.length; k++) {
					if (n == a[k]) {
						board.changeFixedNumber(p, b[k]);
						break;
					}
				}
			} else {
				int n = board.getState(p);
				for (int k = 0; k < a.length; k++) {
					if (n == a[k]) {
						board.changeAnswerNumber(p, b[k]);
						break;
					}
				}
			}
		}
		board.stopCompoundUndo();
	}

}
