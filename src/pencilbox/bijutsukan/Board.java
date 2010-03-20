package pencilbox.bijutsukan;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * �u���p�فv�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int NONUMBER_WALL = 5;
	static final int BULB = -2;
	static final int NOBULB = -3;
	static final int UNKNOWN = -1;
	static final int OUTER = 6;

	private int[][] state;
	private int[][] illuminatedH;
	private int[][] illuminatedV;

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()]; 
		illuminatedV = new int[rows()][cols()];
		illuminatedH = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(state, UNKNOWN);
	}
	
	public void clearBoard() {
		super.clearBoard();
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isFloor(r, c)) {
					setState(r,c,UNKNOWN);
				}
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (getState(r, c) == NOBULB)
					setState(r, c, UNKNOWN);
				}
		}
	}

	public void initBoard() {
		initIlluminations();
	}
	/**
	 * ���݂̏Ɩ��z�u�����ƂɁCilluminatedV illuminatedH���Đݒ肷��
	 */
	void initIlluminations() {
		ArrayUtil.initArrayInt2(illuminatedV, 0);
		ArrayUtil.initArrayInt2(illuminatedH, 0);
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (isBulb(r,c)) {
					illuminate(r, c, true);
				}
			}
		}
	}
	/**
	 * @return Returns the state.
	 */
	int[][] getState() {
		return state;
	}
	/**
	 * �}�X�̏�Ԃ��擾���� 
	 * @param r �s���W
	 * @param c ����W
	 * @return ���
	 */
	public int getState(int r, int c) {
		if (isOn(r,c)) return state[r][c];
		else return OUTER;
	}
	
	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}
	/**
	 * �}�X�̏�Ԃ݂̂�ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param st ���
	 */
	public void setState(int r, int c, int st) {
		state[r][c] = st;
	}
	
	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}
	/**
	 * ���̃}�X���ǂ��ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �ǂȂ� true
	 */
	public boolean isWall(int r, int c){
		return state[r][c]>=0 && state[r][c]<=4 || state[r][c]==NONUMBER_WALL;
	}
	
	public boolean isWall(Address pos) {
		return isWall(pos.r(), pos.c());
	}
	/**
	 * ���̃}�X���������̕ǂ��ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �ǂȂ� true
	 */
	public boolean isNumberedWall(int r, int c){
		return state[r][c]>=0 && state[r][c]<=4;
	}
	/**
	 * ���̃}�X���ǂ̂Ȃ��}�X���ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �Փ��ł��ǂłȂ��Ȃ� true
	 */
	public boolean isFloor(int r, int c){
		return isOn(r,c) && (state[r][c] == UNKNOWN || state[r][c] == NOBULB || state[r][c] == BULB);
	}
	/**
	 * ���̃}�X�ɏƖ����u����Ă��邩�ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �Ɩ����u����Ă���Ȃ� true
	 */
	public boolean isBulb(int r, int c){
		return isOn(r,c) && state[r][c] == BULB;
	}
	/**
	 * ���̃}�X�̏Ɩ��z�u�����肩�ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return ����Ȃ� true
	 */
	public boolean isUnknown(int r, int c) {
		return state[r][c] == UNKNOWN;
	}
	/**
	 * ���̃}�X������������Ƃ炳��Ă��邩
	 * @param r �s���W
	 * @param c ����W
	 * @return ����Ȃ� true
	 */
	public int getHorizIlluminated(int r, int c) {
		return illuminatedH[r][c];
	}
	/**
	 * ���̃}�X���c��������Ƃ炳��Ă��邩
	 * @param r �s���W
	 * @param c ����W
	 * @return ����Ȃ� true
	 */
	public int getVertIlluminated(int r, int c) {
		return illuminatedV[r][c];
	}
	/**
	 * ���̃}�X������̕����̏Ɩ��ɂ��Ƃ炳��Ă��邩�H�H�H
	 * @param r �s���W
	 * @param c ����W
	 * @return ����Ȃ� true
	 */
	public boolean isMultiIlluminated(int r, int c) {
		return illuminatedV[r][c]>1 || illuminatedH[r][c] > 1;
	}
	/**
	 * �}�X���Ƃ炳��Ă��邩�C�܂肻�̃}�X�̏㉺���E�ɏƖ������邩�𒲂ׂ�
	 * @param r �s���W
	 * @param c ����W
	 * @return �Ƃ炳��Ă���� true
	 */
	public boolean isIlluminated(int r, int c) {
			return (illuminatedV[r][c] > 0 || illuminatedH[r][c] > 0);
	}
	/**
	 * �}�X�̏Ɩ��z�u���ύX���ꂽ�ꍇ�ɁC�㉺���E�̌������X�V����
	 * @param r0 �Ɩ��z�u���ύX���ꂽ�}�X�̍s���W
	 * @param c0 �Ɩ��z�u���ύX���ꂽ�}�X�̗���W
	 * @param on �Ɩ����z�u���ꂽ�Ƃ��ɂ� true, ��菜���ꂽ�Ƃ��� false 
	 */
	private void illuminate(int r0, int c0, boolean on) {
		int k = on ? 1 : -1;
		int r = r0;
		int c = c0;
		while (isFloor(r, c)) {
			r--;
		}
		r++;
		while (isFloor(r, c)) {
			illuminatedV[r][c] += k;
			r++;
		}
		r = r0;
		while(isFloor(r,c)) {
			c--;
		}
		c++;
		while(isFloor(r,c)) {
			illuminatedH[r][c] += k;
			c++;
		}
	}

	/**
	 * �}�X�̏㉺�S�����̖����肩��̏Ɩ���Ԃ����ׂčX�V����
	 * @param r0 ���S�}�X�̍s���W
	 * @param c0 ���S�}�X�̗���W
	 * @param on �����������Ƃ��� true, �����Ƃ��� false 
	 */
	private void illuminate4(int r0, int c0, boolean on) {
		Address p = new Address(r0, c0);
		for (int d = 0; d < 4; d++) {
			p.set(r0, c0);
			p.move(d);
			while (isFloor(p.r(), p.c())) {
				if (isBulb(p.r(), p.c()))
					illuminate(p.r(), p.c(), on);
				p.move(d);
			}
		}
	}

	/**
	 * �}�X�̏�Ԃ�ݒ肷��
	 * �}�X����̌������X�V����
	 * ���}�X�𑀍삵���Ƃ��́A�㉺�S�����̖����肩��̏Ɩ���Ԃ����ׂčX�V����
	 * @param r �s���W
	 * @param c ����W
	 * @param st ���
	 */
	public void changeState(int r, int c, int st) {
		int prev = getState(r, c);
		if ((st >= 0 && st <= 4 || st == NONUMBER_WALL)
				|| (prev >= 0 && prev <= 4 || prev == NONUMBER_WALL)) {
			illuminate4(r, c, false);
		}
		if (st == BULB && prev != BULB)
			illuminate(r, c, true);
		else if (prev == BULB && st != BULB)
			illuminate(r, c, false);
		setState(r, c, st);
		if ((st>=0 && st<=4 || st == NONUMBER_WALL)
				|| (prev>=0 && prev<=4 || prev == NONUMBER_WALL)) {
			illuminate4(r, c, true);
		}
	}

	public void changeState(Address pos, int st) {
		changeState(pos.r(), pos.c(), st);
	}
	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX���C�ύX���A���h�D���X�i�[�ɒʒm����
	 * @param pos �}�X���W
	 * @param st �ύX��̏��
	 */
	public void changeStateA(Address pos, int st) {
		fireUndoableEditUpdate(new Step(pos.r(), pos.c(), getState(pos), st));
		changeState(pos, st);
	}

	public void undo(AbstractStep step) {
		Step s = (Step) step;
		changeState(s.row, s.col, s.before);
	}

	public void redo(AbstractStep step) {
		Step s = (Step) step;
		changeState(s.row, s.col, s.after);
	}

	 /**
	 * �אڂ���S�}�X�̏Ɩ����𒲂ׂ�
	 * @param r �s���W
	 * @param c ����W
	 * @return �אڂ���S�}�X�̏Ɩ���
	 */
	public int countAdjacentBulbs(int r, int c) {
		int count = 0;
		if (isBulb(r-1,c)) count++;
		if (isBulb(r+1,c)) count++;
		if (isBulb(r,c-1)) count++;
		if (isBulb(r,c+1)) count++;
		return count;
	}
	/**
	 * �ǂɗאڂ���4�}�X�̏Ɩ��������������ǂ����𒲍�����
	 * @param r �s���W
	 * @param c ����W
	 * @return �Ɩ����������Ɠ������Ȃ�C1, 
	 * �Ɩ�������������Ȃ� -1, 
	 * �Ɩ�����������菬�����Ȃ� 0
	 */
	public int checkAdjacentBulbs(int r, int c) {
		int nBulb = countAdjacentBulbs(r,c);
		int number = getState(r,c);
		if (nBulb > number) {
			return -1;
		} else if (nBulb == number) {
			return 1;
		} else if (nBulb < number) {
			return 0;
		}
		return 0;
	}

	public int checkAnswerCode() {
		int result = 0;
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				if (!isWall(r,c)) {
					if (isMultiIlluminated(r,c)) {
						result |= 1;
					}
					else if (!isIlluminated(r,c)) {
						result |= 2;
					}
				}
				else if (isNumberedWall(r,c)) {
					if (countAdjacentBulbs(r,c) > getState(r,c)) {
						result |= 4;
					}
					else if (countAdjacentBulbs(r,c) < getState(r,c)) {
						result |= 8;
					}
				}
			}
		}
		return result;
	}
	
	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result==0)
			return BoardBase.COMPLETE_MESSAGE;
		StringBuffer message = new StringBuffer();
		if ((result&1) == 1)
			message.append(ERR_MULTI_ILLUMINATION);
		if ((result&2) == 2)
			message.append(YET_NOT_ILLUMINATED);
		if ((result&4) == 4 || (result&8) == 8)
			message.append(ERR_WRONG_NUMBER);
		return message.toString();
	}

	static final String ERR_MULTI_ILLUMINATION = Messages.getString("bijutsukan.AnswerCheckMessage1"); //$NON-NLS-1$
	static final String YET_NOT_ILLUMINATED = Messages.getString("bijutsukan.AnswerCheckMessage2"); //$NON-NLS-1$
	static final String ERR_WRONG_NUMBER =  Messages.getString("bijutsukan.AnswerCheckMessage3"); //$NON-NLS-1$
}
	
  /**
   * �P��̑����\���N���X
   * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
   */
   class Step extends AbstractStep {

	  int row;
	  int col;
	  int before;
	  int after;
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
	  
   }
