package pencilbox.satogaeri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;


/**
 * �u���Ƃ�����v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int BLANK = -1;
	static final int UNDETERMINED = -2;

	static final int OUTER = -9;

	private List<Area> areaList;
	private int[] number; // ���̐���
	private Area[] area;
	private int[] route; // �}�X�̐��̏�ԁi�s��j

	// ���̃}�X��������ǂ�������֌������Ă��邩
	public static final int NOROUTE = -1;
	public static final int UP = 0;
	public static final int LT = 1;
	public static final int DN = 2;
	public static final int RT = 3;
	public static final int END = 8;

	protected void setup() {
		super.setup();
		number = new int[rows()*cols()];
		area = new Area[rows()*cols()];
		areaList = new LinkedList<Area>();
		Arrays.fill(number, Board.BLANK);
		route = new int[rows()*cols()];
		Arrays.fill(route, Board.NOROUTE);
	}

	public void clearBoard() {
		Arrays.fill(route, Board.NOROUTE);
		initBoard();
	}

	public int getNumber(int r, int c) {
		return getNumber(cell(r, c));
	}

	public int getNumber(Address pos) {
		return getNumber(cell(pos));
	}

	private int getNumber(int i) {
		if (isOn(i))
			return number[i];
		else
			return OUTER;
	}

	public boolean hasNumber(Address pos) {
		return getNumber(pos) != Board.BLANK;
	}

	public void setNumber(int r, int c, int n) {
		setNumber(cell(r, c), n);
	}

	public void setNumber(Address pos, int n) {
		setNumber(cell(pos), n);
	}

	public void setNumber(int i, int n) {
		if (isOn(i))
			number[i] = n;
	}

	public int getRoute(Address pos) {	
		if (isOn(pos))
			return route[cell(pos)];
		else
			return Board.OUTER;
	}

	public void setRoute(Address pos, int b) {
		if (isOn(pos))
			route[cell(pos)] = b;
	}

	public Area getArea(int r, int c) {
		return area[cell(r,c)];
	}

	public Area getArea(Address pos) {
		return area[cell(pos)];
	}

	public void setArea(int r, int c, Area a) {
		area[cell(r, c)] = a;
	}

	public void setArea(Address pos, Area a) {
		area[cell(pos)] =  a;
	}

	/**
	 * �z�u���������̏ꍇ true ��Ԃ�
	 * @return false
	 */
	public boolean isError(Address p) {
		return false;
	}

	/**
	 * ���͂S�}�X�����āA�ǂ���֌������ė������𒲂ׂ�B
	 * ��}�X���牺�����ɓ����ė����Ȃ�A�Ԃ�l�́u���v�A�n�_�Ȃ�END
	 * �f�[�^�Ƃ��Ď��̂Ƃǂ����������H
	 * @param p
	 */
	public int getIncomingDirection(Address p0) {
		int ret = Board.END;
		Address p = p0;
		for (int d : Direction.UP_LT_DN_RT) {
			p = Address.nextCell(p0, d^2);
			if (getRoute(p) == d) {
				if (ret == Board.END)
					ret = d;
				else 
					; // System.out.println("������������������Ă��Ă�������");
			}
		}
		return ret;
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
		// �u�����ꏊ�ɂ�������������
		if (getRoute(p) != Board.NOROUTE) {
			eraseRoute(p);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
	}

	/**
	 * �}�X�̐���ύX����B
	 * ���̂Ƃ��A�s��̃}�X���I�_�ɂ���B
	 * @param p
	 * @param st
	 */
	public void changeRoute(Address p, int st) {
		int prev = getRoute(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, prev, st));
//		System.out.println("record: " + p + " : " + prev + " -> " + st);
		if (st >= Board.UP && st <= Board.RT){
			Address q = p.nextCell(st);
			if (getRoute(q) == Board.NOROUTE) {
				setRoute(p, st);
			} else {
				setRoute(p, Board.NOROUTE);
			}
			setRoute(q, Board.END);
		} else if (st == Board.END) {
// �߂�Ƃ�
			if (prev >= Board.UP && prev <= Board.RT){
				Address q = p.nextCell(prev);
				setRoute(q, Board.NOROUTE);
				setRoute(p, Board.END);
//	�����}�X�̏�Ɂ��������Ƃ�
			} else {
				setRoute(p, Board.END);
			}
		} else if (st == Board.NOROUTE) {
			setRoute(p, Board.NOROUTE);
		}
	}

	public void goBackRoute(Address p, int st) {
		int prev = getRoute(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, prev, st));
//		System.out.println("record: " + p + " : " + prev + " -> " + st);
		if (prev >= Board.UP && prev <= Board.RT){
			Address q = p.nextCell(prev);
			setRoute(q, Board.NOROUTE);
			setRoute(p, Board.END);
		}
	}

	/**
	 * �����}�X���܂ސ����ۂ��Ə����B
	 * @param p0
	 */
	public void eraseRoute(Address p0) {
		ArrayList<Address> routeCells = getCellsOfWholeRoute(p0);
		for (int i = routeCells.size()-1; i >= 0; i--) {
			Address p = routeCells.get(i);
			int s = getRoute(p);
			if (i >= 0)
				changeRoute(p, Board.END);
			if (i == 0)
				changeRoute(p, Board.NOROUTE);
//			setRoute(p, Board.NOROUTE);
//			if (isRecordUndo())
//				fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, s, Board.NOROUTE));
		}

	}

	/**
	 * ���̃}�X���ʉ߂��Ă���o�H�̃}�X�̃��X�g���擾����B
	 * �n�_����I�_�̏��ɕ��ׂ����̂ŁA�A��l��AreaBase�ł͂Ȃ�ArrayList<Address>��p����B
	 * @param p0
	 * @return
	 */
	public ArrayList<Address> getCellsOfWholeRoute(Address p0) {
		ArrayList<Address> routeArea = new ArrayList<Address> ();
		Address p = p0;
		int s = getRoute(p);
		if (s == NOROUTE)
			return routeArea;
		// �n�_�܂ňړ�
		while(true) {
			s = getRoute(p);
			int d = getIncomingDirection(p);
			if (d == Board.UP || d == Board.LT || d == Board.DN || d == Board.RT) {
				p = Address.nextCell(p, d^2);
				continue;
			} else if (d == Board.END) {
				break;
			} else if (d == Board.NOROUTE) {
				break;
			} else {
//				System.out.println("��������������");
				break;
			}
		}
		// �}�X��̈�ɒǉ����Ȃ���I�_�܂ňړ�
		while(true) {
			s = getRoute(p);
			if (s == Board.UP || s == Board.LT || s == Board.DN || s == Board.RT) {
				routeArea.add(p);
				p = Address.nextCell(p, s);
				continue;
			} else if (s == Board.END) {
				routeArea.add(p);
				break;
			} else if (s == Board.NOROUTE) {
				break;
			} else {
//				System.out.println("��������������");
				break;
			}
		}
		return routeArea;
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.STATE) {
				Address p = s.getPos();
				setRoute(p, Board.END);
				int prev = s.getBefore();
				int st = s.getAfter();
				if (st >= Board.UP && st <= Board.RT){
					Address q = p.nextCell(st);
//					if (getRoute(p) == Board.NOROUTE) {
//						setRoute(q, st^2);
//					} else {
						setRoute(q, Board.NOROUTE);
//					}
					setRoute(p, prev);
				} else if (st == Board.END) {
					if (prev >= Board.UP && prev <= Board.RT){
						Address q = p.nextCell(prev);
						setRoute(q, Board.END);
						setRoute(p, prev);
					} else {
						setRoute(p, prev);
					}
				} else if (st == Board.NOROUTE) {
					setRoute(p, prev);
				}
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getBefore());
			}
		} else if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				removeCell(s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				addCell(s.getP0(), s.getPos());
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.STATE) {
				Address p = s.getPos();
				int prev = s.getBefore();
				int st = s.getAfter();
				if (st >= Board.UP && st <= Board.RT){
					Address q = p.nextCell(st);
//					if (getRoute(q) == Board.NOROUTE) {
						setRoute(p, st);
//					} else {
//						setRoute(p, Board.NOROUTE);
//					}
					setRoute(q, Board.END);
				} else if (st == Board.END) {
					if (prev >= Board.UP && prev <= Board.RT){
						Address q = p.nextCell(prev);
						setRoute(q, Board.NOROUTE);
					}
					setRoute(p, Board.END);
				} else if (st == Board.NOROUTE) {
					setRoute(p, Board.NOROUTE);
				}
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getAfter());
			}
		} else if (step instanceof AreaEditStep) {
			AreaEditStep s = (AreaEditStep) step;
			if (s.getOperation() == AreaEditStep.ADDED) {
				addCell(s.getP0(), s.getPos());
			} else if (s.getOperation() == AreaEditStep.REMOVED) {
				removeCell(s.getPos());
			}
		}
	}
	/**
	 * �}�Xp �� p0 �Ɠ����̈�ɂ���B������ p0�� NOWHWER�Ȃ�ΐV�����̈�����
	 * @param p0
	 * @param p
	 */
	void addCell(Address p0, Address p) {
		if (Address.NOWHERE.equals(p0)) {
			Area a = new Area();
			addCellToArea(p, a);
		} else {
			Area a = getArea(p0);
			if (a != null) {
				addCellToArea(p, a);
			}
		}
	}
	/**
	 * �}�X p ��̈悩���菜���B
	 * @param p
	 */
	void removeCell(Address p) {
		Area a = getArea(p);
		if (a != null) {
			removeCellFromArea(p, a);
		}
	}
	/**
	 * �}�X��̈�ɒǉ�����
	 * @param p �ǉ�����}�X�̍��W
	 * @param a �ǉ������̈�
	 */
	public void addCellToArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 0) {
				p0 = a.getTopCell(Address.NOWHERE);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.ADDED));
		}
		if (a.isEmpty()) {
			areaList.add(a);
		}
		setArea(p, a);
		a.add(p);
//		initArea(a);
	}
	/**
	 * �}�X��̈悩���菜��
	 * @param p ��菜���}�X�̍��W
	 * @param a ��菜�����̈�
	 */
	public void removeCellFromArea(Address p, Area a) {
		if (isRecordUndo()) {
			Address p0 = Address.NOWHERE;
			if (a.size() > 1) {
				p0 = a.getTopCell(p);
			}
			fireUndoableEditUpdate(new AreaEditStep(p, p0, AreaEditStep.REMOVED));
		}
		setArea(p, null);
		a.remove(p);
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
//			initArea(a);
		}
	}
	/**
	 * �V�����̈��ǉ�����
	 * @param newArea
	 */
	public void addArea(Area newArea) {
		for (Address p : newArea) {
			setArea(p, newArea);
		}
		areaList.add(newArea);
	}
	/**
	 * �̈悷�ׂẴ}�X��ǉ�����
	 * @param newArea
	 */
	public void addWholeArea(Area newArea) {
		Address[] cells = newArea.toArray(new Address[0]);
		for (Address p : cells) {
			addCellToArea(p, newArea);
		}
	}
	/**
	 * �̈�̂��ׂẴ}�X��̈悩�珜���ė̈���폜����
	 * @param oldArea
	 */
	public void removeWholeArea(Area oldArea) {
		Address[] cells = oldArea.toArray(new Address[0]);
		for (Address p : cells) {
			removeCellFromArea(p, oldArea);
		}
	}
	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}

	public void initBoard() {
	}

	public int countRouteLength(Address p0) {
		int ret = -1;
		Address p = p0;
		int s = getRoute(p);
		if (s == Board.NOROUTE)
			return 0;
		while(true) {
			ret += 1;
			s = getRoute(p);
			if (s == Board.UP || s == Board.LT || s == Board.DN || s == Board.RT) {
				p = Address.nextCell(p, s);
				continue;
			} else if (s == Board.END) {
				break;
			} else if (s == Board.NOROUTE) {
				break;
			} else {
//				System.out.println("��������������");
				break;
			}
		}
		return ret;
	}

	public int checkNumbers() {
		int ret = 0;
		for (Address p : cellAddrs()) {
			if (getNumber(p) >= 0) {
				if (checkNumber(p) != 0) {
					ret ++;
					if (ret > 0)
						return ret;
				}
			}
		}
		return ret;
	}

	public int checkNumber(Address p) {
		int length = countRouteLength(p);
		int num = getNumber(p);
//		System.out.println("�}�X" + p + " ����: " + num + "�ړ�����:" + length);
		return num - length;
	}

	public int checkAreas() {
		int ret = 0;
		for (Area a : getAreaList()) {
			int chk = checkArea(a, true);
			if (chk != 1) {
				ret ++;
				if (ret > 0)
					return ret;
			}
		}
		return ret;
	}

	/**
	 * �̈�Ɋ܂܂��I�_�̐��𒲂ׂ�
	 * @param a �̈�
	 * @param noBall �ړ��O�̐������܂߂邩�ǂ���
	 * @return �I�_���Ȃ����0, 1�����Ȃ�1, �����Ȃ�2
	 */
	public int checkArea(Area a, boolean noBall) {
		int nBall = 0;
		for (Address p : a) {
			if (getRoute(p) == Board.END || (noBall && hasNumber(p) && getRoute(p) == Board.NOROUTE)) {
				nBall ++;
				if (nBall>1)
					return nBall;
			}
		}
//		System.out.println("�̈�" + a.toString() + "�Ɋ܂܂�遛�̐���" + nBall);
		return nBall;
	}

	public int countAreasAndNumbers() {
		int nArea = areaList.size();
		int nNumber = 0;
		for (Address p : cellAddrs()) {
			if (hasNumber(p)) {
				nNumber ++;
			}
		}
//		System.out.println(" �̈搔 " + nArea + " ���̐� " + nNumber);
		return nNumber - nArea;
	}

	public int checkAnswerCode() {
		int result = 0;
		if (areaList.size() == 0)
			result |= 1;
		if (countAreasAndNumbers() != 0)
			result |= 2;
		if (checkNumbers() > 0)
			result |= 4;
		if (checkAreas() > 0)
			result |= 8;
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
//			message.append("�̈悪�P���Ȃ�\n");
			message.append(Messages.getString("satogaeri.AnswerCheckMessage1"));  //$NON-NLS-1$
		if ((result & 2) == 2)
//			message.append("�����������̐����̈搔�ƈ�v���Ă��Ȃ�\n");
			message.append(Messages.getString("satogaeri.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
//			message.append("�ړ��̌��\n");
			message.append(Messages.getString("satogaeri.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8)
//			message.append("�̈�̌��\n");
			message.append(Messages.getString("satogaeri.AnswerCheckMessage4")); //$NON-NLS-1$
		return message.toString();
	}

	public Address i2a(int i) {
		return Address.address(i / cols(), i % cols());
	}

	public int a2i(Address a) {
		return cell(a.r(), a.c());
	}

	public boolean isOn(int p) {
		return p >= 0 && p < rows() * cols();
	}

	public int cell(Address p) {
		return cell(p.r(), p.c());
	}

	public int cell(int r, int c) {
		if (r < 0 || c < 0 || r >= rows() || c >= cols())
			return -1;
		else
			return r * cols() + c;
	}
}
