package pencilbox.kakuro;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;

/**
 * �u�J�b�N���v �q���g�t���ՖʃN���X
 */
public class Board extends BoardBase {

	static final int WALL = -1; // ���}�X�ł��邱�Ƃ�\��(number�t�B�[���h�p)
	static final int BLANK = -2; // ���}�X�łȂ����Ƃ�\���i�ꎞ�ϐ��p�j
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
		for (Address p : cellAddrs()) {
			if (!isWall(p)) {
				setNumber(p, 0);
			}
		}
		for (Address p : cellAddrs()) {
			setMulti(p, 0);
		}
		for (Address p : cellAddrs()) {
			for (int d = 0; d < 2; d++) {
				if (getSum(p, d) > 0)
					getWord(p, d).clear();
			}
		}
		initHint();
	}

	public void clearQuestion() {
		for (Address p : cellAddrs()) {
			if (isWall(p)) {
				for (int d = 0; d < 2; d++) {
					Address pp = Address.nextCell(p, d|2);
					if (isOn(pp) && !isWall(pp)) {
						setSum(p, d, 0);
					}
				}
			}
		}
		initBoard();
	}

	public void reconstructQuestion() {
		for (Address p : cellAddrs()) {
			if (isWall(p)) {
				for (int d = 0; d < 2; d++) {
					Address pp = Address.nextCell(p, d|2);
					if (isOn(pp) && !isWall(pp)) {
						setSum(p, d, getWord(pp, d).extractSum());
					}
				}
			}
		}
		initBoard();
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

	/**
	 * �A���h�D�L�^�p�ɍ��}�X�̂Q�̐��������킹���������擾����B
	 * @param p
	 * @return
	 */
	public int getSum2(Address p) {
		return getSum(p, Direction.VERT) | (getSum(p, Direction.HORIZ)<<8);
	}

	public Word getWord(Address p, int dir) {
		if (dir == Direction.VERT)
			return wordV[p.r()][p.c()];
		if (dir == Direction.HORIZ)
			return wordH[p.r()][p.c()];
		return null;
	}

	public void setWord(Address p, int dir, Word w) {
		if (dir == Direction.VERT)
			wordV[p.r()][p.c()] = w;
		if (dir == Direction.HORIZ)
			wordH[p.r()][p.c()] = w;
	}

	public int getMulti(Address pos) {
		return multi[pos.r()][pos.c()];
	}

	public void setMulti(Address pos, int n) {
		multi[pos.r()][pos.c()] = n;
	}

	public void initBoard() {
		initWord();
		initMulti();
		initHint();
	}

	void initWord() {
		for (Address p : cellAddrs()) {
			if (getNumber(p) == WALL) {
				for (int d = 0; d < 2; d++) {
					Address pp = Address.nextCell(p, d|2);
					int i = 0;
					while (isOn(pp) && getNumber(pp) != WALL) {
						pp = Address.nextCell(pp, d|2);
						i++;
					}
					Word word = new Word(p, i, getSum(p, d));
					setWord(p, d, word);
					pp = Address.nextCell(p, d|2);
					while (isOn(pp) && getNumber(pp) != WALL) {
						setWord(pp, d, word);
						if (getNumber(pp) > 0) {
							word.changeNumber(0, getNumber(pp));
						}
						pp = Address.nextCell(pp, d|2);
					}
				}
			}
		}
	}
	/**
	 * �c�܂��͉��̌v�̒��ɁC���̃}�X�̐����Əd�����鐔�������邩�ǂ����𒲂ׂ�
	 * @param p ���W
	 * @return�@�d������������� true
	 */
	public boolean isMultipleNumber(Address p) {
		return getMulti(p) > 1;
	}

	/**
	 * ���}�X�̐�����ύX����
	 * @param p ���W
	 * @param dir ���}�X�̏c�̌v��\�����������̌v��\��������
	 * @param n �ύX��̐�
	 */
	public void changeSum(Address p, int dir, int n) {
		int nn = 0;
		if (dir == Direction.VERT) {
			nn = n | (getSum(p, Direction.HORIZ)<<8);
		} else if (dir == Direction.HORIZ) {
			nn = getSum(p, Direction.VERT) | (n<<8);
		}
		changeWall(p, nn);
	}
	/**
	 * ���}�X�ύX
	 * @param p �}�X���W
	 * @param n 0�Ȃ琔���̂Ȃ��ǁC-2�Ȃ�ǂ����� ���̐��Ȃ�c���𐳂������킹����
	 */
	public void changeWall(Address p, int n) {
		if (getNumber(p) > 0) {
			changeAnswerNumber(p, 0);
		}
		int prev = isWall(p) ? getSum2(p) : BLANK;
		if (n == prev) 
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		if (n == BLANK) {
			removeWall(p);
		} else {
			setWall(p, 0, 0);
			setSum(p, Direction.VERT, n&255);
			setSum(p, Direction.HORIZ, (n>>8)&255);
		}
	}

	/**
	 * �}�X�ɐ�������͂��C�A���h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param n ���͂�������
	 */
	public void changeAnswerNumber(Address p, int n) {
		if (isWall(p)) {
			changeWall(p, 0);
		}
		int prev = getNumber(p);
		if (n == prev) 
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.NUMBER, p, prev, n));
		getWord(p, Direction.HORIZ).changeNumber(prev, n);
		getWord(p, Direction.VERT).changeNumber(prev, n);
		updateMulti(p, prev, n);
		setNumber(p, n);
		updateHint(p);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getBefore());
			} else if (s.getType() == EditType.FIXED) {
				changeWall(s.getPos(), s.getBefore());
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getAfter());
			} else if (s.getType() == EditType.FIXED) {
				changeWall(s.getPos(), s.getAfter());
			}
		}
	}

	/**
	 *  �d�����̏�����
	 */
	void initMulti() {
		for (Address p : cellAddrs()) {
			int n = getNumber(p);
			if (n > 0) {
				setMulti(p, 1);
				updateMulti1(p, n, +1, 0);
			}
		}
	}

	/**
	 * �����̏d�����̍X�V
	 */
	void updateMulti(Address p0, int prev, int num) {
		// prev�Ɠ���������T���ďd������-1����
		if (getMulti(p0) > 1) {	
			updateMulti1(p0, prev, 0, -1);
		}
		// num�Ɠ���������T���ďd������+1����C�}�X���g�̏d������+1����
		if (num > 0) {
			setMulti(p0, 1);
			updateMulti1(p0, num, +1, +1);
		} else if (num == 0) {
			setMulti(p0, 0);
		}
	}

	/**
	 * p0�̐����̕ύX�ɉ����ďd�����𐔂���multi[][]�z����X�V����
	 * �͈͂̐��������āCnum �Ɠ��������̃}�X����������p0�̒�������m, ���̃}�X�̒�������k�ύX����B
	 * @param p0 ��Ԃ�ύX�����}�X�̍��W
	 * @param num ���ׂ鐔��
	 * @param m �����̏d�����X�V��
	 * @param k ����̏d�����X�V��
	 */
	private void updateMulti1(Address p0, int num, int m, int k) {
		for (int d = 0; d < 2; d++) {
			Address p = getWordHead(p0, d);
			for (int i = 0; i < getWordSize(p0, d); i++) {
				p = Address.nextCell(p, d|2);
				if (p.equals(p0))
					continue;
				if (getNumber(p) == num) {
					setMulti(p, getMulti(p)+k);
					setMulti(p0, getMulti(p0)+m);
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
				if (getSumH(p) > 0 && getWordStatus(p, Direction.HORIZ) == -1) {
					result |= 4;
				}
				if (getSumV(p) > 0 && getWordStatus(p, Direction.VERT) == -1) {
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
	protected void updateHint(Address p) {
		hint.updatePattern(p);
	}

	int getPattern(Address p) {
		return hint.getPattern(p);
	}
	/**
	 * ���̃}�X���܂�Word�̃}�X����Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̃}�X��
	 */
	int getWordSize(Address p, int dir) {
		return getWord(p, dir).getSize();
	}
	/**
	 * ���̃}�X���܂�Word�̘a�̐�����\�������}�X�̍��W��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̘a�̐�����\�������}�X�̍��W
	 */
	Address getWordHead(Address p, int dir) {
		return getWord(p, dir).getHead();
	}
	/**
	 * ���̃}�X���܂�Word�̘a��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c������
	 * @return�@���̃}�X���܂�Word�̘a
	 */
	int getWordSum(Address p, int dir) {
		return getWord(p, dir).getSum();
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
	int getWordStatus(Address p, int dir) {
		return getWord(p, dir).getStatus();
	}
}
