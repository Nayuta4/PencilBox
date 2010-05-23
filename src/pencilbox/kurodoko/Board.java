package pencilbox.kurodoko;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (state[r][c] == BLACK || state[r][c] == WHITE)
					state[r][c] = UNKNOWN;
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) == WHITE)
					setState(r, c, UNKNOWN);
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
	 * @param r �s���W
	 * @param c ����W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && state[r][c] == BLACK;
	}
	public boolean isWhiteOrNumber(int r, int c) {
		return state[r][c] == WHITE || state[r][c]>0 || state[r][c] == UNDECIDED_NUMBER;
	}

	public void initBoard() {
		initChain();
		initNumber();
	}
	void initNumber() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isNumber(r,c)) {
					number[r][c] = new Number(getState(r,c));
					initNumber(r, c);
				} else
					number[r][c] = null;
			}
		}
	}
	public Number getNumber(int r, int c) {
		return number[r][c];
	}
	
	public Number getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	
	public void setNumber(int r, int c, int n) {
		setState(r,c,n);
		number[r][c] = new Number(n);
		initNumber(r, c);
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * ���}�X�̎΂߂Ȃ���ԍ���Ԃ�
	 * @param r row coordinate
	 * @param c column coordinate
	 * @return Returns the chain.
	 */
	int getChain(int r, int c) {
		return chain[r][c];
	}
	int getChain(Address p) {
		return chain[p.r()][p.c()];
	}

	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param st �ύX��̏��
	 */
	public void changeState(Address p, int st) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, getState(p), st));
		int r=p.r(), c=p.c();
		int prevState = getState(r,c);
		setState(r,c,st);
		if (st == BLACK) {
			connectChain(r, c);
		} else if (prevState == BLACK) {
			cutChain(r, c);
		}
		updateSpace(r, c);
	}
	
	public void undo(AbstractStep step) {
		CellEditStep s = (CellEditStep)step;
		changeState(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellEditStep s = (CellEditStep)step;
		changeState(s.getPos(), s.getAfter());
	}

	/**
	 * ���̃}�X�̏㉺���E�̗אڂS�}�X�ɍ��}�X�����邩�ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return �㉺���E�ɍ��}�X���ЂƂł������ true
	 */
	boolean isBlock(int r, int c) {
		if (isBlack(r-1, c) || isBlack(r+1, c) || isBlack(r, c-1) || isBlack(r, c+1))
			return true;
		return false;
	}
	
	boolean isBlock(Address pos) {
		return isBlock(pos.r(), pos.c());
	}

	/**
	 * 	chain�z�������������
	 */
	void initChain() {
		maxChain = 1;
		ArrayUtil.initArrayInt2(chain,0);
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (!isOnPeriphery(r, c))
					continue;
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, 1) == -1) {
						updateChain(r, c, -1);
					}
				}
			}
		}
		for (int r = 1; r < rows() - 1; r++) {
			for (int c = 1; c < cols() - 1; c++) {
				if (isBlack(r, c) && chain[r][c] == 0) {
					if (initChain1(r, c, 0, 0, ++maxChain) == -1) {
						updateChain(r, c, -1);
					}
				}
			}
		}
	}
	/**
	 * �΂߂ɂȂ��鍕�}�X�����ǂ�Cchain �ɔԍ� n ��ݒ肷��
	 * ���f�𔭌�������C���̎��_�� -1 ��Ԃ��Ė߂�
	 * @param r
	 * @param c
	 * @param uu
	 * @param vv
	 * @param n
	 * @return �Ֆʂ̕��f�𔭌������� -1 , �����łȂ���� n �Ɠ����l
	 */
	int initChain1(int r, int c, int uu, int vv, int n) {
		if (n == 1 && uu != 0 && isOnPeriphery(r, c)) { // �ւ��O���ɒB����
			return -1;
		}
		if (n >= 0 && isOnPeriphery(r, c)) {
			chain[r][c] = 1;
		} else {
			chain[r][c] = n;
		}
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if ((u == -uu) && (v == -vv))
					continue; // �������Ƃ���͂Ƃ΂�
				if (!isBlack(r + u, c + v))
					continue; // ���}�X�ȊO�͂Ƃ΂�
				if (chain[r + u][c + v] == n) // �ւ�����
					return -1;
				if (initChain1(r + u, c + v, u, v, n) == -1)
					return -1;
			}
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
	 * @param r
	 * @param c
	 */
	void connectChain(int r, int c) {
		int[] adjacent = adjacentChain;
		int k = 0;
		int newChain = Integer.MAX_VALUE;
		if (isOnPeriphery(r,c))
			newChain = 1;
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if (!isBlack(r + u, c + v))
					continue; // ���}�X�ȊO�͂Ƃ΂�
				if (isOnPeriphery(r, c) && chain[r + u][c + v] == 1) {
					newChain = -1; // �[�̃}�X�ɂ���Ƃ��ԍ�1������������
				} 
				adjacent[k] = chain[r + u][c + v];
				for (int l = 0; l < k; l++) {
					if (adjacent[k] == adjacent[l]) // �����ԍ�������������
						newChain = -1;
				}
				k++;
				if (chain[r + u][c + v] < newChain)
					newChain = chain[r + u][c + v];
			}
		}
		if (newChain == Integer.MAX_VALUE)
			chain[r][c] = ++maxChain; // ���͂ɍ��}�X���Ȃ��Ƃ��C�V�����ԍ�������
		else
			updateChain(r, c, newChain); // ���͂ɍ��}�X������Ƃ��C���̍ŏ��ԍ�������
	}
	/**
	 * ���}�X�����������Ƃ��ɁCchain���X�V����
	 * �S���v�Z���Ȃ������Ƃɂ���
	 * @param r
	 * @param c
	 */
	void cutChain(int r, int c) {
		initChain();
	}
	/**
	 * 	�}�X�� chain�ԍ���ݒ肷��
	 * 	�΂ߗׂɍ��}�X������Γ����ԍ���ݒ肷��
	 * @param r
	 * @param c
	 * @param n
	 */
	void updateChain(int r, int c, int n) {
		chain[r][c] = n;
		for (int u = -1; u < 2; u += 2) {
			for (int v = -1; v < 2; v += 2) {
				if (!isBlack(r + u, c + v))
					continue; // ���}�X�ȊO�͂Ƃ΂�
				if (chain[r + u][c + v] == n)
					continue; // �����ԍ����������炻�̂܂�
				updateChain(r + u, c + v, n);
			}
		}
	}
	
	int initNumber(int r0, int c0, int direction) {
		int n=0;
		Address pos = Address.address(r0, c0);
		while(true) {
			pos = pos.nextCell(direction);
			if (!isOn(pos.r(),pos.c()))
				break;
			if (isBlack(pos.r(), pos.c()))
				break;
			n++;
		};
		number[r0][c0].setNSpace(direction, n);

		pos = Address.address(r0, c0);
		n = 0;
		while(true) {
			pos = pos.nextCell(direction);
			if (!isOn(pos.r(),pos.c()))
				break;
			if (!isWhiteOrNumber(pos.r(), pos.c()))
				break;
			n++;
		};
		number[r0][c0].setNWhite(direction, n);

		if (number[r0][c0].tooSmallSpace()) return -1;
		if (number[r0][c0].tooLargeWhite()) return -1;
		return 0;
	}

	void initNumber(int r0, int c0) {
		  initNumber(r0, c0, Direction.UP);
		  initNumber(r0, c0, Direction.DN);
		  initNumber(r0, c0, Direction.LT);
		  initNumber(r0, c0, Direction.RT);
	}
	/**
	 * �}�X�̏�Ԃ�ύX�����Ƃ��ɁC���̃}�X�̏㉺���E�̐����N���X�������X�V����
	 * space �� white �𗼕��X�V����
	 * ��蔭������-1���C�ʏ펞��0��Ԃ�
	 */
	int updateSpace(int r0, int c0) {
		int ret = 0;
		for (int d=0; d<4; d++) {
			Address pos = Address.address(r0, c0);
			while(true) {
				pos = pos.nextCell(d);
				if (!isOn(pos.r(), pos.c()))
					break;
				if (isBlack(pos.r(), pos.c()))
					break;
				if (isNumber(pos.r(), pos.c())) {
					ret += initNumber(pos.r(), pos.c(), d^2);
				}
			}
		}
		if (ret<0) return -1;
		else return 0;
	}
	
	int getSumSpace(Address p) {
		return getNumber(p).getSumSpace();
	}
	int getSumWhite(Address p) {
		return getNumber(p).getSumWhite();
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isBlack(r, c)) {
					if (isBlock(r,c))
						result |= (1<<0);
					if (chain[r][c] == -1)
						result |= (1<<1);
				}
				if (isNumber(r, c)) {
					int remainder = number[r][c].getSumSpace() - number[r][c].getNumber();
					if (remainder < 0)
						result |= (1<<2);
					else if (remainder > 0)
						result |= (1<<3);
				}
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
