package pencilbox.sudoku;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;


/**
 * �u���Ɓv�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;

	private int maxNumber; // ���̍ő吔��
	private int[][] state; // ���̐���:1, �𓚂��ׂ�����:0,
	private int[][] number;
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
	 * �}�X�ɐ����������Ă��Ȃ����ǂ���
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
	 * �����̍��W�̉\�����̃r�b�g�p�^�[�����擾
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(int r, int c){
		return hint.getPattern(r,c);
	}
	/**
	 * [r,c] �� n ��z�u�\���ǂ���
	 * @param r row coordinate
	 * @param c colmun coordinate
	 * @param n number to check
	 * @return true if n can placed at cell [r,c]
	 */
	boolean canPlace(int r, int c, int n) {
		return getNumber(r,c) == 0 && hint.canPlace(r, c, n);
	}
	/**
	 * �}�X�ɐ�������͂��C�A�h�D���X�i�[�ɒʒm����
	 * @param pos �}�X���W
	 * @param n ���͂��鐔��
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
	 * �}�X�Ɠ����s�C��C�{�b�N�X�ɁC���̃}�X�̐����Əd�����鐔�������邩�ǂ����𒲂ׂ�
	 * @param r �s���W
	 * @param c ����W
	 * @return�@�d������������� true
	 */
	public boolean isMultipleNumber(int r, int c) {
		return multi[r][c]>1;
	}
	
	/**
	 * ���݂̔Ֆʏ�Ԃ���Cmulti[][] �z�������������
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
	 *	[r0, c0]�ɕω����������Ƃ���multi�z����X�V����
	 * @param r0 ��Ԃ�ύX�����}�X�̍s���W
	 * @param c0 ��Ԃ�ύX�����}�X�̗���W
	 * @param num �ύX��̃}�X�̐���
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
	 * [r0, c0] �ɐ���num(>0)������Ƃ��ɁCmulti[][]���X�V����
	 * @param r0 ��Ԃ�ύX�����}�X�̍s���W
	 * @param c0 ��Ԃ�ύX�����}�X�̗���W
	 * @param num �ύX��̃}�X�̐���
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
	 * [r0, c0] �̐���prevNum(>0)�������Cmulti[][]���X�V����
	 * @param r0 ��Ԃ�ύX�����}�X�̍s���W
	 * @param c0 ��Ԃ�ύX�����}�X�̗���W
	 * @param prevNum �ύX�O�̃}�X�ɓ����Ă�������
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
