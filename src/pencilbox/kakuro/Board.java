package pencilbox.kakuro;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.util.ArrayUtil;

/**
 * �u�J�b�N���v �q���g�t���ՖʃN���X
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
	private int[][] multi; // �d����
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

	public boolean isWall(int r, int c) {
		if (!isOn(r,c))
			return true;
		return number[r][c] == WALL;
	}
	
	public boolean isWall(Address pos) {
		return isWall(pos.r(), pos.c());
	}
	/**
	 * �}�X�ɐ����������Ă��Ȃ����ǂ���
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
	 * �c�܂��͉��̌v�̒��ɁC���̃}�X�̐����Əd�����鐔�������邩�ǂ����𒲂ׂ�
	 * @param r �s���W
	 * @param c ����W
	 * @return�@�d������������� true
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
		updateHint(r, c);
	}
	
	public void changeNumber(Address pos, int n) {
		changeNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �}�X�ɐ�������͂��C�A���h�D���X�i�[�ɒʒm����
	 * @param pos �}�X���W
	 * @param n ���͂�������
	 */
	public void enterNumberA(Address pos, int n) {
		if (n < 0 || n > maxNumber) 
			return;
		if (n == getNumber(pos)) 
			return;
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(pos.r(), pos.c(), getNumber(pos), n)));
		changeNumber(pos, n);
	}

	/**
	 *  �d�����̏�����
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
	 * �����̏d�����̍X�V
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
			return Messages.getString("Board.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append(Messages.getString("Board.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("Board.AnswerCheckMessage3")); //$NON-NLS-1$
		return message.toString();
	}

	void initHint() {
		hint.initHint();
	}
	protected void updateHint(int r, int c) {
		hint.updatePattern(r, c);
	}

	int getPattern(int r, int c) {
		return hint.getPattern(r,c);
	}
	/**
	 * ���̃}�X���܂�Word�̃}�X����Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̃}�X��
	 */
	int getWordSize(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getSize();
		else if (dir == VERT)
			return wordV[r][c].getSize();
		return 0;
	}
	/**
	 * ���̃}�X���܂�Word�̘a�̐�����\�������}�X�̍��W��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̘a�̐�����\�������}�X�̍��W
	 */
	int getWordHead(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getHead().c();
		else if (dir == VERT)
			return wordV[r][c].getHead().r();
		return 0;
	}
	/**
	 * ���̃}�X���܂�Word�̘a��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̘a
	 */
	int getWordSum(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getSum();
		else if (dir == VERT)
			return wordV[r][c].getSum();
		return 0;
	}
	/**
	 * ���̃}�X���܂�Word�̊������
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̊������
	 * ���܂��Ă��Ȃ��}�X������ : 0
	 * ���ׂẴ}�X�����܂��Ă��āC���v�͐����� : 1
	 * ���ׂẴ}�X�����܂��Ă��āC���v�͊ԈႢ : -1
	 * �i���܂��Ă��Ȃ��}�X�������āC���v���������Ȃ肦�Ȃ��j�͍��͔��肵�Ă��Ȃ�
	 */
	int getWordStatus(int r, int c, int dir) {
		if (dir == HORIZ)
			return wordH[r][c].getStatus();
		else if (dir == VERT)
			return wordV[r][c].getStatus();
		return 0;
	}
	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractUndoableEdit {

		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * �R���X�g���N�^
		 * @param r �ύX���ꂽ�}�X�̍s���W
		 * @param c �ύX���ꂽ�}�X�̗���W
		 * @param b �ύX�O�̏��
		 * @param a �ύX��̏��
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
