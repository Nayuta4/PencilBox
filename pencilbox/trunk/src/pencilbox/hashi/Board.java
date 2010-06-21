package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;


/**
 * �u����������v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNDECIDED_NUMBER = 9;

	private Pier[][] pier;
	private Bridge[][] bridgeV;
	private Bridge[][] bridgeH;
	private int maxChain;

	protected void setup() {
		super.setup();
		bridgeV = new Bridge[rows()][cols()];
		bridgeH = new Bridge[rows()][cols()];
		pier = new Pier[rows()][cols()];
		maxChain = 1;
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (isPier(p))
				getPier(p).clear();
		}
	}

	public void initBoard() {
		initChain();
	}
	/**
	 * �}�X�ɐ�����ݒ肷��
	 * @param r �ݒ肷��}�X�̍s���W
	 * @param c �ݒ肷��}�X�̗���W
	 * @param n �ݒ肷�鐔��
	 */
	public void setNumber(int r, int c, int n) {
		setNumber(Address.address(r, c), n);
	}
	
	public void setNumber(Address p, int n) {
		if (n == 0) {
			if (isPier(p)) {
				removePier(p);
			}
		} else if (n > 0) {
			if (isPier(p))
				getPier(p).setNumber(n);
			else
				addPier(p, n);
		}
	}
	/**
	 * �}�X�̐������擾����
	 * @param r �ݒ肷��}�X�̍s���W
	 * @param c �ݒ肷��}�X�̗���W
	 * @return �}�X�̐���
	 */
	public int getNumber(int r, int c) {
		return getNumber(Address.address(r, c));
	}
	
	public int getNumber(Address p) {
		if (isPier(p))
			return getPier(p).getNumber();
		else
			return 0;
	}
	/**
	 * ���̃}�X�������}�X�i���r�j���ǂ���
	 * @param p ���W
	 * @return �����}�X�Ȃ� true
	 */
	public boolean isPier(Address p) {
		return pier[p.r()][p.c()] != null;
	}
	
	/**
	 * �}�X�̏�Ԃ��擾����
	 * @param r �s���W
	 * @param c ����W
	 * @return �}�X�̏��
	 */
	public int getState(int r, int c) {
		int ret = 0;
		if (bridgeV[r][c] != null)
			ret += bridgeV[r][c].getLine();
		if (bridgeH[r][c] != null)
			ret += (bridgeH[r][c].getLine() << 2);
		return ret;
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * �}�X�̏�Ԃ�ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param n �}�X�̏��
	 */
	public void setState(int r, int c, int n) {
		if (bridgeV[r][c] != null)
			bridgeV[r][c].setLine(n & 0x3);
		if (bridgeH[r][c] != null)
			bridgeH[r][c].setLine((n>>2) & 0x3);
	}
	
	public void setState(Address pos, int n) {
		setState(pos.r(), pos.c(), n);
	}
	/**
	 * ���̃}�X��ʉ߂���c�����̋��̐���Ԃ�
	 * @param p ���W
	 * @return�@���̃}�X��ʉ߂���c�����̋��̐�
	 */
	public int getVertBridge(Address p) {
		Bridge b = bridgeV[p.r()][p.c()];
		if (b == null)
			return -1;
		else
			return b.getLine();
	}
	/**
	 * ���̃}�X��ʉ߂��鉡�����̋��̐���Ԃ�
	 * @param p ���W
	 * @return�@���̃}�X��ʉ߂��鉡�����̋��̐�
	 */
	public int getHorizBridge(Address p) {
		Bridge b = bridgeH[p.r()][p.c()];
		if (b == null)
			return -1;
		else
			return b.getLine();
	}
	/**
	 * ���̃}�X�̏�ŋ����������Ă��邩�����Ă��邩�ǂ���
	 * @param p ���W
	 * @return ���̃}�X�̏�ŋ����������Ă���� true
	 */
	public boolean hasCrossedBridge(Address p) {
		return getHorizBridge(p) > 0 && getVertBridge(p) > 0;
	}
	
	/**
	 * @param r �s���W
	 * @param c ����W
	 * @return Returns the pier.
	 */
	public Pier getPier(int r, int c) {
		return pier[r][c];
	}
	public Pier getPier(Address p) {
		return pier[p.r()][p.c()];
	}

	public Pier setPier(Address p, Pier pi) {
		return pier[p.r()][p.c()] = pi;
	}
	/**
	 * ���̃}�X��̋����擾����
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c�̋������̋���
	 * @return�@���̃}�X�̏�̋�
	 */
	public Bridge getBridge(int r, int c, int dir) {
		if (dir == Direction.HORIZ) {
			return bridgeH[r][c];
		} else if (dir == Direction.VERT) {
			return bridgeV[r][c];
		}
		return null;
	}
	/**
	 * �}�X��ɂ��̃}�X��ʂ鋴��ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param dir �c�̋������̋���
	 * @param b �ݒ肷��Bridge
	 */
	public void setBridge(int r, int c, int dir, Bridge b) {
		if (dir == Direction.HORIZ) {
			bridgeH[r][c] = b;
		} else if (dir == Direction.VERT) {
			bridgeV[r][c] = b;
		}
	}
	/**
	 * ���̒ʂ�Տ�̊e�}�X�ɁC���̋���ݒ肷��
	 * @param pos0 �n�_�}�X
	 * @param pos1 �I�_�}�X
	 * @param d �n�_����I�_����������
	 * @param b Bridge
	 */
	void setBridge(Address pos0, Address pos1, int d, Bridge b) {
		Address pos = Address.address(pos0);
		while(true) {
			pos = pos.nextCell(d);
			if (pos.equals(pos1)) break;
			setBridge(pos.r(), pos.c(), d&1, b);
		}
	}

	/**
	 * ����}�X����w�肵�������֏o�Ă��鋴�̐���Ԃ��B�����}�X�ȊO���w�肵���Ƃ��� -1 ��Ԃ�
	 * @param p �����}�X�̍��W
	 * @param d ����
	 * @return �����}�X���炠������ւ̋��̖{���B�����}�X�ȊO�ł����-1
	 */
	public int getLine(Address p, int d) {
		if (!isPier(p)) {
			return -1;
		} else {
			return getPier(p).getLine(d);
		}
	}

	/**
	 * �w�肵�����W�ɋ��r��V�K�ɍ쐬����
	 * ���̃}�X�ɂ��Ƃ��Ƃ������Ă������͏��������
	 * @param p ���W
	 * @param n ����
	 */
	void addPier(Address p, int n) {
		Pier pi = new Pier(p, n);
		setPier(p, pi);
		for (int d = 0; d < 4; d++) {
			Pier next = findPier(p, d);
			if (next != null) {
				next.setNextPier(d^2,pi);
				pi.setNextPier(d,next);
				Bridge b = new Bridge(pi, next);
				next.setBridge(d^2, b);
				pi.setBridge(d, b);
				setBridge(pi.getPos(), next.getPos(), d, b);
			}
		}
	}
	/**
	 * �w�肵�����W�ɂ��鋴�r����������
	 * ���̋�����o�Ă������͏��������
	 * @param p ���W
	 */
	void removePier(Address p) {
		Pier pi = getPier(p);
		for (int d=0; d<4; d++) {
			Pier p1 = pi.getNextPier(d);
			Pier p2 = pi.getNextPier(d^2);
			if (p1 != null) {
				if (p2 != null)
					p1.setNextPier(d^2, p2);
				else 
					p1.setNextPier(d^2, null);
			}
		}
		for (int d=0; d<2; d++) {
			Pier p1 = pi.getNextPier(d);
			Pier p2 = pi.getNextPier(d^2);
			if (p1 != null) {
				if (p2 != null) {
					Bridge b = new Bridge(p1, p2);
					setBridge(p2.getPos(), p1.getPos(), d, b);
					p1.setBridge(d^2, b);
					p2.setBridge(d, b);
				} else {
					setBridge(pi.getPos(), p1.getPos(), d, null);
					p1.setBridge(d^2, null);
				}
			} else {
				if (p2 != null) {
					setBridge(pi.getPos(), p2.getPos(), d^2, null);
					p2.setBridge(d, null);
				} else {
				}
			}
		}
		setPier(p, null);
	}

	/**
	 * @return Returns the maxChain.
	 */
	int getMaxChain() {
		return maxChain;
	}

	/**
	 * �N�_����w�肵�������ɂ���ŏ��̋��r��Ԃ�
	 * @param p �N�_�̍��W
	 * @param direction ���r��T������
	 * @return �N�_����w�肵�������ɂ���ŏ��̋��r
	 */
	Pier findPier(Address p, int direction) {
		Address pos = Address.nextCell(p, direction);
		while (isOn(pos)) {
			if (isPier(pos)) {
				return getPier(pos);
			}
			pos = pos.nextCell(direction);
		}
		return null;
	}
	/**
	 * ����������^�����C�A���h�D���X�i�[�ɒʒm����
	 * @param p �N�_�̍��W
	 * @param d �����i�㉺���E�j
	 * @param n �ύX��̋��̐�
	 */
	public void changeLine(Address p, int d, int n) {
		if (!isPier(p))
			return;
		Pier pi= getPier(p);
		if (pi.getNextPier(d) == null)
			return;
		int prev = pi.getLine(d);
		if (n < 0 || n > 2)
			return;
		if (prev == n)
			return;
		pi.changeLine(d, n);
		if (n == 0)
			cutChain(pi, pi.getNextPier(d));
		if (prev == 0)
			connectChain(pi, pi.getNextPier(d));
		if (isRecordUndo())
			fireUndoableEditUpdate(new BridgeEditStep(p, d, prev, n));
	}

	public void undo(AbstractStep step) {
		if (step instanceof BridgeEditStep) {
			BridgeEditStep s = (BridgeEditStep) step;
			changeLine(s.getPos(), s.getDirection(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BridgeEditStep) {
			BridgeEditStep s = (BridgeEditStep) step;
			changeLine(s.getPos(), s.getDirection(), s.getAfter());
		}
	}

	/**
	 * ���̘A���ԍ�������������
	 */
	void initChain() {
		for (Address p : cellAddrs()) {
			if (isPier(p)) {
				getPier(p).setChain(0);
			}
		}
		maxChain = 1;
		for (Address p : cellAddrs()) {
			if (isPier(p)) {
				Pier pi = getPier(p);
				if (pi.totalLines() == 0)
					pi.setChain(0);
				else if (pi.getChain() == 0) {
					initChain1(pi, maxChain++);
				}
			}
		}
	}

	/**
	 * ����}�X���n�_�Ƃ��� chain�ԍ��̏�����
	 * @param pi �n�_�ƂȂ鋴�r
	 * @param chain �ԍ�
	 */
	void initChain1(Pier pi, int chain) {
		if (pi.getChain() == chain)
			return;
		pi.setChain(chain);
		for (int d = 0; d < 4; d++) {
			if (pi.getLine(d) > 0)
				initChain1(pi.getNextPier(d), chain);
		}
	}
	/**
	 * chain ����
	 * �h���b�O���ɍ��킹��
	 * @param pierA
	 * @param pierB
	 */
	void connectChain(Pier pierA, Pier pierB) {
		int a = pierA.getChain();
		int b = pierB.getChain();
		if (a == 0) {
			if (b == 0) {
				pierA.setChain(maxChain);
				pierB.setChain(maxChain);
				maxChain++;
			} else if (b > 0) {
				pierA.setChain(b);
			}
		} else if (a > 0) {
			if (b == 0) {
				pierB.setChain(a);
			} else if (b > 0) {
				initChain1(pierB, a);
			}
		}
	}

	/**
	 * chain �ؒf
	 * �h���b�O���ԍ������̔ԍ��Ɏc��
	 * @param pierA
	 * @param pierB
	 */
	void cutChain(Pier pierA, Pier pierB) {
		int a = pierA.totalLines();
		int b = pierB.totalLines();
		if (a == 0) {
			pierA.setChain(0);
			if (b == 0) {
				pierB.setChain(0);
			} else if (b > 0) {
			}
		} else if (a > 0) {
			if (b == 0) {
				pierB.setChain(0);
			} else if (b > 0) {
				initChain1(pierB, maxChain++);
			}
		}
	}

	/**
	 * ���̃}�X����o�鋴�̐��������ɒB���Ă��邩�ǂ������ׂ�
	 * @param p ���W
	 * @return >0: ���Ȃ�����, =0: ���傤��, <0 ��������
	 */
	public int checkNumber(Address p) {
		Pier pi = getPier(p);
		int number = pi.getNumber();
		int bridges = pi.totalLines();
		if (number == UNDECIDED_NUMBER)
			return 0;
		return number - bridges;
	}

	public int checkAnswerCode() {
		int result = 0;
		if (checkCross() == false)
			result |= 1;
		if (checkConnection() == false)
			result |= 2;
		if (checkNumbers() == false)
			result |= 4;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(ERR_CROSS_BRIDGE);
		if ((result & 2) == 2)
			message.append(YET_MULTIPLE_LINE);
		if ((result & 4) == 4)
			message.append(ERR_WRONG_NUMBER);
		return message.toString();
	}

	private boolean checkNumbers() {
		for (Address p : cellAddrs()) {
			if (isPier(p)) {
				if (checkNumber(p) != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkConnection() {
		int n = 0;
		for (Address p : cellAddrs()) {
			if (isPier(p)) {
				int m = getPier(p).getChain();
				if (m == 0)
					return false;
				else if (n == 0)
					n = m;
				else if (n != m)
					return false;
			}
		}
		return true;
	}
	
	private boolean checkCross() {
		for (Address p : cellAddrs()) {
			if (hasCrossedBridge(p)) {
				return false;
			}
		}
		return true;
	}
	
	static final String ERR_CROSS_BRIDGE = Messages.getString("hashi.AnswerCheckMessage1"); //$NON-NLS-1$
	static final String YET_MULTIPLE_LINE = Messages.getString("hashi.AnswerCheckMessage2"); //$NON-NLS-1$
	static final String ERR_WRONG_NUMBER = Messages.getString("hashi.AnswerCheckMessage3"); //$NON-NLS-1$
//	static final String ERR_TOO_MANY_LINE = "���̐����������鐔��������\n";
//	static final String YET_TOO_FEW_LINE= "���̐�������Ȃ�����������\n";
	
}
