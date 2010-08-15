package pencilbox.kurodoko;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;

/**
 * �u���}�X�͂ǂ����v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int HORIZ = Direction.HORIZ; 
	static final int VERT = Direction.VERT;

	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int UNKNOWN = 0;
	static final int OUT = -3;
	static int UNDECIDED_NUMBER = -4;

	private int[][] state;
	private int[][] chain; // ���}�X�̎΂߂Ȃ�����L�^����
	private int maxChain;
	private Number[][] number;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		chain = new int[rows()][cols()];
		number = new Number[rows()][cols()];
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isNumber(p))
				setState(p,UNKNOWN);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE) {
				changeState(p, UNKNOWN);
			}
		}
		initNumber();
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}

	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}

	public void setState(int r, int c, int st ) {
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * �����̃}�X���������ǂ���
	 */
	public boolean isNumber(int r, int c) {
		return state[r][c] > 0;
	}
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	/**
	 * �����̍��W�����}�X���ǂ����B
	 * @param p ���W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(Address p) {
		return isOn(p) && getState(p) == BLACK;
	}

	public boolean isWhiteOrNumber(Address p) {
		int n = getState(p);
		return n == WHITE || n>0 || n == UNDECIDED_NUMBER;
	}

	public void initBoard() {
		initChain();
		initNumber();
	}

	void initNumber() {
		for (Address p : cellAddrs()) {
			if (isNumber(p)) {
				setNumber(p, new Number(getState(p)));
				initNumber(p);
			} else {
				setNumber(p, null);
			}
		}
	}

	/**
	 * �}�X p0 ���� d �����Ɍ����Ƃ��̔��}�X�̐��𒲂ׂ�
	 * @param p0
	 */
	void initNumber(Address p0) {
		int n=1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(isOn(p) && !isBlack(p)) {
				p = p.nextCell(d);
				if (!isOn(p))
					break;
				if (isBlack(p))
					break;
				n++;
			};
		}
		getNumber(p0).setNSpace(n);
		n=1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(true) {
				p = p.nextCell(d);
				if (!isOn(p))
					break;
				if (!isWhiteOrNumber(p))
					break;
				n++;
			};
		}
		getNumber(p0).setNWhite(n);
	}

	public Number getNumber(Address p) {
		return number[p.r()][p.c()];
	}

	public void setNumber(Address p, Number n) {
		number[p.r()][p.c()] = n;
	}
	/**
	 * ���}�X�̎΂߂Ȃ���ԍ���Ԃ�
	 * @param p cell coordinate
	 * @return Returns the chain.
	 */
	int getChain(Address p) {
		return chain[p.r()][p.c()];
	}

	void setChain(Address p, int n) {
		chain[p.r()][p.c()] = n;
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
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		setState(p, st);
		if (st == BLACK) {
			connectChain(p);
		} else if (prev == BLACK) {
			cutChain(p);
		}
		if (st > 0) {
			setNumber(p, new Number(st));
			initNumber(p);
		} else if (prev > 0) {
			setNumber(p, null);
		}
		if (st == BLACK || prev == BLACK)
			updateSpace(p);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getAfter());
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
	 * �}�X�̏�Ԃ�ύX�����Ƃ��ɁC���̃}�X�̏㉺���E�̐����}�X��T���Ĕ��}�X���𐔂�����
	 */
	void updateSpace(Address p0) {
		for (int d=0; d<4; d++) {
			Address p = p0;
			while(true) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (isBlack(p))
					break;
				if (isNumber(p)) {
					initNumber(p);
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (isBlack(p)) {
				if (isBlock(p))
					result |= (1<<0);
				if (getChain(p) == -1)
					result |= (1<<1);
			}
			if (isNumber(p)) {
				int remainder = getNumber(p).getNSpace() - getNumber(p).getNumber();
				if (remainder < 0)
					result |= (1<<2);
				else if (remainder > 0)
					result |= (1<<3);
			}
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append(ERR_CONTINUOUS_BLACK);
		if ((result&2) == 2)
			message.append(ERR_DIVIDED_BOARD);
		if ((result&4) == 4)
			message.append(ERR_SMALL_SIZE);
		if ((result&8) == 8)
			message.append(YET_LARGE_SIZE);
		return message.toString();
	}

	static final String ERR_CONTINUOUS_BLACK = Messages.getString("kurodoko.AnswerCheckMessage1"); //$NON-NLS-1$
	static final String ERR_DIVIDED_BOARD = Messages.getString("kurodoko.AnswerCheckMessage2"); //$NON-NLS-1$
	static final String ERR_SMALL_SIZE = Messages.getString("kurodoko.AnswerCheckMessage3"); //$NON-NLS-1$
	static final String YET_LARGE_SIZE = Messages.getString("kurodoko.AnswerCheckMessage4"); //$NON-NLS-1$
}
