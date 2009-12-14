package pencilbox.slalom;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.util.ArrayUtil;

/**
 * �u�X�����[���v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;

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
		goal = new Address(-1, -1);
		state = new int[2][][];
		state[VERT] = new int[rows()][cols()-1];
		state[HORIZ] = new int[rows()-1][cols()];
		linkList = new LinkedList<Link>();
		link = new Link[2][][];
		link[VERT] = new Link[rows()][cols()-1];
		link[HORIZ] = new Link[rows()-1][cols()];
	}

	/**
	 * �w�肵���}�X�ɐ����}�X�C���}�X�C���}�X�C�󔒃}�X�̏�Ԃ�ݒ肷��
	 * �S�[���͂P�����ȉ��Ƃ���B
	 * @param r �s���W
	 * @param c ����W
	 * @param n �ݒ肷����
	 */
	public void setNumber(int r, int c, int n) {
		int n0 = number[r][c];
		if (n0 == Board.GOAL) {
			goal.setNowhere();
		}
		if (n == Board.GOAL) {
			if (goal.isNowhere()) {
			} else {
				number[goal.r()][goal.c()] = Board.BLANK;
			}
			goal.set(r, c);
		}
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
	 * @param r �s���W
	 * @param c ����W
	 * @return ���}�X�ł���� true
	 */
	public boolean isWall(int r, int c) {
		return number[r][c] >= 0 || number[r][c] == UNDECIDED_NUMBER;
	}

	public boolean isWall(Address pos) {
		return isWall(pos.r(), pos.c());
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
	 * @param d
	 * @param r
	 * @param c
	 * @return �����W�̗�����2�}�X�����ꂩ�����}�X�ł���� true
	 */
	public boolean hasWall(int d, int r, int c) {
		if (d == VERT)
			return isWall(r, c) || isWall(r, c+1);
		else if (d == HORIZ)
			return isWall(r, c) || isWall(r+1, c);
		return false;
	}

	public boolean hasWall(SideAddress pos) {
		return hasWall(pos.d(), pos.r(), pos.c());
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
	 * �ӏ�Ԃ̎擾�B�}�X�ƌ����ō��W�w�肷��B
	 * @param pos
	 * @param d
	 * @return
	 */
	public int getStateJ(Address pos, int d) {
		return getState(SideAddress.get(pos, d));
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

	/**
	 * �ӏ�Ԃ̐ݒ�B�}�X�ƌ����ō��W�w�肷��B
	 * @param pos
	 * @param d
	 * @param st
	 */
	public void setStateJ(Address pos, int d, int st) {
		setState(SideAddress.get(pos, d), st);
	}

	public boolean isLine(int d, int r, int c) {
		if (!isSideOn(d, r, c))
			return false;
		return state[d][r][c] == LINE;
	}

	public Link getLink(int d, int r, int c) {
		if (isSideOn(d, r, c))
			return link[d][r][c];
		else
			return null;
	}

	public Link getLink(SideAddress pos) {
		return link[pos.d()][pos.r()][pos.c()];
	}
	/**
	 * ���̃}�X���܂� Link ��Ԃ�
	 */
	public Link getLink(int r, int c) {
		Link link;
		link = getLink(VERT, r, c - 1);
		if (link != null)
			return link;
		link = getLink(VERT, r, c);
		if (link != null)
			return link;
		link = getLink(HORIZ, r - 1, c);
		if (link != null)
			return link;
		link = getLink(HORIZ, r, c);
		if (link != null)
			return link;
		return null;
	}

	public void setLink(int d, int r, int c, Link l) {
		link[d][r][c] = l;
	}

	public void setLink(SideAddress pos, Link l) {
		link[pos.d()][pos.r()][pos.c()] = l;
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
				changeStateA(side, UNKNOWN);
			}
		}
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * @param d �c������
	 * @param r �s���W
	 * @param c ����W
	 * @param st �ύX��̏��
	 */
	public void changeState(int d, int r, int c, int st) {
		int previousState = getState(d, r, c);
		setState(d, r, c, st);
		if (previousState == LINE) {
			cutLink(d, r, c);
		}
		if (st == LINE) {
			connectLink(d, r, c);
		}
	}

	public void changeState(SideAddress pos, int st) {
		changeState(pos.d(), pos.r(), pos.c(), st);
	}

	/**
	 * �ӂ̏�Ԃ��w�肵����ԂɕύX����
	 * �A���h�D���X�i�[�ɕύX��ʒm����
	 * @param pos �Ӎ��W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(SideAddress pos, int st) {
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(pos.d(), pos.r(), pos.c(), getState(pos), st)));
		changeState(pos, st);
	}

	public void clearBoard() {
		super.clearBoard();
		ArrayUtil.initArrayInt3(state, UNKNOWN);
		initBoard();
	}

	public void trimAnswer() {
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < rows(); r++) {
				for (int c = 0; c < cols(); c++) {
					if (getState(d, r, c) == NOLINE)
						setState(d, r, c, UNKNOWN);
				}
			}
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				setGateNumber(r, c, 0);
			}
		}
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				int n = getNumber(r, c);
				if (n == GOAL) {
					goal.set(r, c);
				} else if (n == GATE_HORIZ) {
					if (isOn(r, c-1) && getNumber(r, c-1) == GATE_HORIZ) {
					} else {
						nGate ++;
					}
				} else if (n == GATE_VERT) {
					if (isOn(r-1, c) && getNumber(r-1, c) == GATE_VERT) {
					} else {
						nGate ++;
					}
				}
			}
		}
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				int n = getNumber(r, c);
				if (n > 0) {
					initGateNumber(r, c, n);
				}
			}
		}
	}

	/**
	 * ���}�X�����̔��Α��̍��}�X��T��
	 * @param p0 �N�_�}�X
	 * @param d �������
	 * @return�@��̔��Α��ɍ��}�X�܂��͊O���Ȃ�΂��̍��W�C����ȊO��null
	 */
	private Address getAnotherPole(Address p0, int d) {
		Address p = new Address(p0);
		int gateType = 0;
		if (d == Direction.UP || d == Direction.DN) {
			gateType = GATE_VERT;
		} else if (d == Direction.LT || d == Direction.RT) {
			gateType = GATE_HORIZ;
		}
		while (true) {
			p.move(d);
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
	 * @param r
	 * @param c
	 * @param d
	 * @param n
	 */
	private void setGateNumber(int r, int c, int d, int n) {
		Address p = new Address(r, c);
		int t = 0;
		if (d == 0 || d == 2)
			t = GATE_VERT;
		else
			t = GATE_HORIZ;
		p.set(r, c);
		while (true) {
			p.move(d);
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
	 * @param r
	 * @param c
	 * @param n0 ���̐��Ƃ���
	 */
	private void initGateNumber(int r, int c, int n0) {
		Address p = new Address(r, c);
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
			p.set(r, c);
			p.move(d);
			if (isOn(p) && getNumber(p) == t) {
//				System.out.println(d + "�̌����ɖ傪������");
				d1 = d;
				count++;
				p1 = getAnotherPole(p, d);
				if (p1 != null && isOn(p1)) {
					int n1 = getNumber(p1);
					if (n1 == n0) { // �Ί݂̖�Ɠ����ԍ�
						ng = n1;
						setGateNumber(r, c, d, n1);
//						System.out.println("�Ί݂̐����}�X" + p1.toString() + "�Ƃ̊Ԃ̖�̔ԍ��� " + n1);
					} else if (n1 > 0 && n1 != n0) {
						ng = -1;
						setGateNumber(r, c, d, -1);
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
				setGateNumber(r, c, d1, n0);
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				initLink(r, c);
			}
		}
	}

	/**
	 * ����}�X���܂� Link �̏�����
	 * link[][][] �͏�������Ă�����̂Ƃ���
	 * @param r Link�������̋N�_�}�X�̍s���W
	 * @param c Link�������̋N�_�}�X�̗���W
	 */
	void initLink(int r, int c) {
		initializingLink = new Link();
		initLink1(VERT, r, c - 1);
		initLink1(VERT, r, c);
		initLink1(HORIZ, r - 1, c);
		initLink1(HORIZ, r, c);
		if (!initializingLink.isEmpty())
			linkList.add(initializingLink);
		// printLink(d,r,c);
	}

	private void initLink1(int d, int r, int c) {
		if (!isSideOn(d, r, c))
			return;
		if (!isLine(d, r, c))
			return;
		if (getLink(d, r, c) != null)
			return;
		initializingLink.add(d, r, c);
		setLink(d, r, c, initializingLink);
		if (d == VERT) {
			initLink1(VERT , r  , c-1);
			initLink1(VERT , r  , c+1);
			initLink1(HORIZ, r-1, c  );
			initLink1(HORIZ, r-1, c+1);
			initLink1(HORIZ, r  , c  );
			initLink1(HORIZ, r  , c+1);
		}
		if (d == HORIZ) {
			initLink1(HORIZ, r-1, c  );
			initLink1(HORIZ, r+1, c  );
			initLink1(VERT , r  , c-1);
			initLink1(VERT , r+1, c-1);
			initLink1(VERT , r  , c  );
			initLink1(VERT , r+1, c  );
		}
	}
	/**
	 * Link ����
	 */	
	void connectLink(int d, int r, int c) {
		Link newLink = null;
		Link link1 = null;
		Link link2 = null;
		if (d==VERT) {
			link1 = getLink(r,c);
			link2 = getLink(r,c+1);
		} else if (d==HORIZ) {
			link1 = getLink(r,c);
			link2 = getLink(r+1,c);
		}
		if (link1==null && link2 == null) {
			newLink = new Link();
			linkList.add(newLink);
		} else if (link1==null && link2!=null) {
			newLink = link2;
		} else if (link1!=null && link2==null) {
			newLink = link1;
		} else if (link1==link2) {
			newLink = link1;
		} else {
			if (link1.size() >= link2.size()) {
				newLink = link1;
				newLink.addAll(link2);
				for(SideAddress joint : link2) {
					setLink(joint, newLink);
				}
				linkList.remove(link2);
			}
			else {
				newLink = link2;
				newLink.addAll(link1);
				for(SideAddress joint : link1) {
					setLink(joint, newLink);
				}
				linkList.remove(link1);
			}
		}
		newLink.add(d,r,c);
		setLink(d,r,c, newLink);
	}
	/**
	 * Link �ؒf
	 */
	void cutLink(int d, int r, int c) {
		Link oldLink = getLink(d, r, c);
		Link longerLink = null;
		for (SideAddress joint : oldLink) {
			setLink(joint, null);
		}
		linkList.remove(oldLink);
		if (d==VERT) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r, c+1);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		} else if (d==HORIZ) {
			initLink(r, c);
			longerLink = initializingLink;
			initLink(r+1, c);
			if (initializingLink.size() > longerLink.size())
				longerLink = initializingLink;
		}
		longerLink.setId(oldLink.getId());
	}

	/**
	 * �}�X�̏㉺���E4�����̂����C���ݐ���������Ă��鐔��Ԃ�
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return �}�X�̏㉺���E�Ɉ�����Ă�����̐�
	 */
	public int countLine(int r, int c) {
		int no = 0;
		if (r < rows()-1 && isLine(HORIZ, r, c))
			no++;
		if (c < cols()-1 && isLine(VERT, r, c))
			no++;
		if (r > 0 && isLine(HORIZ, r-1, c))
			no++;
		if (c > 0 && isLine(VERT, r, c-1))
			no++;
		return no;
	}

	/**
	 * ���Ɋւ�������`�F�b�N����
	 * @return
	 */
	private int checkLinks() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				int l = countLine(r,c);
				if (l > 2) {
					result |= 1;
				} else if (l == 1) {
					result |= 2;
				}
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (isGate(r, c)) {
					int ret = checkGate1(r, c);
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
	private int checkGate1(int r, int c) {
		int count = 0;
		int gateType = getNumber(r, c);
		Address p = new Address(r, c);
//		System.out.println(p.toString() + "�̖�𒲂ׂ�B");
		int d = 0;
		if (gateType == Board.GATE_HORIZ) {
			d = Direction.RT;
			p.set(r, c-1);
		} else if (gateType == Board.GATE_VERT) {
			d = Direction.DN;
			p.set(r-1, c);
		}
		if (isOn(p) && getNumber(p) == gateType) {
			return -2; // �����ς݂̂͂�
		}
		p.set(r, c);
		while (true) {
			int ret = checkGate2(p);
			if (ret == -1) {
//				System.out.println(p.toString() + "�̈ʒu�̖�̒ʂ�������������B");
				return -1;
			} else if (ret == 1) {
				count ++;
			}
			p.move(d);
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
		if (type == GATE_VERT) {
			if (getStateJ(p, Direction.UP) == LINE || getStateJ(p, Direction.DN) == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƕ��s�ɑ����Ă���");
				return -1;
			}
			if (getStateJ(p, Direction.LT) == LINE && getStateJ(p, Direction.RT) == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƒ������Ă���");
				return 1;
			}
		} else if (type == GATE_HORIZ) {
			if (getStateJ(p, Direction.LT) == LINE || getStateJ(p, Direction.RT) == LINE) {
//				System.out.println("�Q�[�g" + p.toString() + "�ƕ��s�ɑ����Ă���");
				return -1;
			}
			if (getStateJ(p, Direction.UP) == LINE && getStateJ(p, Direction.DN) == LINE) {
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
			p0 = new Address(goal);
			if (getLink(p0.r(), p0.c()) == null) {
//				System.out.println("�S�[����ʉ߂��Ă��Ȃ��B");
				return 512;
			}
		}
//		System.out.println("�X�^�[�g�^�S�[���n�_�� " + p0.toString());
		Address p = new Address(p0);
		int d = -1;
		while (true) {
			d = getLineDirection(p, d);
			p.move(d);
			if (isGate(p)) {
//				System.out.println("�Q�[�g " + getGate(p).getNumber() + " �ʉ�");
				gateNumber[k] = getGateNumber(p);
				k++;
				if (k > nGate) { // ���ׂĂ̖�𐳂����ʉ߂��Ă��邱�Ƃ��O��Ȃ�΁A���肦�Ȃ�
//					System.out.println("��" + nGate + "�̂���" + k + "�ӏ��߂�ʉ߂����B�ʉ߂����傪��������");
					return 128;
				}
			}
			if (p.equals(p0)) {
//				System.out.println("�S�[�����B");
				break;
			}
		}
		if (k < nGate) { // ���ׂĂ̖�𐳂����ʉ߂��Ă��邱�Ƃ��O��Ȃ�΁A���肦�Ȃ�
//			System.out.println("��" + nGate + "�̂���" + k + "�ӏ��ʉ߂����B�ʉ߂����傪���Ȃ�����");
			return 128;
		}
//		for (k = 0; k < nGate; k++) {
//			System.out.print(gateNumber[k]);
//			System.out.print(' ');
//		}
//		System.out.println(" �̏��Ԃɖ��ʉ߂����B");
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
//				System.out.println("��̒ʉߏ����������B");
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
//				System.out.println("��̒ʉߏ����������B");
				return 0;
			}
		}
//		System.out.println("��̒ʉߏ�������Ă���B");
		return 256;
	}

	/**
	 * �S�[�����ݒ肳��Ă��Ȃ��ՖʂŁA���̃S�[����ݒ肷��B
	 */
	private Address ��emporalGoal() {
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (countLine(r, c) > 1) {
					return new Address(r, c);
				}
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
		if (getStateJ(p, Direction.UP) == LINE && direction != Direction.DN)
			return Direction.UP;
		if (getStateJ(p, Direction.LT) == LINE && direction != Direction.RT)
			return Direction.LT;
		if (getStateJ(p, Direction.DN) == LINE && direction != Direction.UP)
			return Direction.DN;
		if (getStateJ(p, Direction.RT) == LINE && direction != Direction.LT)
			return Direction.RT;
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
			message.append(Messages.getString("Board.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2) // ���Ă��Ȃ���������\n
			message.append(Messages.getString("Board.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4) // �����̐�������\n
			message.append(Messages.getString("Board.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8) // �����Ȃ�\n
			message.append(Messages.getString("Board.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16) // ��̒ʂ���̊ԈႢ������\n
			message.append(Messages.getString("Board.AnswerCheckMessage5")); //$NON-NLS-1$
		if ((result & 32) == 32) // �ʂ��Ă��Ȃ��傪����\n
			message.append(Messages.getString("Board.AnswerCheckMessage6")); //$NON-NLS-1$
		if ((result & 64) == 64) // ������ʂ����傪����\n
			message.append(Messages.getString("Board.AnswerCheckMessage7")); //$NON-NLS-1$
		if ((result & 256) == 256) // ���ʂ鏇�Ԃ�����Ă���\n
			message.append(Messages.getString("Board.AnswerCheckMessage8")); //$NON-NLS-1$
		if ((result & 512) == 512) // ����ʂ��Ă��Ȃ�\n
			message.append(Messages.getString("Board.AnswerCheckMessage9")); //$NON-NLS-1$
		return message.toString();
	}

	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	class Step extends AbstractUndoableEdit {

		private int direction;
		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * �R���X�g���N�^
		 * @param d �����c��
		 * @param r �ύX���ꂽ�}�X�̍s���W
		 * @param c �ύX���ꂽ�}�X�̗���W
		 * @param b �ύX�O�̏��
		 * @param a �ύX��̏��
		 */
		public Step(int d, int r, int c, int b, int a) {
			super();
			direction = d;
			row = r;
			col = c;
			before = b;
			after = a;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			changeState(direction, row, col, before);
		}

		public void redo() throws CannotRedoException {
			super.redo();
			changeState(direction, row, col, after);
		}
	}
}
