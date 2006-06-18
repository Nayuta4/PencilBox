package pencilbox.kakuro;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.util.ArrayUtil;

/**
 * 「カックロ」 ヒント付き盤面クラス
 */
public class Board extends BoardBase {

	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;
	static final int WALL = -1;
	static final int maxNumber = 9;

	private int number[][];
	private int sumH[][];
	private int sumV[][];

	private Word[][] wordH;
	private Word[][] wordV;
	private int[][] multi; // 重複数
	private DigitPatternHint hint;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		sumH = new int[rows()][cols()];
		sumV = new int[rows()][cols()];
		for (int c = 0; c < cols(); c++) {
			number[0][c] = WALL;
			sumH[0][c] = 0;
			sumV[0][c] = 0;
		}
		for (int r = 1; r < rows(); r++) {
			number[r][0] = WALL;
			sumH[r][0] = 0;
			sumV[r][0] = 0;
		}
		wordH = new Word[rows()][cols()];
		wordV = new Word[rows()][cols()];
		multi = new int[rows()][cols()];
		initWord();
		initMulti();
		hint = new DigitPatternHint();
		initHint();
	}

	public void clearBoard() {
		super.clearBoard();
		for (int r = 1; r < rows(); r++) {
			for (int c = 1; c < cols(); c++) {
				if (!isWall(r, c)) {
					number[r][c] = 0;
				}
			}
		}
		ArrayUtil.initArrayInt2(multi, 0);
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (getSumH(r,c) > 0)
					wordH[r][c].clear();
				if (getSumV(r,c) > 0)
					wordV[r][c].clear();
			}
		}
		initHint();
	}

	/**
	 * @return Returns the number.
	 */
	int[][] getNumber() {
		return number;
	}

	/**
	 * @return Returns the maxNumber.
	 */
	int getMaxNumber() {
		return maxNumber;
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
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}
	public boolean isWall(int r, int c) {
		if (!isOn(r,c))
			return true;
		return number[r][c] == WALL;
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
	public int getSumV(int r, int c) {
		return sumV[r][c];
	}
	public int getSumH(int r, int c) {
		return sumH[r][c];
	}
	public void setSumV(int r, int c, int n) {
		number[r][c] = WALL;
		sumV[r][c] = n;
	}
	public void setSumH(int r, int c, int n) {
		number[r][c] = WALL;
		sumH[r][c] = n;
	}

	public void setWall(int r, int c, int a, int b) {
		number[r][c] = WALL;
		sumH[r][c] = a;
		sumV[r][c] = b;
	}
	public void removeWall(int r, int c) {
		number[r][c] = 0;
		sumH[r][c] = 0;
		sumV[r][c] = 0;
	}

	public void initBoard() {
		initWord();
		initMulti();
		initHint();
	}
	
	void initWord() {
		Word word;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (getNumber(r,c) == WALL) {
					//					if (sumHoriz[r][c] > 0) {
					int cc = c + 1;
					while (isOn(r, cc) && getNumber(r,cc) != WALL) {
						cc++;
					}
					word = new Word(r, c, cc - c - 1, getSumH(r,c));
					wordH[r][c] = word;
					cc = c + 1;
					while (isOn(r, cc) && getNumber(r,cc) != WALL) {
						wordH[r][cc] = word;
						if (getNumber(r,cc) > 0) {
							word.addNumber(getNumber(r,cc));
						}
						cc++;
					}
					//					}
					//					if (sumVert[r][c] > 0) {
					int rr = r + 1;
					while (isOn(rr, c) && getNumber(rr,c) != WALL) {
						rr++;
					}
					word = new Word(r, c, rr - r - 1, getSumV(r,c));
					wordV[r][c] = word;
					rr = r + 1;
					while (isOn(rr, c) && getNumber(rr,c) != WALL) {
						wordV[rr][c] = word;
						if (getNumber(rr,c) > 0) {
							word.addNumber(getNumber(rr,c));
						}
						rr++;
					}
				}
			}
		}
		//		}
	}
	/**
	 * 縦または横の計の中に，そのマスの数字と重複する数字があるかどうかを調べる
	 * @param r 行座標
	 * @param c 列座標
	 * @return　重複数字があれば true
	 */
	public boolean isMultipleNumber(int r, int c) {
		return multi[r][c] > 1;
	}

	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void changeNumber(int r, int c, int n) {
		wordH[r][c].changeNumber(getNumber(r,c), n);
		wordV[r][c].changeNumber(getNumber(r,c), n);
		updateMulti(r, c, n);
		setNumber(r, c, n);
		help(r, c);
	}
	/**
	 * マスに数字を入力し，アンドゥリスナーに通知する
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 入力した数字
	 */
	public void enterNumberA(int r, int c, int n) {
		if (n < 0 || n > maxNumber) 
			return;
		if (n == number[r][c]) 
			return;
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(r, c, number[r][c], n)));
		changeNumber(r, c, n);
	}

	/**
	 * マスの数字を1増加させる
	 * @param r 行座標
	 * @param c 列座標
	 */
	public void increaseNumber(int r, int c) {
		if (number[r][c] == maxNumber)
			enterNumberA(r, c, 0); 
		else
			enterNumberA(r, c, number[r][c]+1);
	}
	/**
	 * マスの数字を１減少させる
	 * @param r 行座標
	 * @param c 列座標
	 */
	public void decreaseNumber(int r, int c) {
		if (number[r][c] == 0) 
			enterNumberA(r, c, maxNumber); 
		else 
			enterNumberA(r, c, number[r][c]-1);
	}

/**
	 *  重複数の初期化
	 */
	void initMulti() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (getNumber(r,c) > 0)
					initMulti1(r, c, getNumber(r,c));
			}
		}
	}
	private void initMulti1(int r0, int c0, int num) {
		multi[r0][c0] = 1;
		int c = getWordHead(r0,c0,HORIZ) + 1;
		for (int i = 0; i < getWordSize(r0,c0,HORIZ); c++, i++) {
			if (c == c0)
				continue;
			if (getNumber(r0,c) == num) {
				multi[r0][c0]++;
			}
		}
		int r = getWordHead(r0,c0,VERT) + 1;
		for (int j = 0; j < getWordSize(r0,c0,VERT); r++, j++) {
			if (r == r0)
				continue;
			if (getNumber(r,c0) == num) {
				multi[r0][c0]++;
			}
		}
	}

	/**
	 * 数字の重複数の更新
	 */
	void updateMulti(int r0, int c0, int num) {
		int prevNum = getNumber(r0, c0);
		if (prevNum == num)
			return;
		if (multi[r0][c0] > 1) {
			int c = getWordHead(r0,c0,HORIZ) + 1;
			for (int i = 0; i < getWordSize(r0,c0,HORIZ); c++, i++) {
				if (c == c0)
					continue;
				if (getNumber(r0,c) == prevNum) {
					multi[r0][c]--;
				}
			}
			int r = getWordHead(r0,c0,VERT) + 1;
			for (int j = 0; j < getWordSize(r0,c0,VERT); r++, j++) {
				if (r == r0)
					continue;
				if (getNumber(r,c0) == prevNum) {
					multi[r][c0]--;
				}
			}
		}
		if (num == 0)
			multi[r0][c0] = 0;
		else if (num > 0) {
			multi[r0][c0] = 1;
			int c = getWordHead(r0,c0,HORIZ) + 1;
			for (int i = 0; i < getWordSize(r0,c0,HORIZ); c++, i++) {
				if (c == c0)
					continue;
				if (getNumber(r0,c) == num) {
					multi[r0][c]++;
					multi[r0][c0]++;
				}
			}
			int r = getWordHead(r0,c0,VERT) + 1;
			for (int j = 0; j < getWordSize(r0,c0,VERT); r++, j++) {
				if (r == r0)
					continue;
				if (getNumber(r,c0) == num) {
					multi[r][c0]++;
					multi[r0][c0]++;
				}
			}
		}
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isWall(r, c)) {
					if (isUnknown(r, c)) {
						result |= 1;
					} else if (isMultipleNumber(r, c)) {
						result |= 2;
					}
				} else if (isWall(r, c)) {
					if (getSumH(r,c) > 0 && wordH[r][c].getStatus() == -1) {
						result |= 4;
					}
					if (getSumV(r,c) > 0 && wordV[r][c].getStatus() == -1) {
						result |= 4;
					}
				}
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		if (result == 1)
			return "未完成\n";
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append("縦横に重複する数字がある\n");
		if ((result & 4) == 4)
			message.append("正しくない計がある\n");
		return message.toString();
	}

	void initHint() {
		hint.init(this);
	}
	protected void help(int r, int c) {
		hint.helpCheck(r, c);
	}
	int getRemNo(int r, int c) {
		return hint.getRemNo(r,c);
	}
	int getRemPattern(int r, int c) {
		return hint.getRemPattern(r,c);
	}
	/**
	 * そのマスを含むWordのマス数を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @param dir 縦か横か
	 * @return　そのマスを含むWordのマス数
	 */
	int getWordSize(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getSize();
		else if (dir == VERT)
			return wordV[r][c].getSize();
		return 0;
	}
	/**
	 * そのマスを含むWordの和の数字を表示したマスの座標を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @param dir 縦か横か
	 * @return　そのマスを含むWordの和の数字を表示したマスの座標
	 */
	int getWordHead(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getHead().c();
		else if (dir == VERT)
			return wordV[r][c].getHead().r();
		return 0;
	}
	/**
	 * そのマスを含むWordの和を返す
	 * @param r 行座標
	 * @param c 列座標
	 * @param dir 縦か横か
	 * @return　そのマスを含むWordの和
	 */
	int getWordSum(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getSum();
		else if (dir == VERT)
			return wordV[r][c].getSum();
		return 0;
	}
	/**
	 * そのマスを含むWordの完成状態
	 * @param r 行座標
	 * @param c 列座標
	 * @param dir 縦か横か
	 * @return　そのマスを含むWordの完成状態
	 * うまっていないマスがある : 0
	 * すべてのマスがうまっていて，合計は正しい : 1
	 * すべてのマスがうまっていて，合計は間違い : -1
	 * （うまっていないマスがあって，合計が正しくなりえない）は今は判定していない
	 */
	int getWordStatus(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getStatus();
		else if (dir == VERT)
			return wordV[r][c].getStatus();
		return 0;
	}
	/**
	 * １手の操作を表すクラス
	 * UNDO, REDO での編集の単位となる
	 */
	class Step extends AbstractUndoableEdit {

		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * コンストラクタ
		 * @param r 変更されたマスの行座標
		 * @param c 変更されたマスの列座標
		 * @param b 変更前の状態
		 * @param a 変更後の状態
		 */
		public Step(int r, int c, int b, int a) {
			super();
			row = r;
			col = c;
			before = b;
			after = a;
		}
		public void undo() throws CannotUndoException {
			super.undo();
			changeNumber(row, col, before);
		}
		public void redo() throws CannotRedoException {
			super.redo();
			changeNumber(row, col, after);
		}
		public boolean addEdit(UndoableEdit anEdit) {
			Step edit = (Step) anEdit;
			if (edit.row == row && edit.col == col) {
				after = edit.after;
				return true;
			} else {
				return false;
			}
		}
	}

}
