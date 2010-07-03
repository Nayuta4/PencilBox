package pencilbox.slalom;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;

/**
 * �u�X�����[���v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNKNOWN = 0;
	static final int LINE = 1;
	static final int NOLINE = -1;
	static final int BLANK = -3;
	static final int GOAL = -1;
	static final int OUTER = -9;
	static final int UNDECIDED_NUMBER = 0;
	static final int GATE_VERT = -5;
	static final int GATE_HORIZ = -4;

	private int[][] number;  // �}�X�̏��
	private int[][][] state; // �ӂ̏��
	private int[][] gateNumber;  // ��̔ԍ�
	private int nGate;  // ����̐�
	private Address goal;  // �X�^�[�g�^�S�[���n�_�̍��W�B�P�����݂̂Ƃ���

	private List<Link> linkList;
	private Link[][][] link;
	private Link initializingLink;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		gateNumber = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(number, BLANK);
		goal = Address.nowhere();
		state = new int[2][][];
		state[Direction.VERT] = new int[rows()][cols()-1];
		state[Direction.HORIZ] = new int[rows()-1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[Direction.VERT] = new Link[rows()][cols()-1];
		link[Direction.HORIZ] = new Link[rows()-1][cols()];
	}

	/**
	 * �w�肵���}�X�ɐ����}�X�C���}�X�C���}�X�C�󔒃}�X�̏�Ԃ�ݒ肷��
	 * �S�[���͂P�����ȉ��Ƃ���B
	 * @param r �s���W
	 * @param c ����W
	 * @param n �ݒ肷����
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �w�肵���}�X�̏�Ԃ��擾����
	 * @param r �s���W
	 * @param c ����W
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}

	/**
	 * ���}�X��
	 * @param p ���W
	 * @return ���}�X�ł���� true
	 */
	public boolean isWall(Address p) {
		return getNumber(p) >= 0 || getNumber(p) == UNDECIDED_NUMBER;
	}
	/**
	 * ����}�X��
	 * @param r �s���W
	 * @param c ����W
	 * @return �����}�X���C���萔���}�X�ł���� true
	 */
	public boolean isGate(int r, int c) {
		return number[r][c] == GATE_HORIZ || number[r][c] == GATE_VERT;
	}

	public boolean isGate(Address pos) {
		return isGate(pos.r(), pos.c());
	}
	/**
	 * �����W�̗�����2�}�X�����ꂩ�����}�X���ǂ���
	 * @param p �����W
	 * @return �����W�̗�����2�}�X�����ꂩ�����}�X�ł���� true
	 */
	public boolean hasWall(SideAddress p) {
		return isWall(SideAddress.nextCellFromBorder(p, 0))
				|| isWall(SideAddress.nextCellFromBorder(p, 1));
	}
	/**
	 * @param p
	 * @return
	 */
	public int getGateNumber(Address p) {
		return gateNumber[p.r()][p.c()];
	}

	public int getGateNumber(int r, int c) {
		return gateNumber[r][c];
	}

	/**
	 * @param p
	 * @return
	 */
	public int setGateNumber(Address p, int n) {
		return gateNumber[p.r()][p.c()] = n;
	}

	public int setGateNumber(int r, int c, int n) {
		return gateNumber[r][c] = n;
	}
	/**
	 * @return the nGate
	 */
	public int getNGate() {
		return nGate;
	}

	/**
	 * @return the Goal
	 */
	public Address getGoal() {
		return goal;
	}

	/**
	 * �ӏ�Ԃ̎擾
	 * @param d
	 * @param r
	 * @param c
	 * @return �ӂ̏�Ԃ�Ԃ�
	 */
	public int getState(int d, int r, int c) {
		if (isSideOn(d, r, c))
			return state[d][r][c];
		else
			return OUTER;
	}

	public int getState(SideAddress pos) {
		return getState(pos.d(), pos.r(), pos.c());
	}

	/**
	 * �ӏ�Ԃ̐ݒ�
	 * @param d
	 * @param r
	 * @param c
	 * @param st
	 */
	public void setState(int d, int r, int c, int st) {
		if (isSideOn(d, r, c))
			state[d][r][c] = st;
	}

	public void setState(SideAddress pos, int st) {
		setState(pos.d(), pos.r(), pos.c(), st);
	}

	public Link getLink(SideAddress pos) {
		if (isSideOn(pos))
			return link[pos.d()][pos.r()][pos.c()];
		else
			return null;
	}
	/**
	 * ���̃}�X���܂� Link ��Ԃ�
	 */
	public Link getLink(Address p) {
		for (int d = 0; d < 4; d++) {
			Link link = getLink(SideAddress.get(p, d));
			if (link != null)
				return link;
		}
		return null;
	}

	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] = l;
	}

	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param p �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeNumber(Address p, int st) {
		int prev = getNumber(p);
		if (prev == st)
			return;
		if (prev == Board.GOAL) {
			goal = Address.nowhere();
		}
		if (st == Board.GOAL) {
			if (!goal.isNowhere()) {
				changeNumber(goal, Board.BLANK);
			}
			goal = p;
		}
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		}
		setNumber(p, st);
	}

	/**
	 * �}�X����㉺���E4�����Ɉ�����Ă��������������
	 * �}�X�����}�X�␔���}�X�ɕύX���ꂽ�ꍇ�ɐ����������邽�߂Ɏg�p����
	 * @param pos �}�X�̍��W
	 */
	void eraseLinesAround(Address pos) {
		for (int d = 0; d <= 3; d++) {
			SideAddress side = SideAddress.get(pos, d);
			if (getState(side) == LINE || getState(side) == NOLINE) {
				changeState(side, UNKNOWN);
			}
		}
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param p �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeState(SideAddress p, int st) {
		int prev = getState(p);
		if (prev == st)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		}
		setState(p, st);
		if (prev == LINE) {
			cutLink(p);
		}
		if (st == LINE) {
			connectLink(p);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getBefore());
		}
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeState(s.getPos(), s.getAfter());
		}
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getAfter());
		}
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getState(p) == NOLINE)
				setState(p, UNKNOWN);
		}
	}

	public void initBoard() {
		initGates();
		initLinks();
	}

	/**
	 * ���������B �ՖʑS�̂̊���Ƃ��̔ԍ���ݒ肷��B
	 */
	void initGates() {
		nGate = 0;
		for (Address p : cellAddrs()) {
			setGateNumber(p, 0);
		}
		for (Address p : cellAddrs()) {
			int n = getNumber(p);
			if (n == GOAL) {
				goal = p;
			} else if (n == GATE_HORIZ) {
				Address p1 = Address.nextCell(p, Direction.LT);
				if (isOn(p1) && getNumber(p1) == GATE_HORIZ) {
				} else {
					nGate ++;
				}
			} else if (n == GATE_VERT) {
				Address p1 = Address.nextCell(p, Direction.UP);
				if (isOn(p1) && getNumber(p1) == GATE_VERT) {
				} else {
					nGate ++;
				}
			}
		}
		for (Address p : cellAddrs()) {
			int n = getNumber(p);
			if (n > 0) {
				initGateNumber(p, n);
			}
		}
	}

	/**
	 * ���}�X�����̔��Α��̍��}�X��T��
	 * @param p0 �N�_�}�X
	 * @param d �������
	 * @return�@��̔��Α��ɍ��}�X�܂��͊O���Ȃ�΂��̍��W�C����ȊO��null
	 */
	Address getAnotherPole(Address p0, int d) {
		Address p = p0;
		int gateType = 0;
		if (d == Direction.UP || d == Direction.DN) {
			gateType = GATE_VERT;
		} else if (d == Direction.LT || d == Direction.RT) {
			gateType = GATE_HORIZ;
		}
		while (true) {
			p = Address.nextCell(p, d);
			if (isOn(p)) {
				if (isWall(p)) {
//					System.out.println(p.toString() + "���[�͍��}�X��");
					return p;
				} else if (getNumber(p) == gateType) {
				} else {
//					System.out.println(p.toString() + "���[�͕��Ă��Ȃ�");
					return null;
				}
			} else {
//				System.out.println(p.toString() + "���[�͊O����");
				return p;
			}
		}
	}

	/**
	 * ���}�X���N�_�ɁC��������ɏo�Ă����ɔԍ���ݒ肷��B
	 * @param p0
	 * @param d
	 * @param n
	 */
	private void setGateNumber(Address p0, int d, int n) {
		int t = 0;
		if (d == 0 || d == 2)
			t = GATE_VERT;
		else
			t = GATE_HORIZ;
		Address p = p0;
		while (true) {
			p = p.nextCell(d);
			if (isOn(p) && getNumber(p) == t) {
				setGateNumber(p, n);
			} else {
				break;
			}
		}
	}
	/**
	 * ����̔ԍ��𒲂ׂ�B
	 * ��̑����猩��̂͌��߂ɂ����̂ŁA���}�X�����猩��B
	 * �����t���̍��}�X����㉺���E�ɗאڂ������݂�B
	 * ��̔��Α��ɓ��������̍��}�X������΁C���}�X�̐�������̔ԍ��B
	 * ��̔��Α��ɈقȂ鐔���̍��}�X������΁C��̔ԍ��� -1�B
	 * ���}�X�ɗאڂ���傪�P�����ŁC��̔ԍ����܂����܂��Ă��Ȃ���΁C���}�X�̐�������̔ԍ��B
	 * @param p0s
	 * @param n0 ���̐��Ƃ���
	 */
	private void initGateNumber(Address p0, int n0) {
		Address p = p0;
		Address p1 = null;
//		System.out.println(p.toString() + "�̍��}�X�ɂ��Ē��ׂ�B");
		int d1 = -1; // �Ί݂̍��}�X�̌������L�^����
		int ng = 0;  // ���肵���ԍ����L�^����
		int count = 0; // �}�X�̂S�����̖�̐��𐔂���
		for (int d = 0; d <= 3; d++) {
			int t = 0;
			if (d == 0 || d == 2)
				t = GATE_VERT;
			else
				t = GATE_HORIZ;
			p = p0;
			p = p.nextCell(d);
			if (isOn(p) && getNumber(p) == t) {
//				System.out.println(d + "�̌����ɖ傪������");
				d1 = d;
				count++;
				p1 = getAnotherPole(p, d);
				if (p1 != null && isOn(p1)) {
					int n1 = getNumber(p1);
					if (n1 == n0) { // �Ί݂̖�Ɠ����ԍ�
						ng = n1;
						setGateNumber(p0, d, n1);
//						System.out.println("�Ί݂̐����}�X" + p1.toString() + "�Ƃ̊Ԃ̖�̔ԍ��� " + n1);
					} else if (n1 > 0 && n1 != n0) {
						ng = -1;
						setGateNumber(p0, d, -1);
//						System.out.println("�Ί݂̐����}�X���قȂ�ԍ��ł��邽�߁C��̔ԍ��� -1 �Ƃ���B");
					}
				} else {
//					System.out.println("�Ί݂ɐ����}�X���Ȃ�");
				}
			}
		}
		if (count == 0) {
//			System.out.println("�㉺���E�S�����ɖ傪�Ȃ� ");
		} else if (count == 1) {
			if (ng == 0) {
				setGateNumber(p0, d1, n0);
//				System.out.println("�אڂ���傪�P�����Ȃ��̂ŁC���̖�̔ԍ��� " + n0 + "�Ɍ��߂�");
			} else if (ng == -1) {
//				System.out.println("�אڂ���傪�P�����Ȃ��ꍇ�ł��A�قȂ鐔���ɂ͂��܂ꂽ��̔ԍ���-1�Ƃ���B ");
			} else if (ng > 0) {
//				System.out.println("�אڂ���傪�P�����Ȃ����C����ς݁B");
			}
		} else if (count > 1) {
//			System.out.println("�����̖�ɗאڂ���̂Ŗ�̔ԍ������܂�Ȃ�");
		}
	}

	void initLinks() {
		Link.resetId();
		linkList.clear();
		ArrayUtil.initArrayObject2(link[0], null);
		ArrayUtil.initArrayObject2(link[1], null);
		for (Address p : cellAddrs()) {
			initLink(p);
		}
	}

	/**
	 * ����}�X���܂� Link �̏�����
	 * link[][][] �͏�������Ă�����̂Ƃ���
	 * @param p Link�������̋N�_�}�X�̍��W
	 */
	void initLink(Address p) {
		initializingLink = new Link();
		for (int d = 0; d < 4; d++) {
			initLink1(SideAddress.get(p, d));
		}
		if (!initializingLink.isEmpty()) {
			linkList.add(initializingLink);
		}
	}

	private void initLink1(SideAddress p) {
		if (!isSideOn(p))
			return;
		if (getState(p) != LINE)
			return;
		if (getLink(p) != null)
			return;
		initializingLink.add(p);
		setLink(p, initializingLink);
		for (int d = 0; d < 6; d++) {
			initLink1(SideAddress.nextBorder(p, d));
		}
	}
	/**
	 * Link ����
	 */	
	void connectLink(SideAddress p) {
		Link newLink = new Link();
		for (int d = 0; d < 2; d++) {
			Link link = getLink(SideAddress.nextCellFromBorder(p, d));
			if (link != null && (link.size() > newLink.size()))
				newLink = link;
		}
		if (newLink.isEmpty()) {
			linkList.add(newLink);
		}
		for (int d = 0; d < 2; d++) {
			Link link = getLink(SideAddress.nextCellFromBorder(p, d));
			if (link != null && link != newLink) {
				for(SideAddress b : link) {
					setLink(b, newLink);
					newLink.add(b);
				}
				linkList.remove(link);
			}
		}
		newLink.add(p);
		setLink(p, newLink);
	}
	/**
	 * Link �ؒf
	 */
	void cutLink(SideAddress p) {
		Link oldLink = getLink(p);
		Link longerLink = new Link();
		for (SideAddress b : oldLink) {
			setLink(b, null);
		}
		linkList.remove(oldLink);
		for (int d = 0; d < 2; d++) {
			Address p1 = SideAddress.nextCellFromBorder(p, d);
			initLink(p1);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		}
		longerLink.setId(oldLink.getId());
	}

	/**
	 * �}�X�̏㉺���E4�����̂����C���ݐ���������Ă��鐔��Ԃ�
	 * @param p �}�X�̍��W
	 * @return �}�X�̏㉺���E�Ɉ�����Ă�����̐�
	 */
	public int countLine(Address p) {
		int no = 0;
		for (int d = 0; d < 4; d++) {
			SideAddress b = SideAddress.get(p, d);
			if (getState(b) == Board.LINE) {
				no++;
			}
		}
		return no;
	}

	/**
	 * ���Ɋւ�������`�F�b�N����
	 * @return
	 */
	private int checkLinks() {
		int result = 0;
		for (Address p : cellAddrs()) {
			int l = countLine(p);
			if (l > 2) {
				result |= 1;
			} else if (l == 1) {
				result |= 2;
			}
		}
		if (linkList.size() > 1)
			result |= 4;
		else if (linkList.size() == 0)
			result |= 8;
		return result;
	}

	/**
	 * ����ɂ��āA�������ʉ߂��Ă��邩�ǂ����𒲂ׂ�B 
	 * ��ƂP�ӏ��݂̂Œ�������ꍇ���������B
	 * �������Ȃ��ꍇ�͌��B
	 * �����񒼌�����ꍇ�͌��B
	 * @return ��肪����ΐ����C�Ȃ����0
	 */
	private int checkGates() {
		for (Address p : cellAddrs()) {
			if (isGate(p)) {
				int ret = checkGate1(p);
				if (ret == -1) {
					return 16;
				} else if (ret == 0) {
					return 32;
				} else if (ret > 1) {
					return 64;
				} else if (ret == -2) {
				}
			}
		}
		return 0;
	}

	/**
	 * ����̃}�X�ɒ��ڂ��āA�ʂ�������������𒲂ׂ�B
	 * �c��̏�[�܂��͉���̍��[�̃}�X�ɂ��Ē��ׂ�B
	 * ��[�܂��͍��[�ȊO�̃}�X�̏ꍇ�͂��łɒ����ς݂̂͂��B
	 * @param p ����̃}�X�̍��W
	 * @return ��̒ʂ��������Ă���� -1 , �����ς݂� -2, 0�ȏ�̐��͐���Ɍ��������񐔂�Ԃ��B
	 */
	private int checkGate1(Address p0) {
		int gateType = getNumber(p0);
//		System.out.println(p.toString() + "�̖�𒲂ׂ�B");
		int d = 0;
		if (gateType == Board.GATE_HORIZ) {
			d = Direction.RT;
		} else if (gateType == Board.GATE_VERT) {
			d = Direction.DN;
		}
		Address p2 = Address.nextCell(p0, d^2);
		if (isOn(p2) && getNumber(p2) == gateType) { // �ЂƂ�O�̃}�X�����������̖�Ȃ�΁C
			return -2; // �����ς݂̂͂�
		}
		int count = 0;
		Address p = p0;
		while (true) {
			int ret = checkGate2(p);
			if (ret == -1) {
//				System.out.println(p.toString() + "�̈ʒu�̖�̒ʂ�������������B");
				return -1;
			} else if (ret == 1) {
				count ++;
			}
			p = p.nextCell(d);
			if (isOn(p) && getNumber(p) == gateType) {
			} else {
				break;
			}
		}
//		System.out.println(count + "��C������������B");
		return count;
	}

	/**
	 * ��̃}�X�ɒ��ڂ��āA�ʂ�������������𒲂ׂ�B
	 * ��ƕ��s�ɐ���������Ă���Ό��C�������Ă���ΐ������B
	 * @param p�@��̃}�X�̍��W
	 * @return ���s���Ă���� -1�A�������Ă���� 1, ����ȊO�� 0
	 */
	private int checkGate2(Address p) {
		int type = getNumber(p);
		int st4[] = new int[4];
		for (int d = 0; d < 4; d++) {
			st4[d] = getState(SideAddress.get(p, d));
		}
		if (type == GATE_VERT) {
			if (st4[0] == LINE || st4[2] == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƕ��s�ɑ����Ă���");
				return -1;
			}
			if (st4[1] == LINE && st4[3] == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƒ������Ă���");
				return 1;
			}
		} else if (type == GATE_HORIZ) {
			if (st4[1] == LINE || st4[3] == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƕ��s�ɑ����Ă���");
				return -1;
			}
			if (st4[0] == LINE && st4[2] == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƒ������Ă���");
				return 1;
			}
		}
		return 0;
	}

	/**
	 * �X�^�[�g����S�[���܂ł̖��ʂ������Ԃ����������ǂ����𒲂ׂ�B
	 * �X�^�[�g����S�[���܂ł̌o�H������Ȃ����Ă��邱�Ƃ����肷��B
	 * ���ׂĂ̖�𐳂����ʉ߂��Ă��邱�Ƃ�O��Ƃ���B
	 * �S�[�����Ȃ��Ƃ��́A���Ԃ����������ǂ����̂ݒ��ׂ�B
	 * @return �����������Œʉ߂��Ă���� 0, ����Ă���ΐ���
	 */
	private int checkRoute() {
		int[] gateNumber = new int[nGate]; // �ʉ߂�����̔ԍ����L�^����B
		int k = 0;
		Address p0; // �X�^�[�g�^�S�[���n�_
		if (goal.isNowhere()) {
			// �S�[�����Ȃ��ꍇ�́C���Ԃ𐔂���N�_�Ƃ��邽�߂̉��̃S�[����ݒ肷��B
			p0 = ��emporalGoal();
		} else {
			p0 = goal;
			if (getLink(p0) == null) {
				System.out.println("�S�[����ʉ߂��Ă��Ȃ��B");
				return 512;
			}
		}
		System.out.println("�X�^�[�g�^�S�[���n�_�� " + p0.toString());
		Address p = p0;
		int d = -1;
		while (true) {
			d = getLineDirection(p, d);
			p = Address.nextCell(p, d);
			if (isGate(p)) {
								System.out.println("�Q�[�g " + getGateNumber(p) + " �ʉ�");
				gateNumber[k] = getGateNumber(p);
				k++;
				if (k > nGate) { // ���ׂĂ̖�𐳂����ʉ߂��Ă��邱�Ƃ��O��Ȃ�΁A���肦�Ȃ�
					System.out.println("��" + nGate + "�̂���" + k + "�ӏ��߂�ʉ߂����B�ʉ߂����傪��������");
					return 128;
				}
			}
			if (p.equals(p0)) {
				System.out.println("�S�[�����B");
				break;
			}
		}
		if (k < nGate) { // ���ׂĂ̖�𐳂����ʉ߂��Ă��邱�Ƃ��O��Ȃ�΁A���肦�Ȃ�
			System.out.println("��" + nGate + "�̂���" + k + "�ӏ��ʉ߂����B�ʉ߂����傪���Ȃ�����");
			return 128;
		}
		for (k = 0; k < nGate; k++) {
			System.out.print(gateNumber[k]);
			System.out.print(' ');
		}
		System.out.println(" �̏��Ԃɖ��ʉ߂����B");
		int gg = 1;
		// �S�[�����ݒ肳��Ă��ȂƂ��́A���S�[���͉��Ԗڂɒʉ߂��Ă��悢���Ƃɂ���B
		if (goal.isNowhere() && nGate >= 1) {
			gg = nGate;
		}
		for (int g = 0; g < gg; g++) {
			for (k = 0; k < nGate; k++) {
				if (gateNumber[k] > 0) { // ��ɔԍ����w�肳��Ă���Ƃ�
					if (gateNumber[k] != ((k + g) % nGate + 1)) {
						break;
					}
				}
			}
			if (k == nGate) {
				System.out.println("��̒ʉߏ����������B");
				return 0;
			}
		}
		for (int g = 0; g < gg; g++) {
			for (k = 0; k < nGate; k++) {
				if (gateNumber[k] > 0) {
					if (gateNumber[k] != ((nGate - 1 - k + g) % nGate + 1)) {
						break;
					}
				}
			}
			if (k == nGate) {
				System.out.println("��̒ʉߏ����������B");
				return 0;
			}
		}
		System.out.println("��̒ʉߏ�������Ă���B");
		return 256;
	}

	/**
	 * �S�[�����ݒ肳��Ă��Ȃ��ՖʂŁA���̃S�[����ݒ肷��B
	 */
	private Address ��emporalGoal() {
		for (Address p : cellAddrs()) {
			if (countLine(p) > 1) {
				return p;
			}
		}
		return Address.NOWHERE;
	}

	/**
	 * �����������ȊO�Ő��̉��тĂ���������ЂƂԂ��B
	 * �����N�����ǂ�̂ɗp����B
	 * @param p
	 * @param direction
	 * @return
	 */
	private int getLineDirection(Address p, int direction) {
		for (int d = 0; d < 4; d++) {
			if (getState(SideAddress.get(p, d)) == LINE && direction != (d^2))
				return d;
		}
		return -1;
	}

	public int checkAnswerCode() {
		int result = 0;
		result |= checkLinks();
		result |= checkGates();
		if (result == 0) // ���[�v�Ɩ�̒ʂ�����������Ƃ��̂ݏ��Ԃ��`�F�b�N����B
			result |= checkRoute();
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1) // ��������܂��͌������Ă���
			message.append(Messages.getString("slalom.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2) // ���Ă��Ȃ���������\n
			message.append(Messages.getString("slalom.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4) // �����̐�������\n
			message.append(Messages.getString("slalom.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8) // �����Ȃ�\n
			message.append(Messages.getString("slalom.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16) // ��̒ʂ���̊ԈႢ������\n
			message.append(Messages.getString("slalom.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 32) == 32) // �ʂ��Ă��Ȃ��傪����\n
			message.append(Messages.getString("slalom.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 64) == 64) // ������ʂ����傪����\n
			message.append(Messages.getString("slalom.AnswerCheckMessage7")); //$NON-NLS-1$
		if ((result & 256) == 256) // ���ʂ鏇�Ԃ�����Ă���\n
			message.append(Messages.getString("slalom.AnswerCheckMessage8")); //$NON-NLS-1$
		if ((result & 512) == 512) // ����ʂ��Ă��Ȃ�\n
			message.append(Messages.getString("slalom.AnswerCheckMessage9")); //$NON-NLS-1$
		return message.toString();
	}
}
