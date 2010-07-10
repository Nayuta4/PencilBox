package pencilbox.hakyukoka;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;


/**
 * �u�g�y���ʁv�ՖʃN���X
 * �q���g�@�\��
 */
public class Board extends BoardBase {
	
	public static final int BLANK = 0;
	public static final int UNKNOWN = 0;
	public static final int UNDETERMINED = -2;
	
	private List<Area> areaList;
//	private int maxNumber = 9; // �ő吔��9�Ƃ���
	private int[][] state; // �𓚂̐���
	private int[][] number; // ���̐���
	private Area[][] area;

	private int[][] multi;  // �����s���ł̏d�������L�^����
	private int[][] multi2;  // ���̈���ł̏d�������L�^����
	private DigitPatternHint hint;       // �g�p�\�����p�^�[��

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		area = new Area[rows()][cols()];
		areaList = new LinkedList<Area>();
		multi = new int[rows()][cols()];
		multi2 = new int[rows()][cols()];
		hint = new DigitPatternHint();
		hint.setupHint(this);
	}

	public void clearBoard() {
		for (Address p : cellAddrs()) {
			setState(p, 0);
		}
		initBoard();
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

	public boolean isStable(Address pos) {
		return isStable(pos.r(), pos.c());
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

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
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
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * Get number of a cell.
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the number.
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}
	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
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
	
	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}

	public int getNumberOrState(Address p) {
		return isStable(p) ? getNumber(p) : getState(p);
	}
	/**
	 * ���̃}�X�̏�������̈���擾����
	 * ���̃}�X���̈�ɑ����Ă��Ȃ��ꍇ�� null ��Ԃ�
	 * @param r Row coordinate of the cell.
	 * @param c Column coordinate of the cell.
	 * @return Returns the area.
	 */
	public Area getArea(int r, int c ) {
		return area[r][c];
	}
	
	public Area getArea(Address pos) {
		return getArea(pos.r(), pos.c());
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

	public void setArea(Address pos, Area a) {
		setArea(pos.r(), pos.c(), a);
	}
	/**
	 * �z�u���������̏ꍇ true ��Ԃ�
	 * ���̐����̋����ȓ��ɓ������������邩�C
	 * �̈���ɓ������������邩�C
	 * �̈�̃}�X�������傫�������ł���΁Ctrue��Ԃ�
	 * @param p �}�X�̍��W
	 * @return ���̃}�X�̐��������ł���� true ��Ԃ�
	 */
	public boolean isError(Address p) {
		return isTooClose(p) || isMultipleNumber(p) || isTooLarge(p);
	}
	/**
	 * ���̃}�X����C�}�X�̐��������ȓ��ɓ������������邩�ǂ����𒲂ׂ�
	 * @param p �}�X�̍��W
	 * @return �}�X�̐��������ȓ��ɓ�������������ꍇ true ��Ԃ�
	 */
	public boolean isTooClose(Address p) {
		return multi[p.r()][p.c()] > 1;
	}
	/**
	 * ���̃}�X�Ɠ����̈���ɓ������������邩�ǂ����𒲂ׂ�
	 * @param p �}�X�̍��W
	 * @return �̈���ɓ�������������ꍇ true ��Ԃ�
	 */
	public boolean isMultipleNumber(Address p) {
		return multi2[p.r()][p.c()] > 1;
	}
	/**
	 * ���̃}�X�̐������C�̈�ʐς𒴂��Ă��Ȃ����ǂ����𒲂ׂ�
	 * @param p �}�X�̍��W
	 * @return �̈�ʐς𒴂��������̏ꍇ true ��Ԃ�
	 */
	public boolean isTooLarge(Address p) {
		return getArea(p)!=null && getNumberOrState(p) > getArea(p).size() ;
	}
	/**
	 * �����̍��W�̉\�����̃r�b�g�p�^�[�����擾
	 * @param r row coordinate
	 * @param c column coordinate
	 * @return Returns the pattern.
	 */
	int getPattern(Address p){
		return hint.getPattern(p);
	}
	/**
	 * ���̏ꏊ�ɂ��鐔�������[���Ɉᔽ�����ɔz�u�\���ǂ����𒲂ׂ�
	 * @param p �}�X�̍��W
	 * @param n �z�u�ł��邩�ǂ����𒲂ׂ鐔��
	 * @return �z�u�\�Ȃ� true�@��Ԃ�
	 */
	boolean canPlace(Address p, int n) {
		return hint.canPlace(p, n);
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
			changeFixedNumber(p, Board.BLANK);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellNumberEditStep(p, prev, n));
		updateMulti(p, n);
		if (getArea(p) != null)
			updateMulti2(p, n);
		setState(p, n);
		if (prev == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
//		changeNumber1(p, prev, n);
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
		if (getState(p) != Board.UNKNOWN) {
			changeAnswerNumber(p, Board.UNKNOWN);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, n));
		updateMulti(p, n);
		if (getArea(p) != null)
			updateMulti2(p, n);
		setNumber(p, n);
		if (prev == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
//		changeNumber1(p, prev, n);
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellNumberEditStep) {
			CellNumberEditStep s = (CellNumberEditStep) step;
			changeAnswerNumber(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeFixedNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellNumberEditStep) {
			CellNumberEditStep s = (CellNumberEditStep) step;
			changeAnswerNumber(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeFixedNumber(s.getPos(), s.getAfter());
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
	 * �̈���폜����
	 * @param oldArea
	 */
	public void removeArea(Area oldArea) {
		for (Address p : oldArea) {
			if (getArea(p) == oldArea)
				setArea(p, null);
		}
		areaList.remove(oldArea);
	}
	/**
	 * �}�X��̈�ɒǉ�����
	 * @param r �ǉ�����}�X�̍s���W
	 * @param c �ǉ�����}�X�̗���W
	 * @param area �ǉ������̈�
	 */
	public void addCellToArea(int r, int c, Area area) {
		if (area.isEmpty()) {
			areaList.add(area);
		}
		setArea(r, c, area);
		area.add(r, c);
//		initArea(area);
	}

	public void addCellToArea(Address pos, Area area) {
		addCellToArea(pos.r(), pos.c(), area);
	}
	/**
	 * �}�X��̈悩���菜��
	 * @param r ��菜���}�X�̍s���W
	 * @param c ��菜���}�X�̗���W
	 * @param area ��菜�����̈�
	 */
	public void removeCellFromArea(int r, int c, Area area) {
		setArea(r, c, null);
		area.remove(r, c);
		if (area.isEmpty()) {
			areaList.remove(area);
		} else {
//			initArea(area);
		}
	}

	public void removeCellFromArea(Address pos, Area area) {
		removeCellFromArea(pos.r(), pos.c(), area);
	}
	/**
	 * @return Returns the areaList.
	 */
	List<Area> getAreaList() {
		return areaList;
	}

	/**
	 * @return Returns the number.
	 */
	int[][] getNumber() {
		return number;
	}
	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}

	public void initBoard() {
		initMulti();
		initMulti2();
		hint.initHint();
	}
	/**
	 * multi[][] ������
	 */
	void initMulti() {
		for (Address p : cellAddrs()) {
			if(getNumber(p)>0)
				initMulti1(p,getNumber(p));
		}
	}

	private void initMulti1(Address p0, int num) {
		int r0=p0.r(), c0=p0.c();
		multi[r0][c0] = 1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			for (int k=1; k<=num; k++) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (getNumber(p) == num) {
					multi[r0][c0]++;
				}
			}
		}
	}

	/**
	 * �}�X�̐������ύX���ꂽ�Ƃ��ɁC����ɉ����ċ������̏d������\��multi�z����X�V����
	 * @param p0 �����̕ύX���ꂽ�}�X�̍��W
	 * @param num �ύX��̐���
	 */
	void updateMulti(Address p0, int num) {
		int r0 = p0.r();
		int c0 = p0.c();
		int prev = getNumberOrState(p0);
		if (multi[r0][c0]>1) {
			for (int d=0; d<4; d++) {
				Address p = p0;
				for (int k=1; k<=prev; k++) {
					p = Address.nextCell(p, d);
					if (!isOn(p))
						break;
					if (getNumber(p) == prev) {
						multi[p.r()][p.c()]--;
					}
				}
			}
		}
		if (num==0)
			multi[r0][c0]=0;
		else if (num>0) {
			multi[r0][c0] = 1;
			for (int d=0; d<4; d++) {
				Address p = p0;
				for (int k=1; k<=num; k++) {
					p = Address.nextCell(p, d);
					if (!isOn(p))
						break;
					if (getNumber(p) == num) {
						multi[p.r()][p.c()]++;
						multi[p0.r()][p0.c()]++;
					}
				}
			}
		}
//			printMulti();
	}
	void initMulti2() {
		for (Address p : cellAddrs()) {
			if(getNumber(p)>0 && getArea(p)!=null)
				initMulti21(p,getNumberOrState(p));
		}
	}

	private void initMulti21(Address p0, int num) {
		multi2[p0.r()][p0.r()] = 1;
		for (Address p : getArea(p0)) {
			if (p.equals(p0))
				continue;
			if (getNumber(p) == num) {
				multi2[p0.r()][p0.r()]++;
			}
		}
	}
	/**
	 * �}�X�̐������ύX���ꂽ�Ƃ��ɁC����ɉ����ė̈���̏d������\��multi2�z����X�V����
	 * @param p0 �����̕ύX���ꂽ�}�X�̍s���W
	 * @param num �ύX��̐���
	 */
	void updateMulti2(Address p0, int num) {
		int r0=p0.r();
		int c0=p0.c();
		int prevNum = getNumber(p0);
		if (multi2[r0][c0]>1) {
			for (Address p : getArea(p0)) {
				if (p.equals(p0))
					continue;
				if (getNumber(p) == prevNum) {
					multi2[p.r()][p.c()]--;
				}
			}
		}
		if (num==0)
			multi2[r0][c0]=0;
		else if (num>0) {
			multi2[r0][c0] = 1;
			for (Address p : getArea(p0)) {
				if (p.equals(p0))
					continue;
				if (getNumber(p) == num) {
					multi2[p.r()][p.c()]++;
					multi2[r0][c0]++;
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumberOrState(p) > 0 && isMultipleNumber(p))
				result |= 2;
			if (isTooLarge(p))
				result |= 4;
			if (isTooClose(p))
				result |= 8;
			if (getNumberOrState(p) <= 0)
				result |= 1;
		}
		return result;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return COMPLETE_MESSAGE;
		if (result==1)
			return Messages.getString("hakyukoka.AnswerCheckMessage1"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result&2) == 2)
			message.append(Messages.getString("hakyukoka.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result&4) == 4)
			message.append(Messages.getString("hakyukoka.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result&8) == 8)
			message.append(Messages.getString("hakyukoka.AnswerCheckMessage4")); //$NON-NLS-1$
		return message.toString();
	}
}
