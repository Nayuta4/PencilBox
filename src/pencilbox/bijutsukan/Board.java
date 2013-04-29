package pencilbox.bijutsukan;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.Direction;
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

	public static boolean isWall(int st) {
		return (st >= 0 && st <= 4) || st == NONUMBER_WALL;
	}

	protected void setup() {
		super.setup();
		state = new int[rows()][cols()];
		illuminatedV = new int[rows()][cols()];
		illuminatedH = new int[rows()][cols()];
		ArrayUtil.initArrayInt2(state, UNKNOWN);
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (isFloor(p)) {
				setState(p, UNKNOWN);
			}
		}
		initBoard();
	}

	public void trimAnswer() {
		for (Address p : cellAddrs()) {
			if (getState(p) == NOBULB) {
				changeState(p, UNKNOWN);
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
		for (Address p : cellAddrs()) {
			if (getState(p) == Board.BULB) {
				illuminate(p, true);
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
	 * @param p �}�X���W
	 * @return �ǂȂ� true
	 */
	public boolean isWall(Address p) {
		int n = getState(p);
		return n>=0 && n<=4 || n==NONUMBER_WALL;
	}
	/**
	 * ���̃}�X���������̕ǂ��ǂ���
	 * @param p �}�X���W
	 * @return �ǂȂ� true
	 */
	public boolean isNumberedWall(Address p){
		int n = getState(p);
		return n>=0 && n<=4;
	}
	/**
	 * ���̃}�X���ǂ̂Ȃ��}�X���ǂ���
	 * @param p �}�X���W
	 * @return �Փ��ł��ǂłȂ��Ȃ� true
	 */
	public boolean isFloor(Address p){
		if (isOn(p)) {
			int n = getState(p);
			return n == UNKNOWN || n == NOBULB || n == BULB;
		} else {
			return false;
		}
	}
	/**
	 * ���̃}�X������������Ƃ炳��Ă��邩
	 * @param p �}�X���W
	 * @return ����Ȃ� true
	 */
	public int getHorizIlluminated(Address p) {
		return illuminatedH[p.r()][p.c()];
	}
	/**
	 * ���̃}�X���c��������Ƃ炳��Ă��邩
	 * @param p �}�X���W
	 * @return ����Ȃ� true
	 */
	public int getVertIlluminated(Address p) {
		return illuminatedV[p.r()][p.c()];
	}
	/**
	 * ���̃}�X������̕����̏Ɩ��ɂ��Ƃ炳��Ă��邩�H�H�H
	 * @param p �}�X���W
	 * @return ����Ȃ� true
	 */
	public boolean isMultiIlluminated(Address p) {
		return illuminatedV[p.r()][p.c()]>1 || illuminatedH[p.r()][p.c()] > 1;
	}
	/**
	 * �}�X���Ƃ炳��Ă��邩�C�܂肻�̃}�X�̏㉺���E�ɏƖ������邩�𒲂ׂ�
	 * @param p �}�X���W
	 * @return �Ƃ炳��Ă���� true
	 */
	public boolean isIlluminated(Address p) {
			return (illuminatedV[p.r()][p.c()] > 0 || illuminatedH[p.r()][p.c()] > 0);
	}
	/**
	 * �}�X�̏Ɩ��z�u���ύX���ꂽ�ꍇ�ɁC�㉺���E�̌������X�V����
	 * @param p0 �Ɩ��z�u���ύX���ꂽ�}�X�̍��W
	 * @param on �Ɩ����z�u���ꂽ�Ƃ��ɂ� true, ��菜���ꂽ�Ƃ��� false 
	 */
	private void illuminate(Address p0, boolean on) {
		int k = on ? 1 : -1;
		for (int d=0; d<4; d++) {
			Address p = p0;
			p = p0.nextCell(d);
			while (isFloor(p)) {
				increaseIlluminated(p, d, k);
				p = p.nextCell(d);
			}
		}
		increaseIlluminated(p0, 0, k);
		increaseIlluminated(p0, 1, k);
	}

	public void increaseIlluminated(Address p, int d, int k) {
		if ((d&1) == Direction.VERT) {
			illuminatedV[p.r()][p.c()] += k;
		} else if ((d&1) == Direction.HORIZ){
			illuminatedH[p.r()][p.c()] += k;
		}
	}

	/**
	 * �}�X�̍��}�X�z�u���ύX���ꂽ�ꍇ�ɁC�㉺���E�̏Ɩ�����̌������X�V����
	 * @param p0 ���}�X�z�u���ύX���ꂽ�}�X�̍��W
	 * @param on ���}�X�z�u���ꂽ�Ƃ��ɂ� false, ��菜���ꂽ�Ƃ��� true 
	 */
	private void illuminate4(Address p0, boolean on) {
		for (int d = 0; d < 4; d++) {
			Address p = p0.nextCell(d);
			while (isFloor(p)) {
				if (getState(p) == Board.BULB)
					illuminate(p, on);
				p = p.nextCell(d);
			}
		}
	}
	/**
	 * �}�X�̏�Ԃ��w�肵����ԂɕύX����
	 * �}�X����̌������X�V����
	 * ���}�X�𑀍삵���Ƃ��́A�㉺�S�����̖����肩��̏Ɩ���Ԃ����ׂčX�V����
	 * @param p �}�X���W
	 * @param st �ύX��̏��
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (st == prev)
			return;
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(p, prev, st));
		if (isWall(st) || isWall(prev)) {
			illuminate4(p, false);
		}
		if (prev == BULB && st != BULB) {
			illuminate(p, false);
		}
		setState(p, st);
		if (st == BULB && prev != BULB) {
			illuminate(p, true);
		}
		if (isWall(st) || isWall(prev)) {
			illuminate4(p, true);
		}
	}

	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeState(s.getPos(), s.getAfter());
		}
	}

	/**
	 * �אڂ���S�}�X�̏Ɩ����𒲂ׂ�
	 * @param p �}�X���W
	 * @return �אڂ���S�}�X�̏Ɩ���
	 */
	public int countAdjacentBulbs(Address p0) {
		int count = 0;
		for (int d=0; d<4; d++) {
			Address p = Address.nextCell(p0, d);
			if (getState(p) == Board.BULB) {
				count++;
			}
		}
		return count;
	}
	/**
	 * �ǂɗאڂ���4�}�X�̏Ɩ��������������ǂ����𒲍�����
	 * @param p �}�X���W
	 * @return �Ɩ����������Ɠ������Ȃ�C1, 
	 * �Ɩ�������������Ȃ� -1, 
	 * �Ɩ�����������菬�����Ȃ� 0
	 */
	public int checkAdjacentBulbs(Address p) {
		int nBulb = countAdjacentBulbs(p);
		int number = getState(p);
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
		for (Address p : cellAddrs()) {
			if (!isWall(p)) {
				if (isMultiIlluminated(p)) {
					result |= 1;
				}
				else if (!isIlluminated(p)) {
					result |= 2;
				}
			} else if (isNumberedWall(p)) {
				if (countAdjacentBulbs(p) > getState(p)) {
					result |= 4;
				}
				else if (countAdjacentBulbs(p) < getState(p)) {
					result |= 8;
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
