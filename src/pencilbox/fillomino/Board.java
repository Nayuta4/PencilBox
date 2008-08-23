package pencilbox.fillomino;

import java.util.LinkedList;
import java.util.List;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.util.ArrayUtil;


/**
 *  �u�t�B���I�~�m�v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;

	private int[][] state; // ���̐���:1, �𓚂��ׂ�����:0,
	private int[][] number;
	
	private Area[][] area;
	private List<Area> areaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (!isStable(r,c))
					number[r][c] = 0;
			}
		}
		initBoard();
	}

	/**
	 * @return the number
	 */
	int[][] getNumber() {
		return number;
	}

	/**
	 * @return the state
	 */
	int[][] getState() {
		return state;
	}

	/**
	 * ���̃}�X�͖��Ƃ��Đ�����^����ꂽ�}�X���ǂ���
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return ��萔���̃}�X�Ȃ� true, �𓚂��ׂ��}�X�Ȃ� false
	 */
	public boolean isStable(int r, int c) {
		return state[r][c] == STABLE;
	}
	
	public boolean isStable(Address pos) {
		return isStable(pos.r(), pos.c());
	}
	/**
	 * Get state of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the state.
	 */
	public int getState(int r, int c) {
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * Set state to a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param st The state to set.
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * Get number of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns the number.
	 */
	public int getNumber(int r, int c ) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �}�X�ɐ����������Ă��Ȃ����ǂ���
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @return Returns true if the cell is empty.
	 */
	public boolean isUnknown(int r, int c) {
		return number[r][c] == 0;
	}

	public void initBoard() {
		initAreas();
	}
	/**
	 * ���݂̔Ֆʏ�ԂɊ�Â��āC�̈�̐ݒ���s��
	 */
	public void initAreas() {
		ArrayUtil.initArrayObject2(area, null);
		areaList.clear();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getNumber(r, c) > 0 && area[r][c] == null) {
					initArea(r,c);
				}
			}
		}
	}
	/**
	 * �w�肵���}�X���N�_�Ƃ��ă}�X�̂Ȃ���𒲂ׂ�Area���쐬����
	 * @param r
	 * @param c
	 */
	void initArea(int r, int c) {
		initializingArea = new Area(getNumber(r,c));
		initArea1(r, c);
		areaList.add(initializingArea);
	}

	private void initArea1(int r, int c) {
		if (!isOn(r, c))
			return;
		if (getArea(r, c) == initializingArea)
			return;
		if (getNumber(r, c) != initializingArea.getNumber())
			return;
		initializingArea.add(r, c);
		setArea(r, c, initializingArea);
		initArea1(r - 1, c);
		initArea1(r, c - 1);
		initArea1(r + 1, c);
		initArea1(r, c + 1);
		return;
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
	public void setArea(int r, int c,  Area a) {
		area[r][c] = a;
	}
	
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Colmun coordinate of the cell.
	 * @param n The number to set.
	 */
	public void changeNumber(int r, int c, int n) {
		int prevNum = getNumber(r, c);
		setNumber(r, c, n);
		if (prevNum>0) {
			splitArea(r, c, prevNum);
		}
		if (n>0) {
			mergeArea(r, c, n);
		}
	}
	
	public void changeNumber(Address pos, int n) {
		changeNumber(pos.r(), pos.c(), n);
	}
	/**
	 * �}�X�ɐ�������͂��C�A�h�D���X�i�[�ɒʒm����
	 * @param pos �}�X���W
	 * @param n ���͂��鐔��
	 */
	public void enterNumberA(Address pos, int n) {
		if (n < 0)
			return;
		if (n == getNumber(pos)) 
			return;
		fireUndoableEditUpdate(
			new UndoableEditEvent(this, new Step(pos.r(), pos.c(), getNumber(pos), n)));
		changeNumber(pos, n);
	}

	/**
	 * ������ݒ�C�ύX�����Ƃ��� Area �����������s��
	 * @param r �ύX�����}�X�̍s���W
	 * @param c �ύX�����}�X�̗���W
	 * @param number �ύX��̐���
	 */
	void mergeArea(int r, int c, int number) {
		Area mergedArea = null;
		mergedArea = mergeArea1(getArea(r-1, c), mergedArea, number);
		mergedArea = mergeArea1(getArea(r, c-1), mergedArea, number);
		mergedArea = mergeArea1(getArea(r+1, c), mergedArea, number);
		mergedArea = mergeArea1(getArea(r, c+1), mergedArea, number);
		if (mergedArea == null) {
			mergedArea = new Area(number);
			areaList.add(mergedArea);
		}
		mergedArea.add(r,c);
		setArea(r, c, mergedArea);
	}
	private Area mergeArea1(Area area, Area mergedArea, int number) {
		if (area != null && area.getNumber() == number) {
			if (mergedArea == null){
				mergedArea = area;
			} else if (mergedArea != area) {
				mergedArea.addAll(area);
				for (Address pos : area) {
					setArea(pos.r(), pos.c(), mergedArea);
				}
				areaList.remove(area);
			}
		}
		return mergedArea;
	}
	/**
	 * ������ύX�C���������Ƃ��� Area �����������s��
	 * @param r �ύX�����}�X�̍s���W
	 * @param c �ύX�����}�X�̗���W
	 * @param number �ύX��̐���
	 */
	void splitArea(int r, int c, int number) {
		areaList.remove(getArea(r, c));
		for (Address pos : getArea(r, c)) {
			setArea(pos.r(), pos.c(), null);
		}
		if (isOn(r-1,c) && getNumber(r-1,c)==number && getArea(r-1,c) == null)
			initArea(r-1,c);
		if (isOn(r,c-1) && getNumber(r,c-1)==number && getArea(r,c-1) == null)
			initArea(r,c-1);
		if (isOn(r+1,c) && getNumber(r+1,c)==number && getArea(r+1,c) == null)
			initArea(r+1,c);
		if (isOn(r,c+1) && getNumber(r,c+1)==number && getArea(r,c+1) == null)
			initArea(r,c+1);
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++){
				if (isUnknown(r,c))
					result |= 1;
			}
		}
		for (Area area : areaList) {
			int status = area.getStatus();
			if (status == -1) result |= 2;
			else if (status == 0) result |= 4;
		}
		return result;
	}
	
	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		if (result == 1)
			return Messages.getString("Board.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append(Messages.getString("Board.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("Board.AnswerCheckMessage3")); //$NON-NLS-1$
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
			if (isStable(row, col))
				return;
			changeNumber(row,col,before);
		}
		public void redo() throws CannotRedoException {
			super.redo();
			if (isStable(row, col))
				return;
			changeNumber(row, col, after);
		}
		public boolean addEdit(UndoableEdit anEdit) {
			Step edit = (Step) anEdit;
			if (edit.row == row && edit.col == col) {
				after = edit.after;
				return true;
			} else {
				return false;
			}
		}
	
	}

}
