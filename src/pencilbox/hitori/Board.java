package pencilbox.hitori;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

/**
 * �u�ЂƂ�ɂ��Ă���v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int UNKNOWN = 0;
	static final int UNDECIDED_NUMBER = -1;

	private int[][] state;
	private int[][] number;
	private int[][] multiH; // �������̏d����
	private int[][] multiV; // �c�����̏d����
	private boolean[][] single; // �ŏ�����ЂƂ肩
	private int[][] chain; // ���}�X�΂ߘA��
	private int maxNumber; // �g�p�\������
	private int maxChain = 1; // ���ݎg�p���Ă���ő��chain�ԍ�

	protected void setup() {
		super.setup();
		int rows = rows();
		int cols = cols();
		number = new int[rows][cols];
		state = new int[rows][cols];
		single = new boolean[rows][cols];
		multiH = new int[rows][cols];
		multiV = new int[rows][cols];
		chain = new int[rows][cols];
		maxNumber = (rows > cols) ? rows : cols;
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt2(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE) {
				changeState(p, UNKNOWN);
			}
		}
	}

	public void initBoard() {
		initSingle();
		initMulti();
		initChain();
	}
	
	/**
	 * �}�X�̏�Ԃ��擾����
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return �}�X�̏�Ԃ�\�� state[r][c] �̒l
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * �}�X�̏�Ԃ�ݒ肷��
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @param st state[r][c] �ɐݒ肷��l
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * �}�X�̐������擾����
	 * @param r row coordinate of the cell.
	 * @param c columun coordinate of the cell.
	 * @return Returns the number of the cell.
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * �}�X�ɐ�����ݒ肷��
	 * @param r row coordinate of the cell.
	 * @param c columun coordinate of the cell.
	 * @param n the number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �����̍��W�����}�X���ǂ����B
	 * @param p ���W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(Address p) {
		return isOn(p) && getState(p) == BLACK;
	}
	/**
	 * ���̃}�X�Əc�܂��͉��̓�����ɁC���}�X�ŏ�����Ă��Ȃ��������������邩
	 * @param p ���W
	 * @return ���}�X�ŏ�����Ă��Ȃ���������������� true, �Ȃ���� false
	 */
	public boolean isRedundantNumber(Address p) {
		return multiH[p.r()][p.c()] > 1 || multiV[p.r()][p.c()] > 1;
	}
	/**
	 * ���̃}�X�̐������ŏ�����ЂƂ肩�ǂ����C
	 * �܂�c���̓�����ɓ���̐��������݂��Ȃ����ǂ�����Ԃ�
	 * @param p ���W 
	 * @return �ŏ�����ЂƂ�Ȃ� true, �����łȂ���� false
	 */
	public boolean isSingle(Address p) {
		return single[p.r()][p.c()];
	}

	int getChain(Address p) {
		return chain[p.r()][p.c()];
	}

	void setChain(Address p, int n) {
		chain[p.r()][p.c()] = n;
	}
	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param n �ύX��̏��
	 */
	public void changeNumber(Address p, int n) {
		int prev = getNumber(p);
		if (n == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
	}

	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param st �ύX��̏��
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, prev, st));
		setState(p, st);
		if (st == BLACK) {
			updateMulti(p, -1);
			connectChain(p);
		} else if (prev == BLACK) {
			updateMulti(p, +1);
			cutChain(p);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.STATE) {
				changeState(s.getPos(), s.getBefore());
			} else if (step.getType() == EditType.FIXED) {
				changeNumber(s.getPos(), s.getBefore());
			}
		} 
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.STATE) {
				changeState(s.getPos(), s.getAfter());
			} else if (step.getType() == EditType.FIXED) {
				changeNumber(s.getPos(), s.getAfter());
			}
		}
	}
	/**
	 * ���̃}�X�̏㉺���E�̗אڂS�}�X�ɍ��}�X�����邩�ǂ����𒲂ׂ�
	 * @param p
	 * @return �㉺���E�ɍ��}�X���ЂƂł������ true
	 */
	boolean isBlock(Address p) {
		for (int d=0; d<4; d++) {
			if (isBlack(Address.nextCell(p, d)))
				return true;
		}
		return false;
	}
	/**
	 * 	chain�z�������������
	 */
	void initChain() {
		maxChain = 1;
		for (Address p : cellAddrs()) {
			setChain(p, 0);
		}
		for (Address p : cellAddrs()) {
			if (isOnPeriphery(p)) {
				if (isBlack(p) && getChain(p) == 0) {
					if (initChain1(p, -1, 1) == -1) {
						updateChain(p, -1);
					}
				}
			}
		}
		for (Address p : cellAddrs()) {
			if (!isOnPeriphery(p)) {
				if (isBlack(p) && getChain(p) == 0) {
					if (initChain1(p, -1, ++maxChain) == -1) {
						updateChain(p, -1);
					}
				}
			}
		}
	}
	/**
	 * �΂߂ɂȂ��鍕�}�X�����ǂ�Cchain �ɔԍ� n ��ݒ肷��
	 * ���f�𔭌�������C���̎��_�� -1 ��Ԃ��Ė߂�
	 * @param p ���̃}�X
	 * @param d �Ăяo�����̃}�X���獡�̃}�X�����������C���̃}�X�����߂Ȃ� -1
	 * @param n �ݒ肷��l
	 * @return �Ֆʂ̕��f�𔭌������� -1 , �����łȂ���� n �Ɠ����l
	 */
	int initChain1(Address p, int d, int n) {
		if (n == 1 && d != -1 && isOnPeriphery(p)) { // �ւ��O���ɒB����
			return -1;
		}
		if (n >= 0 && isOnPeriphery(p)) {
			setChain(p, 1);
		} else {
			setChain(p, n);
		}
		for (int dd : Direction.DIAGONAL4) {
			Address pp = Address.nextCell(p, dd);
			if (dd == (d^2))
				continue; // �������Ƃ���͂Ƃ΂�
			if (!isBlack(pp))
				continue; // ���}�X�ȊO�͂Ƃ΂�
			if (getChain(pp) == n) // �ւ�����
				return -1;
			if (initChain1(pp, dd, n) == -1)
				return -1;
		}
		return n;
	}
	private int[] adjacentChain = new int[4];
	/**
	 * 	���Ŋm�肵���Ƃ��ɁC���̃}�X����_�Ƃ���chain���X�V����D
	 * 	���̃}�X���m�肵�����Ƃɂ��C�V�K�ɕ��f���������邩�𒲂ׁC
	 * 	��������Ȃ� chain �S�̂� -1 �ōX�V����D
	 * 	�������Ȃ��Ȃ�C�΂ߗא�4�}�X�̍ŏ��l�ɂ��킹��D
	 * 	�΂ߗׂɍ��}�X���Ȃ���΁C�V�����ԍ�������D
	 * @param p �}�X�̍��W
	 */
	void connectChain(Address p) {
		int[] adjacent = adjacentChain;
		int k = 0;
		int newChain = Integer.MAX_VALUE;
		if (isOnPeriphery(p))
			newChain = 1;
		for (int dd : Direction.DIAGONAL4) {
			Address pp = Address.nextCell(p, dd);
			if (!isBlack(pp))
				continue; // ���}�X�ȊO�͂Ƃ΂�
			int c1 = getChain(pp);
			if (isOnPeriphery(p) && c1 == 1) {
				newChain = -1; // �[�̃}�X�ɂ���Ƃ��ԍ�1�����������番�f���ꂽ
			} 
			adjacent[k] = c1;
			for (int l = 0; l < k; l++) {
				if (adjacent[k] == adjacent[l]) // �����ԍ������������番�f���ꂽ
					newChain = -1;
			}
			k++;
			if (c1 < newChain)
				newChain = c1;
		}
		if (newChain == Integer.MAX_VALUE)
			setChain(p, ++maxChain); // ���͂ɍ��}�X���Ȃ��Ƃ��C�V�����ԍ�������
		else
			updateChain(p, newChain); // ���͂ɍ��}�X������Ƃ��C���̍ŏ��ԍ�������
	}
	/**
	 * ���}�X�����������Ƃ��ɁCchain���X�V����
	 * �S���v�Z���Ȃ������Ƃɂ���
	 * @param p
	 */
	void cutChain(Address p) {
		initChain();
	}
	/**
	 * 	�}�X�� chain�ԍ���ݒ肷��
	 * 	�΂ߗׂɍ��}�X������Γ����ԍ���ݒ肷��
	 * @param p �}�X�̍��W
	 * @param n �ݒ肷��l
	 */
	void updateChain(Address p, int n) {
		setChain(p, n);
		for (int dd : Direction.DIAGONAL4) {
			Address pp = Address.nextCell(p, dd);
			if (!isBlack(pp))
				continue; // ���}�X�ȊO�͂Ƃ΂�
			if (getChain(pp) == n)
				continue; // �����ԍ����������炻�̂܂�
			updateChain(pp, n);
		}
	}

	/**
	 * ���}�X�ɂ��Ֆʂ����f����Ă��Ȃ����ǂ����𒲍�����
	 * @return ���}�X�ɂ��Ֆʂ����f����Ă��Ȃ���� true ���f����Ă���� false ��Ԃ�
	 */
	boolean checkDivision() {
		boolean ret = true;
		for (Address p : cellAddrs()) {
			if (getChain(p) == -1) {
				ret = false;
			}
		}
		return ret;
	}
	/**
	 * �ՖʑS�̂ŁC�c���ɘA�����鍕�}�X���Ȃ����ǂ����𒲍�����
	 * @return�@�A�����鍕�}�X���Ȃ���� true, ����� false ��Ԃ��@
	 */
	boolean checkContinuousBlack() {
		boolean ret = true;
		for (Address p : cellAddrs()) {
			if (isBlack(p)) {
				if (isBlock(p)) {
					ret = false;
				}
			}
		}
		return ret;
	}
	/**
	 * �ՖʑS�̂ŁC�c���ɍ��}�X�ŏ����ꂸ�ɏd�����鐔�����Ȃ����𒲍�����
	 * @return �d���������Ȃ���� true ����� false
	 */
	boolean checkMulti() {
		for (Address p : cellAddrs()) {
			if (isRedundantNumber(p))
				return false;
		}
		return true;
	}
	
	public int checkAnswerCode() {
		int result = 0;
		if (!checkContinuousBlack())
			result |= 1;
		if (!checkDivision())
			result |= 2;
		if (!checkMulti())
			result |= 4;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append ( Messages.getString("hitori.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append (Messages.getString("hitori.AnswerCheckMessage2")) ; //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append (Messages.getString("hitori.AnswerCheckMessage3")) ; //$NON-NLS-1$
		return message.toString();
	}

	/**
	 *  single�z�������������
	 */
	void initSingle() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				single[r][c] = true;
			}
		}
		int[] used = new int[maxNumber + 1];
		for (int r = 0; r < rows(); r++) {
			for (int i = 1; i <= maxNumber; i++)
				used[i] = 0;
			for (int c = 0; c < cols(); c++) {
				used[number[r][c]]++;
			}
			for (int c = 0; c < cols(); c++) {
				if (used[number[r][c]] > 1)
					single[r][c] = false;
			}
		}
		for (int c = 0; c < cols(); c++) {
			for (int i = 1; i <= maxNumber; i++)
				used[i] = 0;
			for (int r = 0; r < rows(); r++) {
				used[number[r][c]]++;
			}
			for (int r = 0; r < rows(); r++) {
				if (used[number[r][c]] > 1)
					single[r][c] = false;
			}
		}
	}
	/**
	 * 	hmulti, vmulti �z�������������
	 */
	void initMulti() {
		int[] used = new int[maxNumber + 1];
		for (int r = 0; r < rows(); r++) {
			for (int i = 0; i <= maxNumber; i++)
				used[i] = 0;
			for (int c = 0; c < cols(); c++) {
				if (state[r][c] != BLACK && number[r][c] > 0)
					used[number[r][c]]++;
			}
			for (int c = 0; c < cols(); c++) {
				multiH[r][c] = used[number[r][c]];
			}
		}
		for (int c = 0; c < cols(); c++) {
			for (int i = 0; i <= maxNumber; i++)
				used[i] = 0;
			for (int r = 0; r < rows(); r++) {
				if (state[r][c] != BLACK && number[r][c] > 0)
					used[number[r][c]]++;
			}
			for (int r = 0; r < rows(); r++) {
				multiV[r][c] = used[number[r][c]];
			}
		}
	}

	/**
	 * 	�}�X���m�肵���Ƃ��ɁC�����s�C��� hmulti, vmulti ��ύX����
	 * @param p0 ��Ԃ�ύX�����}�X�̍��W
	 * @param k ��������l
	 */
	private void updateMulti(Address p0, int k) {
		int r0=p0.r(), c0=p0.c();
		for (int c = 0; c < cols(); c++) {
			if (number[r0][c] == number[r0][c0]) {
				multiH[r0][c] += k;
			}
		}
		for (int r = 0; r < rows(); r++) {
			if (number[r][c0] == number[r0][c0]) {
				multiV[r][c0] += k;
			}
		}
	}

	/**
	 * @return Returns the maxNumber.
	 */
	int getMaxNumber() {
		return maxNumber;
	}
	/**
	 * @return Returns the chain.
	 */
	int[][] getChain() {
		return chain;
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
}
