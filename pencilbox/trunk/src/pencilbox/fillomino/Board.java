package pencilbox.fillomino;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.SideAddress;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 *  �u�t�B���I�~�m�v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int BLANK = 0;
	static final int UNKNOWN = 0;
	static final int UNDETERMINED = -2;
	static final int LINE = 1;
	static final int NOLINE = 0;

	private int[][] state; // �𓚂̐���
	private int[][] number; // ���̐���
	private int[][][] edge;

	private Area[][] area;
	private List<Area> areaList;
	private Area initializingArea;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		edge = new int[2][][];
		edge[0] = new int[rows()][cols() - 1];
		edge[1] = new int[rows() - 1][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (!isStable(p))
				setState(p, 0);
		}
		for (SideAddress p : borderAddrs()) {
			setEdge(p, NOLINE);
		}
		initBoard();
	}

	public void trimAnswer() {
		for (SideAddress p : borderAddrs()) {
			if (getEdge(p) == LINE) {
				changeEdge(p, NOLINE);
			}
		}
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
		return number[r][c] != Board.BLANK;
	}

	public boolean isStable(Address p) {
		return isStable(p.r(), p.c());
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
	 * Get number of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the number.
	 */
	public int getNumber(int r, int c ) {
		return number[r][c];
	}

	public int getNumber(Address p) {
		return getNumber(p.r(), p.c());
	}

	public int getNumberOrState(Address p) {
		return isStable(p) ? getNumber(p) : getState(p);
	}
	/**
	 * Set number to  a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @param n The number to set.
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address p, int n) {
		setNumber(p.r(), p.c(), n);
	}

	public int getEdge(SideAddress p) {
		return edge[p.d()][p.r()][p.c()];
	}

	public void setEdge(SideAddress p, int n) {
		edge[p.d()][p.r()][p.c()] = n;
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
		for (Address p : cellAddrs()) {
			if (getNumberOrState(p) > 0 && getArea(p) == null) {
				initArea(p);
			}
		}
	}
	/**
	 * �w�肵���}�X���N�_�Ƃ��ă}�X�̂Ȃ���𒲂ׂ�Area���쐬����
	 * @param p
	 */
	void initArea(Address p) {
		initializingArea = new Area(getNumberOrState(p));
		initArea1(p);
		areaList.add(initializingArea);
	}

	private void initArea1(Address p) {
		if (!isOn(p))
			return;
		if (getArea(p) == initializingArea)
			return;
		if (getNumberOrState(p) != initializingArea.getNumber())
			return;
		initializingArea.add(p);
		setArea(p, initializingArea);
		for (int d=0; d<4; d++) {
			initArea1(p.nextCell(d));
		}
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param p coordinate of the cell.
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
	 * @param p coordinate of the cell.
	 * @param a The area to set.
	 */
	public void setArea(Address p, Area a) {
		area[p.r()][p.c()] = a;
	}
	/**
	 * �}�X�ɉ𓚐�������͂��C�A�h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param n ���͂��鐔��
	 */
	public void changeAnswerNumber(Address p, int n) {
		int prev = getState(p);
		if (n == prev) 
			return;
		if (isStable(p)) {
			changeFixedNumber(p, 0);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.NUMBER, p, prev, n));
		setState(p, n);
		changeNumber1(p, prev, n);
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
		if (getState(p) > 0) {
			changeAnswerNumber(p, 0);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
		changeNumber1(p, prev, n);
	}

	private void changeNumber1(Address p, int prev, int n) {
		if (prev>0) {
			splitArea(p, prev);
		}
		if (n>0) {
			mergeArea(p, n);
		}
	}

	/**
	 * @param p
	 * @param st
	 */
	public void changeEdge(SideAddress p, int st) {
		int prev = getEdge(p);
		if (prev == st)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new BorderEditStep(p, prev, st));
		}
		setEdge(p, st);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getBefore());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getBefore());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getAfter());
			} else if (s.getType() == EditType.FIXED) {
				changeFixedNumber(s.getPos(), s.getAfter());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getAfter());
		}
	}

	/**
	 * ������ݒ�C�ύX�����Ƃ��� Area �����������s��
	 * @param p �ύX�����}�X�̍��W
	 * @param number �ύX��̐���
	 */
	void mergeArea(Address p, int number) {
		Area mergedArea = null;
		for (int d=0; d<4; d++) {
			Area a = getArea(p.nextCell(d));
			if (a != null && a.getNumber() == number) {
				if (mergedArea == null){
					mergedArea = a;
				} else if (mergedArea != a) {
					mergedArea.addAll(a);
					for (Address pos : a) {
						setArea(pos, mergedArea);
					}
					areaList.remove(area);
				}
			}
		}
		if (mergedArea == null) {
			mergedArea = new Area(number);
			areaList.add(mergedArea);
		}
		mergedArea.add(p);
		setArea(p, mergedArea);
	}
	/**
	 * ������ύX�C���������Ƃ��� Area �����������s��
	 * @param p0 �ύX�����}�X�̍��W
	 * @param number �ύX��̐���
	 */
	void splitArea(Address p0, int number) {
		areaList.remove(getArea(p0));
		for (Address p : getArea(p0)) {
			setArea(p, null);
		}
		for (int d=0; d<4; d++) {
			Address p = Address.nextCell(p0, d);
			if (isOn(p) && getNumberOrState(p)==number && getArea(p)==null)
				initArea(p);
		}
	}

	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumberOrState(p) <= 0)
				result |= 1;
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
			return Messages.getString("fillomino.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 2) == 2)
			message.append(Messages.getString("fillomino.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("fillomino.AnswerCheckMessage3")); //$NON-NLS-1$
		return message.toString();
	}
}
