package pencilbox.kakuro;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;
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
		hint = new DigitPatternHint();
		hint.setupHint(this);
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isWall(p)) {
				setNumber(p, 0);
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
	 * @return the sumH
	 */
	int[][] getSumH() {
		return sumH;
	}

	/**
	 * @return the sumV
	 */
	int[][] getSumV() {
		return sumV;
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

	public boolean isWall(int r, int c) {
		if (!isOn(r,c))
			return true;
		return number[r][c] == WALL;
	}
	
	public boolean isWall(Address pos) {
		return isWall(pos.r(), pos.c());
	}
	public int getSumV(int r, int c) {
		return sumV[r][c];
	}
	public int getSumV(Address p) {
		return sumV[p.r()][p.c()];
	}
	public int getSumH(int r, int c) {
		return sumH[r][c];
	}
	public int getSumH(Address p) {
		return sumH[p.r()][p.c()];
	}
	public int getSum(Address p, int dir) {
		if (dir == Direction.VERT)
			return sumV[p.r()][p.c()];
		if (dir == Direction.HORIZ)
			return sumH[p.r()][p.c()];
		return 0;
	}
	public void setSumV(int r, int c, int n) {
		number[r][c] = WALL;
		sumV[r][c] = n;
	}
	public void setSumH(int r, int c, int n) {
		number[r][c] = WALL;
		sumH[r][c] = n;
	}
	public void setSum(Address pos, int dir, int n) {
		if (dir == Direction.VERT)
			setSumV(pos.r(), pos.c(), n);
		if (dir == Direction.HORIZ)
			setSumH(pos.r(), pos.c(), n);
	}

	public void setWall(int r, int c, int a, int b) {
		number[r][c] = WALL;
		sumH[r][c] = a;
		sumV[r][c] = b;
	}
	public void setWall(Address pos, int a, int b) {
		setWall(pos.r(), pos.c(), a, b);
	}
	public void removeWall(int r, int c) {
		number[r][c] = 0;
		sumH[r][c] = 0;
		sumV[r][c] = 0;
	}
	public void removeWall(Address pos) {
		removeWall(pos.r(), pos.c());
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
							word.changeNumber(0, getNumber(r,cc));
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
							word.changeNumber(0, getNumber(rr,c));
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
	public boolean isMultipleNumber(Address p) {
		return multi[p.r()][p.c()] > 1;
	}

	/**
	 * マスに数字を入力し，アンドゥリスナーに通知する
	 * @param p マス座標
	 * @param n 入力した数字
	 */
	public void changeNumber(Address p, int n) {
		if (n < 0 || n > maxNumber) 
			return;
		if (n == getNumber(p)) 
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellNumberEditStep(p, getNumber(p), n));
		int r=p.r(), c=p.c();
		wordH[r][c].changeNumber(getNumber(r,c), n);
		wordV[r][c].changeNumber(getNumber(r,c), n);
		updateMulti(r, c, n);
		setNumber(r, c, n);
		updateHint(r, c);
	}
	
	public void undo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		changeNumber(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		changeNumber(s.getPos(), s.getAfter());
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
		for (Address p : cellAddrs()) {
			if (!isWall(p)) {
				if (getNumber(p) == 0) {
					result |= 1;
				} else if (isMultipleNumber(p)) {
					result |= 2;
				}
			} else if (isWall(p)) {
				if (getSumH(p) > 0 && getWordStatus(p, HORIZ) == -1) {
					result |= 4;
				}
				if (getSumV(p) > 0 && getWordStatus(p, VERT) == -1) {
					result |= 4;
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
			return Messages.getString("kakuro.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append(Messages.getString("kakuro.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("kakuro.AnswerCheckMessage3")); //$NON-NLS-1$
		return message.toString();
	}

	void initHint() {
		hint.initHint();
	}
	protected void updateHint(int r, int c) {
		hint.updatePattern(r, c);
	}

	int getPattern(Address p) {
		return hint.getPattern(p);
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
	int getWordStatus(Address p, int dir) {
		if (dir == HORIZ)
			return wordH[p.r()][p.c()].getStatus();
		else if (dir == VERT)
			return wordV[p.r()][p.c()].getStatus();
		return 0;
	}
}
