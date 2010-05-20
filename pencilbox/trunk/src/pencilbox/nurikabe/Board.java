package pencilbox.nurikabe;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  �u�ʂ肩�ׁv�ՖʃN���X
 */
public class Board extends BoardBase {

	public static final int SPACE = -1;
	public static final int WALL = -2;
	public static final int UNKNOWN = 0;
	public static final int UNDECIDED_NUMBER = -3;
	
	private int[][] state;

	private Area[][] area; // ���}�X�܂��͊m�蔒�}�X�̈�p
	private Area[][] whiteArea; // �󔒃}�X�܂ޔ��}�X�̈搳�𔻒�p
	private List<Area> wallAreaList;
	private List<Area> spaceAreaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		whiteArea = new Area[rows()][cols()];
		wallAreaList = new LinkedList<Area>();
		spaceAreaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isNumber(p))
				setState(p, UNKNOWN);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == SPACE)
				setState(p, UNKNOWN);
		}
		initBoard();
	}

	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	/**
	 * @param r row coordinate of the cell
	 * @param c column coordinate of the cell
	 * @return Returns the state of the cell.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * @param r row coordinate of the cell
	 * @param c column coordinate of the cell
	 * @param st state to set
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}

	public boolean isNumber(int r, int c) {
		return isOn(r,c) && (state[r][c] > 0 || state[r][c] == UNDECIDED_NUMBER);
	}
	
	public boolean isNumber(Address pos) {
		return isNumber(pos.r(), pos.c());
	}
	
	public boolean isWall(Address p) {
		return isOn(p) && (state[p.r()][p.c()] == WALL);
	}
	public boolean isUnknown(int r, int c) {
		return (state[r][c] == UNKNOWN);
	}
	public int getSpaceOrWall(Address p) {
		int r=p.r(), c=p.c();
		if (state[r][c] > 0)
			return SPACE;
		else if (state[r][c] == UNDECIDED_NUMBER)
			return SPACE;
		else
			return state[r][c];
	}
	public boolean isSpaceOrNumber(Address p) {
		return isOn(p) && (state[p.r()][p.c()] > 0 || state[p.r()][p.c()] == SPACE || state[p.r()][p.c()] == UNDECIDED_NUMBER);
	}

	public void initBoard() {
		initAreas();
	}
	/**
	 * ���݂̔Ֆʏ�ԂɊ�Â��āC�̈�̐ݒ���s��
	 */
	public void initAreas() {
		ArrayUtil.initArrayObject2(area, null);
		wallAreaList.clear();
		spaceAreaList.clear();
		for (Address p : cellAddrs()) {
			if (getState(p) != UNKNOWN && getArea(p) == null) {
				initArea(p);
			}
		}
	}
	/**
	 * �w�肵���}�X���N�_�Ƃ��ă}�X�̂Ȃ���𒲂ׂ�Area���쐬����
	 * @param p
	 */
	void initArea(Address p) {
		initializingArea = makeNewArea(p);
		initArea1(p);
	}

	private void initArea1(Address p) {
		if (!isOn(p))
			return;
		if (getArea(p) == initializingArea)
			return;
		if (getSpaceOrWall(p) != initializingArea.getAreaType())
			return;
		initializingArea.add(p);
		if (isNumber(p))
			initializingArea.addNumber(getState(p));
		setArea(p, initializingArea);
		for (int d=0; d<4; d++) {
			initArea1(p.nextCell(d));
		}
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(Address p) {
		// mergeArea �Ȃǂ���g�p����ꍇ�̂��߂ɁC�����`�F�b�N���s��
		if (!isOn(p))
			return null;
		return area[p.r()][p.c()];
	}
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(Address p, Area a) {
		area[p.r()][p.c()] = a;
	}

	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param st �ύX��̏��
	 */
	public void changeState(Address p, int st) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, getState(p), st));
		int prevSt = getSpaceOrWall(p);
		setState(p, st);
		int type;
		if (st > 0)
			type = SPACE;
		else
			type = st;
		if (prevSt != UNKNOWN) {
			splitArea(p, prevSt);
		}
		if (st != UNKNOWN) {
			mergeArea(p, type);
		}
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
	 * �}�X�̏�Ԃ�ύX�����Ƃ��� Area �����������s��
	 * @param r �ύX�����}�X�̍s���W
	 * @param c �ύX�����}�X�̗���W
	 * @param type �ύX��̗̈�̎��
	 */
	void mergeArea(Address p, int type) {
		Area mergedArea = null;
		for (int d=0; d<4; d++) {
			Address p1 = p.nextCell(d);
			mergedArea = mergeArea1(getArea(p1), mergedArea, type);
		}
		if (mergedArea == null) {
			mergedArea = makeNewArea(p);
		}
		mergedArea.add(p);
		if (isNumber(p))
			mergedArea.addNumber(getState(p));
		setArea(p, mergedArea);
	}

	private Area mergeArea1(Area a, Area mergedArea, int type) {
		if (a != null && a.getAreaType() == type) {
			if (mergedArea == null){
				mergedArea = a;
			} else if (mergedArea != a) {
				mergedArea.addAll(a);
				for (Address pos : a) {
					setArea(pos, mergedArea);
					if (isNumber(pos))
						mergedArea.addNumber(getState(pos));
				}
				removeAreaFromList(a);
			}
		}
		return mergedArea;
	}
	/**
	 * �}�X�̏�Ԃ�ύX�C���������Ƃ��� Area�̕����������s��
	 * @param r �ύX�����}�X�̍s���W
	 * @param c �ύX�����}�X�̗���W
	 * @param type �ύX��̗̈�̎��
	 */
	void splitArea(Address p, int type) {
		Area oldArea = getArea(p);
		Area largerArea = null;
		removeAreaFromList(oldArea);
		for (Address p1 : oldArea) {
			setArea(p1, null);
		}
		for (int d=0; d<4; d++) {
			Address p1 = p.nextCell(d);
			if (isOn(p1) && getSpaceOrWall(p1)==type && getArea(p1) == null) {
				initArea(p1);
				if (largerArea == null || initializingArea.size() > largerArea.size())
					largerArea = initializingArea;
			}
		}
		if (largerArea != null) {
			largerArea.setId(oldArea.getId());
		}
	}
	/**
	 * �����ɍ��W��^�����}�X���܂� Area ��V�����쐬����
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return �쐬�����̈�
	 */
	private Area makeNewArea(Address p) {
		if (isWall(p)) {
			Area a = new Area(WALL);
			wallAreaList.add(a);
			return a;
		} else if (isSpaceOrNumber(p)) {
			Area a = new Area(SPACE);
			spaceAreaList.add(a);
			return a;
		} else {
			return null;
		}
	}

	private void removeAreaFromList(Area a) {
		int type = a.getAreaType();
		if (type == WALL)
			wallAreaList.remove(a);
		else if (type == SPACE) {
			spaceAreaList.remove(a);
		}
	}

	/**
	 * ���̃}�X��2��2�̍��}�X�u���b�N�̈�p���ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return 2x2�u���b�N�Ȃ�� true
	 */
	boolean is2x2Block(Address p) {
		if (isWall(p)) {
			for (int d = 0; d < 4; d++) {
				Address p1 = Address.nextCell(p, d);
				if (isWall(p1)) {
					Address p2 = Address.nextCell(p1, (d+1)%4);
					if (isWall(p2)) {
						Address p3 = Address.nextCell(p2, (d+2)%4);
						if (isWall(p3)) {
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
		for (Address p : cellAddrs()) {
			if (is2x2Block(p)) {
				result |= 64;
			}
		}
		if (wallAreaList.size() > 1) {
			result |= 32;
		}
//		result |= checkSpaceAreas();
		result |= checkWhiteAreas();
		return result;
	}

//	/**
//	 * �m�蔒�}�X�݂̂𔒃}�X�Ƃ݂Ȃ��āC���}�X�̈�Ɋւ��Đ��𔻒肷��B
//	 */
//	private int checkSpaceAreas() {
//		int ret = 0;
//		for (Area a : spaceAreaList) {
//			ret |= checkSpaceArea(a);
//		}
//		return ret;
//	}

	/**
	 * �󔒃}�X�Ɗm�蔒�}�X���Ƃ��Ƃ���ʂ������}�X�Ƃ݂Ȃ��āC���}�X�̈�Ɋւ��Đ��𔻒肷��B
	 */
	private int checkWhiteAreas() {
		ArrayUtil.initArrayObject2(whiteArea, null);
		int ret = 0;
		for (Address p : cellAddrs()) {
			if (getState(p) != WALL && whiteArea[p.r()][p.c()] == null) {
				initializingArea = new Area(SPACE);
				initWhiteArea1(p);
				ret |= checkSpaceArea(initializingArea);
			}
		}
		return ret;
	}

	private int checkSpaceArea(Area a) {
		int ret = 0;
		int number = a.getNumber();
		if (number == 0) {
			ret |= 16;
		} else if (number == Area.MULTIPLE_NUMBER) {
			ret |= 8;
		} else if (number == UNDECIDED_NUMBER) {
			;
		} else if (number > 0) {
			if (a.size() < number) {
				ret |= 4;
			} else if (a.size() > number) {
				ret |= 2;
			}
		}
		return ret;
	}

	private void initWhiteArea1(Address p) {
		if (!isOn(p))
			return;
		if (whiteArea[p.r()][p.c()] == initializingArea)
			return;
		if (getState(p) == WALL)
			return;
		initializingArea.add(p);
		if (isNumber(p))
			initializingArea.addNumber(getState(p));
		whiteArea[p.r()][p.c()] = initializingArea;
		for (int d=0; d<4; d++) {
			initWhiteArea1(p.nextCell(d));
		}
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		if (result==1)
			return Messages.getString("nurikabe.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
//		if ((result & 2) == 2)
//			message.append("�������ʐς̑傫���V�}������\n"); 
//		if ((result & 4) == 4)
//			message.append("�������ʐς̏������V�}������\n"); 
		if ((result & 2) == 2 || (result & 4) == 4)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage2"));  //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage4"));  //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage5"));  //$NON-NLS-1$
		if ((result & 32) == 32)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage6"));  //$NON-NLS-1$
		if ((result & 64) == 64)
			message.append(Messages.getString("nurikabe.AnswerCheckMessage7"));  //$NON-NLS-1$
		return message.toString();
	}
}
