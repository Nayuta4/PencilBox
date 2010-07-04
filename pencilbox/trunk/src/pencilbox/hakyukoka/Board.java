package pencilbox.hakyukoka;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellNumberEditStep;
import pencilbox.resource.Messages;


/**
 * �u�g�y���ʁv�ՖʃN���X
 * �q���g�@�\��
 */
public class Board extends BoardBase {
	
	static final int UNSTABLE = 0;
	static final int STABLE = 1;
	static final int UNKNOWN = 0;
	static final int UNDETERMINED = -2;
	
	private List<Area> areaList;
//	private int maxNumber = 9; // �ő吔��9�Ƃ���
	private int[][] state; // ���̐���:1, �𓚂��ׂ�����:0,
	private int[][] number;
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
			if (!isStable(p))
				setNumber(p, 0);
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
		return state[r][c] == STABLE;
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
	public int getNumber(int r, int c ) {
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
	public void setArea(int r, int c,  Area a) {
		area[r][c] = a;
	}

	public void setArea(Address pos,  Area a) {
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
		return getArea(p)!=null && getNumber(p) > getArea(p).size() ;
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
	 * �}�X�ɐ�������͂��C�A�h�D���X�i�[�ɒʒm����
	 * @param p �}�X���W
	 * @param n ���͂��鐔��
	 */
	public void changeNumber(Address p, int n) {
		if (n < 0) 
			return;
		if (n == getNumber(p)) 
			return;
		fireUndoableEditUpdate(
			new CellNumberEditStep(p, getNumber(p), n));
		changeNumber1(p, n);
	}

	private void changeNumber1(Address p, int n) {
		int prevNum = getNumber(p);
		updateMulti(p, n);
		if (getArea(p) != null)
			updateMulti2(p, n);
		setNumber(p, n);
		if (prevNum == 0 && n > 0)
			hint.checkUsedNumber(p, n);
		else
			hint.initHint();
	}
	
	public void changeFixedNumber(Address p, int n) {
		if (n == Board.UNKNOWN)
			setState(p, Board.UNSTABLE);
		else
			setState(p, Board.STABLE);
		if (n == Board.UNDETERMINED)
			n = 0;
		changeNumber1(p, n);
	}

	public void undo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		if (isStable(s.getPos()))
			return;
		changeNumber(s.getPos(), s.getBefore());
	}

	public void redo(AbstractStep step) {
		CellNumberEditStep s = (CellNumberEditStep) step;
		if (isStable(s.getPos()))
			return;
		changeNumber(s.getPos(), s.getAfter());
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
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if(getNumber(r,c)>0)
					initMulti1(r,c,getNumber(r,c));
			}
		}
	}

	private void initMulti1(int r0, int c0, int num) {
		multi[r0][c0] = 1;
		for (int c = c0-num; c <= c0+num; c++) {
			if (c==c0 || !isOn(r0,c)) continue;
			if (getNumber(r0,c) == num) {
				multi[r0][c0]++;
			}
		}
		for (int r = r0-num; r <= r0+num; r++) {
			if (r==r0 || !isOn(r,c0)) continue;
			if (getNumber(r,c0) == num) {
				multi[r0][c0]++;
			}
		}
	}

	/**
	 * �}�X�̐������ύX���ꂽ�Ƃ��ɁC����ɉ����ċ������̏d������\��multi�z����X�V����
	 * @param p0 �����̕ύX���ꂽ�}�X�̍s�W
	 * @param num �ύX��̐���
	 */
	void updateMulti(Address p0, int num) {
		int r0 = p0.r();
		int c0 = p0.c();
		int prevNum = getNumber(r0, c0);
		if (multi[r0][c0]>1) {
			for (int c = c0-prevNum; c <= c0+prevNum; c++) {
				if (c==c0 || !isOn(r0,c))
					continue;
				if (getNumber(r0,c) == prevNum) {
					multi[r0][c]--;
				}
			}
			for (int r = r0-prevNum; r <= r0+prevNum; r++) {
				if (r==r0 || !isOn(r,c0))
					continue;
				if (getNumber(r,c0) == prevNum) {
					multi[r][c0]--;
				}
			}
		}
		if (num==0)
			multi[r0][c0]=0;
		else if (num>0) {
			multi[r0][c0] = 1;
			for (int c = c0-num; c <= c0+num; c++) {
				if (c==c0 || !isOn(r0,c))
					continue;
				if (getNumber(r0,c) == num) {
					multi[r0][c]++;
					multi[r0][c0]++;
				}
			}
			for (int r = r0-num; r <= r0+num; r++) {
				if (r==r0 || !isOn(r,c0))
					continue;
				if (getNumber(r,c0) == num) {
					multi[r][c0]++;
					multi[r0][c0]++;
				}
			}
		}
//			printMulti();
	}
	void initMulti2() {
		for (int r = rows() - 1; r >= 0; r--) {
			for (int c = cols() - 1; c >= 0; c--) {
				if(getNumber(r,c)>0 && getArea(r,c)!=null)
					initMulti21(r,c,getNumber(r,c));
			}
		}
	}
	private void initMulti21(int r0, int c0, int num) {
		multi2[r0][c0] = 1;
		for (Address pos : getArea(r0,c0)) {
			if (pos.equals(r0,c0))
				continue;
			if (getNumber(pos.r(),pos.c()) == num) {
				multi2[r0][c0]++;
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
		int prevNum = getNumber(r0, c0);
		if (multi2[r0][c0]>1) {
			for (Address pos : getArea(r0,c0)) {
				if (pos.equals(r0,c0))
					continue;
				if (getNumber(pos.r(),pos.c()) == prevNum) {
					multi2[pos.r()][pos.c()]--;
				}
			}
		}
		if (num==0)
			multi2[r0][c0]=0;
		else if (num>0) {
			multi2[r0][c0] = 1;
			for (Address pos : getArea(r0,c0)) {
				if (pos.equals(r0,c0))
					continue;
				if (getNumber(pos.r(),pos.c()) == num) {
					multi2[pos.r()][pos.c()]++;
					multi2[r0][c0]++;
				}
			}
		}
	}
	
	public int checkAnswerCode() {
		int result = 0;
		for (Address p : cellAddrs()) {
			if (getNumber(p) > 0 && isMultipleNumber(p))
				result |= 2;
			if (isTooLarge(p))
				result |= 4;
			if (isTooClose(p))
				result |= 8;
			if (getNumber(p) == 0)
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
