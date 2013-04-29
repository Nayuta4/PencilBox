package pencilbox.sudoku;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;


/**
 * �u���Ɓv�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0; // �𓚐������Ȃ����
	static final int BLANK = 0; // ��萔�����Ȃ����
	static final int UNDETERMINED = -2; // ��萔�������萔���̏��

	private int maxNumber; // ���̍ő吔��
	private int[][] state; // �𓚂̐���
	private int[][] number; // ���̐���
	private int unit = 0;

	private int[][] multi; // �d����

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
			throw new RuntimeException("�s���ȑ傫��"); //$NON-NLS-1$
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
	 * @return �{�b�N�X��1�ӂ̃}�X���B�ʏ�̐��Ƃł�3
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * ���̃}�X�͖��Ƃ��Đ�����^����ꂽ�}�X���ǂ���
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return ��萔���̃}�X�Ȃ� true, �𓚂��ׂ��}�X�Ȃ� false
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
	 * �����̍��W�̉\�����̃r�b�g�p�^�[�����擾
	 * @param r row coordinate
	 * @param c column coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(Address p){
		return hint.getPattern(p);
	}
	/**
	 * [r,c] �� n ��z�u�\���ǂ���
	 * @param r row coordinate
	 * @param c column coordinate
	 * @param n number to check
	 * @return true if n can placed at cell [r,c]
	 */
	boolean canPlace(Address p, int n) {
		return getNumber(p) == 0 && hint.canPlace(p, n);
	}
	/**
	 * �}�X�ɉ𓚐�������͂��C�A�h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param n ���͂��鐔��
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
	 * �}�X�ɖ�萔������͂��C�A�h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param n ���͂��鐔��
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
	 * �}�X�Ɠ����s�C��C�{�b�N�X�ɁC���̃}�X�̐����Əd�����鐔�������邩�ǂ����𒲂ׂ�
	 * @param p ���W
	 * @return�@�d������������� true
	 */
	public boolean isMultipleNumber(Address p) {
		return multi[p.r()][p.c()]>1;
	}

	/**
	 * ���݂̔Ֆʏ�Ԃ���Cmulti[][] �z�������������
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
	 *	p0�ɕω����������Ƃ���multi�z����X�V����
	 * @param p0 ��Ԃ�ύX�����}�X�̍��W
	 * @param prev �ύX�O�̐���
	 * @param n �ύX��̐���
	 */
	void updateMulti(Address p0, int prev, int n) {
		int r0=p0.r(), c0=p0.c();
		// prev�Ɠ���������T���ďd������-1����
		if (multi[r0][c0]>1) {
			updateMulti1(p0, prev, 0, -1);
		}
		// n�Ɠ���������T���ďd������+1����C�}�X���g�̏d������+1����
		if (n>0) {
			multi[r0][c0] = 1;
			updateMulti1(p0, n, +1, +1);
		} else {
			multi[r0][c0] = 0;
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
