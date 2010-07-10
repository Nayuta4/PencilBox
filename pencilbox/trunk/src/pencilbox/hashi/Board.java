package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.resource.Messages;


/**
 * �u����������v�ՖʃN���X
 */
public class Board extends BoardBase {

	public static final int UNDECIDED_NUMBER = 9;
	public static final int NO_NUMBER = 0;

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
		if (n == NO_NUMBER) {
			if (isPier(p)) {
				removePier(p);
			}
		} else if (n >= 1 && n <= 8 || n == UNDECIDED_NUMBER) {
			if (isPier(p))
				getPier(p).setNumber(n);
			else
				addPier(p, n);
		}
	}
	/**
	 * ������ύX����
	 * @param p �}�X���W
	 * @param n �ύX��̐��� 0 �Ȃ琔��������
	 */
	public void changeNumber(Address p, int n) {
		int prev = getNumber(p);
		if (prev == n)
			return;
		setNumber(p, n);
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(p, prev, n));
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
		Address p = Address.address(r, c);
		return getLine(p, Direction.VERT) + (getLine(p, Direction.HORIZ)<<2);
	}
	/**
	 * �}�X�̏�Ԃ�ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param n �}�X�̏��
	 */
	public void setState(int r, int c, int n) {
		Address p = Address.address(r, c);
		setLine(p, Direction.VERT, (n&0x3));
		setLine(p, Direction.HORIZ, ((n>>2) & 0x3));
	}

	public void setState(Address pos, int n) {
		setState(pos.r(), pos.c(), n);
	}
	/**
	 * �}�X��ʉ߂��鋴�̐���Ԃ�
	 * @param p ���W
	 * @param d �����i�c�����j
	 * @return�@�}�X��ʉ߂��鋴�̐�
	 */
	public int getLine(Address p, int d) {
		if (getBridge(p, d) != null)
			return getBridge(p, d).getLine();
		return 0;
	}
	/**
	 * �}�X�̏�̋��̐���ݒ肷��
	 * @param p �}�X
	 * @param d �����i�c�������j
	 * @param n ���̖{��
	 */
	public void setLine(Address p, int d, int n) {
		if (getBridge(p, d) != null)
			getBridge(p, d).setLine(n);
	}

	/**
	 * ���̃}�X�̏�ŋ����������Ă��邩�����Ă��邩�ǂ���
	 * @param p ���W
	 * @return ���̃}�X�̏�ŋ����������Ă���� true
	 */
	public boolean hasCrossedBridge(Address p) {
		int v = getLine(p, Direction.VERT);
		int h = getLine(p, Direction.HORIZ);
		return (v>0 && h>0);
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
	public Bridge getBridge(Address p, int dir) {
		if (dir == Direction.HORIZ) {
			return bridgeH[p.r()][p.c()];
		} else if (dir == Direction.VERT) {
			return bridgeV[p.r()][p.c()];
		}
		return null;
	}
	/**
	 * ���̒ʂ�Տ�̊e�}�X�ɁC���̋���ݒ肷��
	 * @param pos0 �n�_�}�X
	 * @param d �n�_����I�_����������
	 * @param b Bridge
	 */
	void setBridge(Address pos0, int d, Bridge b) {
		Address pos = pos0;
		int dir = d&1;
		while(true) {
			pos = pos.nextCell(d);
			if (isPier(pos))
				break;
			if (dir == Direction.HORIZ) {
				bridgeH[pos.r()][pos.c()] = b;
			} else if (dir == Direction.VERT) {
				bridgeV[pos.r()][pos.c()] = b;
			}
		}
	}

	/**
	 * ����}�X����w�肵�������֏o�Ă��鋴�̐���Ԃ��B�����}�X�ȊO���w�肵���Ƃ��� -1 ��Ԃ�
	 * @param p �����}�X�̍��W
	 * @param d ����
	 * @return �����}�X���炠������ւ̋��̖{���B�����}�X�ȊO�ł����-1
	 */
	public int getLineFromPier(Address p, int d) {
		if (isPier(p)) {
			return getPier(p).getLine(d);
		} else {
			return -1;
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
			Address nextPos = findPier(p, d);
			if (nextPos == Address.nowhere())
				continue;
			Pier next = getPier(nextPos);
			if (next.getLine(d^2) > 0) {
				changeLine(nextPos, d^2, 0);
			}
		}
		for (int d = 0; d < 4; d++) {
			Address nextPos = findPier(p, d);
			if (nextPos == Address.nowhere())
				continue;
			Pier next = getPier(nextPos);
			next.setNextPier(d^2,pi);
			pi.setNextPier(d,next);
			Bridge b = new Bridge(pi, next);
			next.setBridge(d^2, b);
			pi.setBridge(d, b);
			setBridge(p, d, b);
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
			if (getLineFromPier(p, d) > 0) {
				changeLine(p, d, 0);
			}
		}
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
					setBridge(p2.getPos(), d, b);
					p1.setBridge(d^2, b);
					p2.setBridge(d, b);
				} else {
					setBridge(p, d, null);
					p1.setBridge(d^2, null);
				}
			} else {
				if (p2 != null) {
					setBridge(p, d^2, null);
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
	 * �N�_����w�肵�������ɂ���ŏ��̋��̃}�X�̍��W��Ԃ�
	 * @param p0 �N�_�̍��W
	 * @param direction ���r��T������
	 * @return �N�_����w�肵�������ɂ���ŏ��̋��r
	 */
	Address findPier(Address p0, int direction) {
		Address p = Address.nextCell(p0, direction);
		while (isOn(p)) {
			if (isPier(p)) {
				return p;
			}
			p = p.nextCell(direction);
		}
		return Address.nowhere();
	}
	/**
	 * ����������C��������
	 * @param p �N�_�̍��W
	 * @param d �����i�㉺���E�j
	 * @param n �ύX��̋��̐�
	 */
	public void changeLine(Address p, int d, int n) {
		if (!isPier(p))
			return;
		if (n < 0 || n > 2)
			return;
		Pier pi = getPier(p);
		Pier nextPier = pi.getNextPier(d);
		if (nextPier == null)
			return;
		int prev = pi.getLine(d);
		if (prev == n)
			return;
		pi.setLine(d, n);
		if (isRecordUndo())
			fireUndoableEditUpdate(new BridgeEditStep(p, d, prev, n));
		if (n == 0)
			cutChain(pi, nextPier);
		if (prev == 0)
			connectChain(pi, nextPier);
	}

	public void undo(AbstractStep step) {
		if (step instanceof BridgeEditStep) {
			BridgeEditStep s = (BridgeEditStep)step;
			changeLine(s.getPos(), s.getDirection(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep)step;
			changeNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BridgeEditStep) {
			BridgeEditStep s = (BridgeEditStep)step;
			changeLine(s.getPos(), s.getDirection(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep)step;
			changeNumber(s.getPos(), s.getAfter());
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
