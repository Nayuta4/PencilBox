package pencilbox.hashi;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;


/**
 * �u����������v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNDECIDED_NUMBER = 9;

	private Pier[][] pier;
	private Bridge[][] bridgeV;
	private Bridge[][] bridgeH;
	private int maxChain;
	private int nPier;
	private int nBridge;

	protected void setup() {
		super.setup();
		bridgeV = new Bridge[rows()][cols()];
		bridgeH = new Bridge[rows()][cols()];
		pier = new Pier[rows()][cols()];
		maxChain = 1;
		nPier = 0;
		nBridge = 0;
	}

	public void clearBoard() {
		super.clearBoard();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c))
					pier[r][c].clear();
			}
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
		if (n == 0) {
			if (isPier(r, c)) {
				removePier(r, c);
			}
		} else if (n > 0) {
			if (isPier(r, c))
				pier[r][c].setNumber(n);
			else
				addPier(r, c, n);
		}
	}
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �}�X�̐������擾����
	 * @param r �ݒ肷��}�X�̍s���W
	 * @param c �ݒ肷��}�X�̗���W
	 * @return �}�X�̐���
	 */
	public int getNumber(int r, int c) {
		if (pier[r][c] == null)
			return 0;
		else
			return pier[r][c].getNumber();
	}
	
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * ���̃}�X�������}�X�i���r�j���ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �����}�X�Ȃ� true
	 */
	public boolean isPier(int r, int c) {
		return pier[r][c] != null;
	}
	/**
	 * ���̃}�X�������}�X�i���r�j���ǂ���
	 * @param pos ���W
	 * @return �����}�X�Ȃ� true
	 */
	public boolean isPier(Address pos) {
		return isPier(pos.r(), pos.c());
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
			ret += bridgeV[r][c].getBridge();
		if (bridgeH[r][c] != null)
			ret += (bridgeH[r][c].getBridge() << 2);
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
			bridgeV[r][c].setBridge(n & 0x3);
		if (bridgeH[r][c] != null)
			bridgeH[r][c].setBridge((n>>2) & 0x3);
	}
	
	public void setState(Address pos, int n) {
		setState(pos.r(), pos.c(), n);
	}
	/**
	 * ���̃}�X��ʉ߂���c�����̋��̐���Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @return�@���̃}�X��ʉ߂���c�����̋��̐�
	 */
	public int getVertBridge(int r, int c) {
		if (bridgeV[r][c] == null)
			return -1;
		else
			return bridgeV[r][c].getBridge();
	}
	/**
	 * ���̃}�X��ʉ߂��鉡�����̋��̐���Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @return�@���̃}�X��ʉ߂��鉡�����̋��̐�
	 */
	public int getHorizBridge(int r, int c) {
		if (bridgeH[r][c] == null)
			return -1;
		else
			return bridgeH[r][c].getBridge();
	}
	/**
	 * ���̃}�X�̏�ŋ����������Ă��邩�����Ă��邩�ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return ���̃}�X�̏�ŋ����������Ă���� true
	 */
	public boolean hasCrossedBridge(int r, int c) {
		return getHorizBridge(r, c) > 0 && getVertBridge(r, c) > 0;
	}
	
	/**
	 * @param r �s���W
	 * @param c ����W
	 * @return Returns the pier.
	 */
	public Pier getPier(int r, int c) {
		return pier[r][c];
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
		Address pos = new Address(pos0);
		while(true) {
			pos.move(d);
			if (pos.equals(pos1)) break;
			setBridge(pos.r(), pos.c(), d&1, b);
		}
	}
	/**
	 * �w�肵�����W�ɋ��r��V�K�ɍ쐬����
	 * ���̃}�X�ɂ��Ƃ��Ƃ������Ă������͏��������
	 * @param r �s���W
	 * @param c ����W
	 * @param n ����
	 */
	void addPier(int r, int c, int n) {
		Pier p = new Pier(r, c, n);
		pier[r][c] = p;
		for (int d = 0; d < 4; d++) {
			Pier next = findPier(r, c, d);
			if (next != null) {
				next.setNextPier(d^2,p);
				p.setNextPier(d,next);
				Bridge b = new Bridge(p, next);
				next.setBridge(d^2, b);
				p.setBridge(d, b);
				setBridge(p.getPos(), next.getPos(), d, b);
				nBridge ++;
			}
		}
		nPier ++;
	}
	/**
	 * �w�肵�����W�ɂ��鋴�r����������
	 * ���̋�����o�Ă������͏��������
	 * @param r �s���W
	 * @param c ����W
	 */
	void removePier(int r, int c) {
		
		Pier p = pier[r][c];
		
		for (int d=0; d<4; d++) {
			Pier p1 = p.getNextPier(d);
			Pier p2 = p.getNextPier(d^2);
			if (p1 != null) {
				if (p2 != null)
					p1.setNextPier(d^2, p2);
				else 
					p1.setNextPier(d^2, null);
			}
		}
		for (int d=0; d<2; d++) {
			Pier p1 = p.getNextPier(d);
			Pier p2 = p.getNextPier(d^2);
			if (p1 != null) {
				if (p2 != null) {
					Bridge b = new Bridge(p1, p2);
					setBridge(p2.getPos(), p1.getPos(), d, b);
					p1.setBridge(d^2, b);
					p2.setBridge(d, b);
					nBridge --;
				} else {
					setBridge(p.getPos(), p1.getPos(), d, null);
					p1.setBridge(d^2, null);
					nBridge --;
				}
			} else {
				if (p2 != null) {
					setBridge(p.getPos(), p2.getPos(), d^2, null);
					p2.setBridge(d, null);
					nBridge --;
				} else {
				}
			}
		}

		pier[r][c] = null;
		nPier --;
	}

	/**
	 * @param nBridge The nBridge to set.
	 */
	void setNBridge(int nBridge) {
		this.nBridge = nBridge;
	}

	/**
	 * @return Returns the nBridge.
	 */
	int getNBridge() {
		return nBridge;
	}

	/**
	 * @param nPier The nPier to set.
	 */
	void setNPier(int nPier) {
		this.nPier = nPier;
	}

	/**
	 * @return Returns the nPier.
	 */
	int getNPier() {
		return nPier;
	}

	/**
	 * @return Returns the maxChain.
	 */
	int getMaxChain() {
		return maxChain;
	}

	/**
	 * �N�_����w�肵�������ɂ���ŏ��̋��r��Ԃ�
	 * @param r �N�_�̍s���W
	 * @param c �N�_�̗���W
	 * @param direction ���r��T������
	 * @return �N�_����w�肵�������ɂ���ŏ��̋��r
	 */
	Pier findPier(int r, int c, int direction) {
		Address pos = new Address(r, c);
		pos.move(direction);
		while (isOn(pos)) {
			if (isPier(pos)) {
				return pier[pos.r()][pos.c()];
			}
			pos.move(direction);
		}
		return null;
	}
	/**
	 * ����������
	 * @param r �N�_�̍s���W
	 * @param c �N�_�̗���W
	 * @param direction �����i�㉺���E�j
	 */
	public void addBridge(int r, int c, int direction) {
		if (!isPier(r, c))
			return;
		if (pier[r][c].getNextPier(direction) == null)
			return;
		if (pier[r][c].getNBridge(direction) == 2)
			return;
		pier[r][c].increaseBridge(direction);
		if (pier[r][c].getNBridge(direction) == 1)
			connectChain(pier[r][c], pier[r][c].getNextPier(direction));
	}

	/**
	 * ��������
	 * @param r �N�_�̍s���W
	 * @param c �N�_�̗���W
	 * @param direction �����i�㉺���E�j
	 */
	public void removeBridge(int r, int c, int direction) {
		if (!isPier(r, c))
			return;
		if (pier[r][c].getNextPier(direction) == null)
			return;
		if (pier[r][c].getNBridge(direction) == 0)
			return;
		pier[r][c].decreaseBridge(direction);
		if (pier[r][c].getNBridge(direction) == 0)
			cutChain(pier[r][c], pier[r][c].getNextPier(direction));
	}

	/**
	 * ����������C�A���h�D���X�i�[�ɒʒm����
	 * @param pos �N�_�̍��W
	 * @param direction �����i�㉺���E�j
	 */
	public void addBridgeA(Address pos, int direction) {
		addBridge(pos.r(), pos.c(), direction);
		fireUndoableEditUpdate(new UndoableEditEvent(this,
				new Step(pos.r(), pos.c(), direction, Step.ADDED)));
	}
	/**
	 * ���������C�A���h�D���X�i�[�ɒʒm����
	 * @param pos �N�_�̍��W
	 * @param direction �����i�㉺���E�j
	 */
	public void removeBridgeA(Address pos, int direction) {
		removeBridge(pos.r(), pos.c(), direction);
		fireUndoableEditUpdate(new UndoableEditEvent(this, 
				new Step(pos.r(), pos.c(), direction, Step.REMOVED)));
	}
	/**
	 * ���̘A���ԍ�������������
	 */
	void initChain() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					pier[r][c].setChain(0);
				}
			}
		}
		maxChain = 1;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					if (pier[r][c].totalBridges() == 0)
						pier[r][c].setChain(0);
					else if (pier[r][c].getChain() == 0) {
						initChain1(pier[r][c], maxChain++);
					}
				}
			}
		}
	}

	/**
	 * ����}�X���n�_�Ƃ��� chain�ԍ��̏�����
	 * @param p �n�_�ƂȂ鋴�r
	 * @param chain �ԍ�
	 */
	void initChain1(Pier p, int chain) {
		if (p.getChain() == chain)
			return;
		p.setChain(chain);
		for (int d = 0; d < 4; d++) {
			if (p.getNBridge(d) > 0)
				initChain1(p.getNextPier(d), chain);
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
				pierB.setChain(maxChain++);
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

		int a = pierA.totalBridges();
		int b = pierB.totalBridges();

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
	 * @param r �s���W
	 * @param c ����W
	 * @return >0: ���Ȃ�����, =0: ���傤��, <0 ��������
	 */
	public int checkPier(int r, int c) {
		int number = pier[r][c].getNumber();
		int bridges = pier[r][c].totalBridges();
		if (number == UNDECIDED_NUMBER)
			return 1;
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					if (pier[r][c].getNumber() == UNDECIDED_NUMBER)
						continue;
					if (pier[r][c].totalBridges() != pier[r][c].getNumber())
						return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkConnection() {
		int n = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r, c)) {
					int m = pier[r][c].getChain();
					if (m == 0)
						return false;
					else if (n == 0)
						n = m;
					else if (n != m)
						return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkCross() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (hasCrossedBridge(r, c))
					return false;
			}
		}
		return true;
	}
	
	static final String ERR_CROSS_BRIDGE = "�����������Ă���\n";
	static final String YET_MULTIPLE_LINE = "�S�̂��ЂƂȂ���ɂȂ��Ă��Ȃ�\n";
	static final String ERR_WRONG_NUMBER = "���̐��������ƈ�v���Ă��Ȃ�\n";
//	static final String ERR_TOO_MANY_LINE = "���̐����������鐔��������\n";
//	static final String YET_TOO_FEW_LINE= "���̐�������Ȃ�����������\n";
	
	int sumAllNumbers() {
		int ret = 0;
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isPier(r,c))
					ret += pier[r][c].getNumber(); 
			}
		}
		return ret/2;
	}

	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractUndoableEdit {

		static final int ADDED = 1;
		static final int REMOVED = -1;

		int row;
		int col;
		int direction;
		int change;

		/**
		 * �R���X�g���N�^
		 * @param r �ύX���ꂽ���̋N�_�}�X�̍s���W
		 * @param c �ύX���ꂽ���̋N�_�}�X�̗���W
		 * @param dir �}�X����݂��ύX���ꂽ���̕���
		 * @param ch �ǉ����ꂽ�̂��C�������ꂽ�̂�
		 */
		public Step(int r, int c, int dir, int ch) {
			super();
			row = r;
			col = c;
			direction = dir;
			change = ch;
		}
		
		public void undo() throws CannotUndoException {
			super.undo();
			if (change == ADDED) {
				removeBridge(row, col, direction);
			} else if (change == REMOVED) {
				addBridge(row, col, direction);
			}
		}
		
		public void redo() throws CannotRedoException {
			super.redo();
			if (change == ADDED) {
				addBridge(row, col, direction);
			} else if (change == REMOVED) {
				removeBridge(row, col, direction);
			}
		}
	}
}
