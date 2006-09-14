package pencilbox.nurikabe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
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

	private Area[][] area;
	private List areaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList();
	}

	public void clearBoard() {
		super.clearBoard();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isNumber(r,c))
					state[r][c] = UNKNOWN;
				}
		}
		ArrayUtil.initArrayObject2(area, null);
		areaList.clear();
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (state[r][c] == SPACE)
					state[r][c] = UNKNOWN;
				}
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
	/**
	 * @param r row coordinate of the cell
	 * @param c column coordinate of the cell
	 * @param st state to set
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	public boolean isNumber(int r, int c) {
		return isOn(r,c) && (state[r][c] > 0 || state[r][c] == UNDECIDED_NUMBER);
	}
	public boolean isWall(int r, int c) {
		return isOn(r,c) && (state[r][c] == WALL);
	}
	public boolean isUnknown(int r, int c) {
		return (state[r][c] == UNKNOWN);
	}
	public int getSpaceOrWall(int r, int c) {
		if (state[r][c] > 0)
			return SPACE;
		else if (state[r][c] == UNDECIDED_NUMBER)
			return SPACE;
		else
			return state[r][c];
	}
	public boolean isSpaceOrNumber(int r, int c) {
		return isOn(r,c) && (state[r][c] > 0 || state[r][c] == SPACE || state[r][c] == UNDECIDED_NUMBER);
	}

	public void initBoard() {
		initAreas();
	}
	/**
	 * ���݂̔Ֆʏ�ԂɊ�Â��āC�̈�̐ݒ���s��
	 */
	public void initAreas() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) != UNKNOWN && area[r][c] == null) {
					initArea(r,c);
				}
			}
		}
	}
	/**
	 * ����}�X���܂� Area �̏�����
	 * domain[][] �͏�������Ă�����̂Ƃ���
	 * @param r
	 * @param c
	 */
	void initArea(int r, int c) {
		initializingArea = makeNewArea(r,c);
		initializingArea.add(r,c);
		setArea(r,c,initializingArea);
		if (isNumber(r,c))
			initializingArea.addNumber(getState(r,c));
		areaList.add(initializingArea);
		initArea1(r, c);
	}

	private void initArea1(int r, int c) {
		if (initArea2(r-1,c) == true) initArea1(r-1,c);
		if (initArea2(r,c-1) == true) initArea1(r,c-1);
		if (initArea2(r+1,c) == true) initArea1(r+1,c);
		if (initArea2(r,c+1) == true) initArea1(r,c+1);
	}
	
	private boolean initArea2(int r, int c) {
		if (!isOn(r,c))
			return false;
		if (getSpaceOrWall(r,c) != initializingArea.getAreaType())
			return false;
		if (getArea(r,c) == initializingArea)
			return false;
		initializingArea.add(r,c);
		if (isNumber(r,c))
			initializingArea.addNumber(getState(r,c));
		setArea(r, c, initializingArea);
		return true;
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c ) {
		// mergeArea �Ȃǂ���g�p����ꍇ�̂��߂ɁC�����`�F�b�N���s��
		if (!isOn(r,c))
			return null;
		return area[r][c];
	}
	/**
	 * �Տ�̃}�X�ɁC���̃}�X�̏�������̈��ݒ肷��
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(int r, int c, Area a) {
		area[r][c] = a;
	}

	public void changeState(int r, int c, int st) {
		int prevSt = getSpaceOrWall(r, c);
		setState(r, c, st);
		int type;
		if (st > 0)
			type = SPACE;
		else
			type = st;
		if (prevSt != UNKNOWN) {
			splitArea(r, c, prevSt);
		}
		if (st != UNKNOWN) {
			mergeArea(r, c, type);
		}
	}
	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param r �s���W
	 * @param c ����W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(int r, int c, int st) {
		fireUndoableEditUpdate(new UndoableEditEvent(this, new Step(r, c, state[r][c], st)));
		changeState(r, c, st);
	}

	/**
	 * �}�X�̏�Ԃ� ���� �� st �ƕύX����
	 * @param r �s���W
	 * @param c ����W
	 * @param st �؂�ւ�����
	 */
	public void toggleState(int r, int c, int st) {
		if(isNumber(r,c))
			return;
		if (state[r][c] == st)
			changeStateA(r, c, UNKNOWN);
		else
			changeStateA(r, c, st);
	}

	/**
	 * �}�X�̏�Ԃ�ύX�����Ƃ��� Area �����������s��
	 * @param r �ύX�����}�X�̍s���W
	 * @param c �ύX�����}�X�̗���W
	 * @param type �ύX��̗̈�̎��
	 */
	void mergeArea(int r, int c, int type) {
		Area mergedArea = null;
		mergedArea = mergeArea1(getArea(r-1, c), mergedArea, type);
		mergedArea = mergeArea1(getArea(r, c-1), mergedArea, type);
		mergedArea = mergeArea1(getArea(r+1, c), mergedArea, type);
		mergedArea = mergeArea1(getArea(r, c+1), mergedArea, type);
		if (mergedArea == null) {
			mergedArea = makeNewArea(r,c);
			areaList.add(mergedArea);
		}
		mergedArea.add(r,c);
		if (isNumber(r,c))
			mergedArea.addNumber(getState(r,c));
		setArea(r, c, mergedArea);
	}

	private Area mergeArea1(Area area, Area mergedArea, int type) {
		if (area != null && area.getAreaType() == type) {
			if (mergedArea == null){
				mergedArea = area;
			} else if (mergedArea != area) {
				mergedArea.addAll(area);
				for (Iterator itr = area.iterator(); itr.hasNext(); ) {
					Address pos = (Address) itr.next();
					setArea(pos.r(), pos.c(), mergedArea);
					if (isNumber(pos.r(),pos.c()))
						mergedArea.addNumber(getState(pos.r(),pos.c()));
				}
				areaList.remove(area);
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
	void splitArea(int r, int c, int type) {
		Area oldArea = getArea(r,c);
		Area largerArea = null;
		areaList.remove(getArea(r,c));
		for (Iterator itr = getArea(r,c).iterator(); itr.hasNext(); ) {
			Address pos = (Address) itr.next();
			setArea(pos.r(), pos.c(), null);
		}
		if (isOn(r-1,c) && getSpaceOrWall(r-1,c)==type && getArea(r-1,c) == null) {
			initArea(r-1,c);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (isOn(r,c-1) && getSpaceOrWall(r,c-1)==type && getArea(r,c-1) == null) {
			initArea(r,c-1);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (isOn(r+1,c) && getSpaceOrWall(r+1,c)==type && getArea(r+1,c) == null) {
			initArea(r+1,c);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (isOn(r,c+1) && getSpaceOrWall(r,c+1)==type && getArea(r,c+1) == null) {
			initArea(r,c+1);
			if (largerArea == null || initializingArea.size() > largerArea.size())
				largerArea = initializingArea;
		}
		if (largerArea != null) {
			largerArea.setID(oldArea.getID());
		}
	}
	/**
	 * �����ɍ��W��^�����}�X���܂� Area ��V�����쐬����
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return �쐬�����̈�
	 */
	Area makeNewArea(int r, int c) {
		if (isWall(r,c)) {
			return new Area(WALL);
		} else if (isSpaceOrNumber(r,c)) {
			return new Area(SPACE);
		} else {
			return null;
		}
	}
	/**
	 * ���̃}�X��2��2�̍��}�X�u���b�N�̈�p���ǂ����𒲂ׂ�
	 * @param r
	 * @param c
	 * @return 2x2�u���b�N�Ȃ�� true
	 */
	boolean is2x2Block(int r, int c) {
		if (isWall(r,c)) {
			if (isWall(r-1,c)) {
				if (isWall(r,c-1)) {
					if (isWall(r-1,c-1)) {
						return true;
					}
				}
				if (isWall(r,c+1)) {
					if (isWall(r-1,c+1)) {
						return true;
					}
				}
			}
			if (isWall(r+1,c)) {
				if (isWall(r,c-1)) {
					if (isWall(r+1,c-1)) {
						return true;
					}
				}
				if (isWall(r,c+1)) {
					if (isWall(r+1,c+1)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r=rows()-1; r>=0; r--) {
			for (int c=cols()-1; c>=0; c--) {
				if (isUnknown(r,c)) {
					result |= 1;
				}
				if (is2x2Block(r,c)) {
					result |= 64;
				}
			}
		}
		result |= checkAreas();
		return result;
	}

	private int checkAreas() {
		int ret = 0;
		int blackAreaCount = 0;
		Area area;
		int number;
		for (Iterator itr = areaList.iterator(); itr.hasNext(); ) {
			area = (Area) itr.next();
			number = area.getNumber();
			if (area.getAreaType() == SPACE) {
				if (number == 0) {
					ret |= 16;
				} else if (number == Area.MULTIPLE_NUMBER) {
					ret |= 8;
				} else if (number == UNDECIDED_NUMBER) {
					;
				} else if (number > 0) {
					if (area.size() < number) {
						ret |= 4;
					} else if (area.size() > number) {
						ret |= 2;
					}
				}
			} else if (area.getAreaType() == WALL) {
				blackAreaCount ++;
			}
		}
		if (blackAreaCount > 1) {
			ret |= 32;
		}
		return ret;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		if (result==1)
			return "�󔒃}�X������\n";
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append("�������ʐς̑傫���V�}������\n"); 
		if ((result & 4) == 4)
			message.append("�������ʐς̏������V�}������\n"); 
		if ((result & 8) == 8)
			message.append("�����𕡐��܂ރV�}������\n"); 
		if ((result & 16) == 16)
			message.append("�������܂܂Ȃ��V�}������\n"); 
		if ((result & 32) == 32)
			message.append("���}�X�������ɕ��f����Ă���\n"); 
		if ((result & 64) == 64)
			message.append("���}�X���Q���Q�}�X�̂����܂�ɂȂ��Ă���\n"); 
		return message.toString();
	}

	/**
	 * �P��̑����\���N���X
	 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
	 */
	 class Step extends AbstractUndoableEdit {
	
		private int row;
		private int col;
		private int before;
		private int after;
		/**
		 * �R���X�g���N�^
		 * @param r �ύX���ꂽ�}�X�̍s���W
		 * @param c �ύX���ꂽ�}�X�̗���W
		 * @param b �ύX�O�̏��
		 * @param a �ύX��̏��
		 */
		public Step(int r, int c, int b, int a) {
			super();
			row = r;
			col = c;
			before = b;
			after = a;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			changeState(row, col, before);
		}

		public void redo() throws CannotRedoException {
			super.redo();
			changeState(row, col, after);
		}
	}

}
