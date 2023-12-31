package pencilbox.norinori;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * �u�̂�̂�v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int WHITE = -1;
	static final int BLACK = -2;
	static final int UNKNOWN = 0;

	private List<Area> areaList;
	private Area[][] area;
	private int[][] state;
//	private List<Wall> wallList;
	private Wall[][] wall;
	private Wall initializingWall;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
		wall = new Wall[rows()][cols()];
//		wallList = new LinkedList<Wall>();
	}

	public void clearBoard() {
		for (Address p : cellAddrs()) {
			setState(p, UNKNOWN);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == WHITE) {
				changeState(p, UNKNOWN);
			}
		}
	}

	/**
	 * Get state of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the state.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address p) {
		return getState(p.r(), p.c());
	}
	/**
	 * Set state to a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param st The state to set.
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}

	public void setState(Address p, int st) {
		setState(p.r(), p.c(), st);
	}
	/**
	 * �����̍��W�����}�X���ǂ����B
	 * @param r �s���W
	 * @param c ����W
	 * @return ���}�X�Ȃ� true ��Ԃ��B
	 */
	public boolean isBlack(int r, int c) {
		return isOn(r, c) && (state[r][c] == BLACK);
	}
	public boolean isBlack(Address p) {
		return isOn(p) && getState(p) == BLACK;
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param p coordinate of the cell.
	 * @return Returns the area.
	 */
	public Wall getWall(Address p ) {
		return wall[p.r()][p.c()];
	}
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param p coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setWall(Address p, Wall a) {
		wall[p.r()][p.c()] = a;
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c) {
		if (!isOn(r, c))
			return null;
		return area[r][c];
	}

	public Area getArea(Address p) {
		return getArea(p.r(), p.c());
	}

	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}

	public void setArea(Address p, Area a) {
		setArea(p.r(), p.c(), a);
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
		Area a = getArea(p);
		if (a != null) {
			if (prev == BLACK) {
				a.getBlock().remove(p);
			}
			if (st == BLACK) {
				a.getBlock().add(p);
			}
		}
	}

	/**
	 * �V�����̈��ǉ�����
	 * @param newArea
	 */
	public void addArea(Area newArea) {
		for (Address p : newArea) {
			area[p.r()][p.c()] = newArea;
		}
		areaList.add(newArea);
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
		if (getState(p) == Board.BLACK) {
			a.getBlock().add(p);
		}
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
		if (getState(p) == Board.BLACK) {
			a.getBlock().remove(p);
		}
		if (a.isEmpty()) {
			areaList.remove(a);
		} else {
//			initArea(a);
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

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getBefore());
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
			changeState(s.getPos(), s.getAfter());
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
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}

	public void initBoard() {
		initBlocks();
	}

	/**
	 * ���̃}�X��2��2�̍��}�X�u���b�N�̈�p���ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return 2x2�u���b�N�Ȃ�� true
	 */
	boolean is2x2Block(Address p) {
		if (isBlack(p)) {
			for (int d = 0; d < 4; d++) {
				Address p1 = Address.nextCell(p, d);
				if (isBlack(p1)) {
					Address p2 = Address.nextCell(p1, (d+1)%4);
					if (isBlack(p2)) {
						Address p3 = Address.nextCell(p2, (d+2)%4);
						if (isBlack(p3)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public int checkAnswerCode() {
		int result = 0;
		if (areaList.size() == 0)
			result |= 1;
		// �u���b�N���ƂɂQ�}�X�̍��}�X�����邩�̃`�F�b�N
		result |= checkBlocks();
		// ���}�X�Q�}�X�Ȃ���̃`�F�b�N
		result |= checkWalls();
		return result;
	}

	/**
	 * ���݂̔Ֆʏ�ԂɊ�Â��āC�̈�̐ݒ���s��
	 */
	public void initBlocks() {
		for (Area a : areaList) {
			a.getBlock().clear();
		}
		for (Address p : cellAddrs()) {
			if (getArea(p) != null) {
				if (getState(p) == BLACK) {
					getArea(p).getBlock().add(p);
				}
			}
		}
	}

	public int checkBlocks() {
		initBlocks();
		int result = 0;
		for (Area a : areaList) {
			if (a.getBlock().size() != 2) {
				return 2;
			}
		}
		return result;
	}

	private int checkWalls() {
		ArrayUtil.initArrayObject2(wall, null);
//		wallList.clear();
		for (Address p : cellAddrs()) {
			if (getState(p) == BLACK && getWall(p) == null) {
				initWall(p);
				if (initializingWall.size() != 2) {
					return 4;
				}
			}
		}
		return 0;
	}
	/**
	 * ����}�X���N�_�Ƃ��鍕�}�X�̂Ȃ���𒲂ׂ�Wall���쐬����
	 * @param p
	 */
	private void initWall(Address p) {
		initializingWall = new Wall();
		initWall1(p);
//		wallList.add(initializingWall);
	}

	private boolean initWall1(Address p) {
		if (!isOn(p))
			return false;
		if (!isBlack(p))
			return false;
		if (getWall(p) == initializingWall)
			return false;
		initializingWall.add(p);
		setWall(p, initializingWall);
		for (int d = 0; d < 4; d++) {
			initWall1(Address.nextCell(p, d));
		}
		return true;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("norinori.AnswerCheckMessage1"));  //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("norinori.AnswerCheckMessage2"));  //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("norinori.AnswerCheckMessage3"));  //$NON-NLS-1$
		return message.toString();
	}
}
