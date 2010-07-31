package pencilbox.hakyukoka;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.AreaEditStep;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.AbstractStep.EditType;
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
	public Area getArea(int r, int c) {
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
			changeAnswerNumber(p, Board.UNKNOWN);
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		setNumber(p, n);
		changeNumber1(p, prev, n);
	}

	private void changeNumber1(Address p, int prev, int n) {
		updateMulti(p, prev, n);
		if (getArea(p) != null)
			updateMulti2(p, prev, n);
		if (prev == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getBefore());
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
			if (s.getType() == EditType.NUMBER) {
				changeAnswerNumber(s.getPos(), s.getAfter());
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
	 * ���݂̔Ֆʂɂ��ċ������̏d������\��multi�z������߂�
	 */
	void initMulti() {
		for (Address p : cellAddrs()) {
			int n = getNumberOrState(p);
			if (n > 0) {
				multi[p.r()][p.c()] = 1;
				updateMulti1(p, n, +1, 0);
			} else {
				multi[p.r()][p.c()] = 0;
			}
		}
	}

	/**
	 * �}�X�̐������ύX���ꂽ�Ƃ��ɁC����ɉ����ċ������̏d������\��multi�z����X�V����
	 * @param p0 �����̕ύX���ꂽ�}�X�̍��W
	 * @param prev �ύX�O�̐���
	 * @param num �ύX��̐���
	 */
	void updateMulti(Address p0, int prev, int num) {
		int r0 = p0.r(), c0 = p0.c();
		if (multi[r0][c0]>1) {
			updateMulti1(p0, prev, 0, -1);
		}
		if (num > 0) {
			multi[r0][c0] = 1;
			updateMulti1(p0, num, +1, +1);
		} else if (num <= 0) {
			multi[r0][c0]=0;
		}
	}
	/**
	 * p0�̐����̕ύX�ɉ����ďd�����𐔂���multi[][]�z����X�V����
	 * �͈͂̐��������āCnum �Ɠ��������̃}�X����������p0�̒�������m, ���̃}�X�̒�������k�ύX����B
	 * @param p0 ��Ԃ�ύX�����}�X�̍��W
	 * @param num ���ׂ鐔��
	 * @param m �����̏d�����X�V��
	 * @param k ����̏d�����X�V��
	 */
	private void updateMulti1(Address p0, int num, int m, int k) {
		for (int d=0; d<4; d++) {
			Address p = p0;
			for (int l=1; l<=num; l++) {
				p = Address.nextCell(p, d);
				if (!isOn(p))
					break;
				if (getNumberOrState(p) == num) {
					multi[p.r()][p.c()] += k;
					multi[p0.r()][p0.c()] += m;
				}
			}
		}
	}
	/**
	 * ���݂̔Ֆʂ̗̈���̏d������\��multi2�z������߂�B
	 */
	void initMulti2() {
		for (Address p : cellAddrs()) {
			int n = getNumberOrState(p);
			if (n>0 && getArea(p)!=null) {
				multi2[p.r()][p.r()] = 1;
				updateMulti21(p, n, +1, 0);
			} else {
				multi2[p.r()][p.c()] = 0;
			}
		}
	}
	/**
	 * �}�X�̐������ύX���ꂽ�Ƃ��ɁC����ɉ����ė̈���̏d������\��multi2�z����X�V����
	 * @param p0 �����̕ύX���ꂽ�}�X�̍s���W
	 * @param prev �ύX�O�̐���
	 * @param num �ύX��̐���
	 */
	void updateMulti2(Address p0, int prev, int num) {
		int r0=p0.r(), c0=p0.c();
		if (multi2[r0][c0]>1) {
			updateMulti21(p0, prev, 0, -1);
		}
		if (num > 0) {
			multi2[r0][c0] = 1;
			updateMulti21(p0, num, +1, +1);
		} else if (num <= 0) {
			multi2[r0][c0]=0;
		}
	}
	/**
	 * p0�̐����̕ύX�ɉ����ďd�����𐔂���multi2[][]�z����X�V����
	 * �͈͂̐��������āCnum �Ɠ��������̃}�X����������p0�̒�������m, ���̃}�X�̒�������k�ύX����B
	 * @param p0 ��Ԃ�ύX�����}�X�̍��W
	 * @param num ���ׂ鐔��
	 * @param m �����̏d�����X�V��
	 * @param k ����̏d�����X�V��
	 */
	private void updateMulti21(Address p0, int num, int m, int k) {
		for (Address p : getArea(p0)) {
			if (p.equals(p0))
				continue;
			if (getNumberOrState(p) == num) { // �ύX��̐����Ɠ�����������������C
				multi2[p.r()][p.c()] += k; // �ύX��̐����Ɠ�����������������C
				multi2[p0.r()][p0.c()] += m; // �ύX���ꂽ�}�X�̏d������m���₷
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
