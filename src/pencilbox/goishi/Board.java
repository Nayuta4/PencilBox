package pencilbox.goishi;

import java.util.ArrayList;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;


/**
 *  �u��΂Ђ낢�v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int BLANK = 0;
	static final int STONE = -1;

	private int[][] state;
	private int[][] number;
	ArrayList<Address> pickedList;

	protected void setup(){
		super.setup();
		state = new int[rows()][cols()];
		number = new int[rows()][cols()];
		pickedList = new ArrayList<Address>();
	}

	public void clearBoard() {
		super.clearBoard();
		for (Address p : cellAddrs()) {
			if (getState(p) == STONE)
				setNumber(p, 0);
		}
		pickedList.clear();
//		initBoard();
	}

	/**
	 * ���̃}�X�͌�΂����邩
	 * @param r �}�X�̍s���W
	 * @param c �}�X�̗���W
	 * @return �΂�����}�X�Ȃ� true, �Ȃ��}�X�Ȃ� false
	 */
	public boolean isStone(Address pos) {
		return getState(pos) == STONE;
	}

	public int getState(int r, int c) {
		if (!isOn(r, c))
			return Board.BLANK;
		return state[r][c];
	}

	public int getState(Address pos) {
		return getState(pos.r(), pos.c());
	}

	public void setState(int r, int c, int st) {
		if (!isOn(r, c))
			return;
		state[r][c] = st;
	}

	public void setState(Address pos, int st) {
		setState(pos.r(), pos.c(), st);
	}

	public int getNumber(Address pos) {
		return number[pos.r()][pos.c()];
	}

	public void setNumber(Address pos, int n) {
		number[pos.r()][pos.c()] = n;
	}

	public void initBoard() {
//		rePickUpAll();
	}

	/**
	 * �΂�u������菜���B
	 * @param p ���W
	 * @param st �ύX��̏�� �u�������C��菜������
	 */
	public void changeState(Address p, int st) {
		int prev = getState(p);
		if (prev == st)
			return;
		if (st == STONE) {
			int m = checkRoute(p);
			if (m > 0) {
				placeBackFromN(m);
			}
		} else if (st == BLANK) {
			int m = getNumber(p);
			if (m > 0) {
				placeBackFromN(m);
			}
		}
		if (isRecordUndo())
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, getState(p), st));
		setState(p, st);
	}

	private void placeBackFromN(int m) {
		for (int i = pickedList.size(); i >= m; i--) {
			placeBack();
		}
	}

	/**
	 * �E���BUndoManger�ɒʒm����B
	 * @param p
	 */
	public void pickUp(Address p) {
		int n = pickedList.size();
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, 0, n+1));
		}
		pickedList.add(p);
		setNumber(p, n+1);
	}

	/**
	 * �߂��BUndoManger�ɒʒm����B
	 */
	public void placeBack() {
		int n = pickedList.size();
		Address p = pickedList.get(n-1);
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(EditType.STATE, p, n, 0));
		}
		pickedList.remove(n-1);
		setNumber(p, 0);
	}

	/**
	 * �E���邩
	 * �E���Ȃ��͈̂ȉ��̏ꍇ
	 * �΂��Ȃ�
	 * �΂����łɏE���Ă���
	 * �O�ɏE�����΂ƈ꒼����ɂȂ�
	 * �O�ɏE�����΂Ƃ̊ԂɏE���Ă��Ȃ��΂�����
	 * �O�ɏE�����΂Ƃ��̑O�ɏE�����΂Ƃɂ͂��܂�Ă���
	 * @param pos
	 * @return
	 */
	public boolean canPick(Address pos) {
		if (getState(pos) != STONE)
			return false;
		if (getNumber(pos) > 0)
			return false;
		if (pickedList.size() == 0)
			return true;
		Address prev = pickedList.get(pickedList.size() - 1);
		int direction = Address.getDirectionTo(prev, pos);
		if (direction < 0)
			return false;
		for (Address p = prev; !p.equals(pos); p = p.nextCell(direction)) {
			if (isStone(p) && getNumber(p) == 0)
				return false;
		}
		if (pickedList.size() == 1)
			return true;
		Address prev2 = pickedList.get(pickedList.size() - 2);
		int direction2 = Address.getDirectionTo(prev, prev2);
		if (direction == direction2)
			return false;
		return true;
	}

	/**
	 * �㉺���E�����ďE���Ȃ��Ȃ�悤�ȍŏ��̔ԍ���Ԃ�
	 * �E���Ȃ��Ȃ邱�Ƃ��Ȃ����0��Ԃ�
	 */
	private int checkRoute(Address p0) {
		int[] next4 = new int[4];
		for (int d = 0; d < 4; d++) {
			Address p = p0;
			while (isOn(p)) {
				p = p.nextCell(d);
				if (getState(p) == STONE) {
					int n = getNumber(p);
					if (n > 0)
						next4[d] = n;
					break;
				}
			}
		}
		int m1 = diff1(next4[0], next4[2]);
		int m2 = diff1(next4[1], next4[3]);
		int mm = min2(m1, m2);
		return mm;
	}
	/**
	 * 2�̐����̍���1�̏ꍇ�C���̑傫�����̒l��Ԃ�
	 */
	private int diff1(int a, int b) {
		if (a > 0 && b > 0) {
			if (a == b+1)
				return a;
			else if (b == a+1)
				return b;
		}
		return 0;
	}
	/**
	 * 2�̐����̂�����菬�������̒l��Ԃ��B�Е��݂̂������Ȃ炻�̒l��Ԃ�
	 */
	private int min2(int a, int b) {
		if (a > 0 && b > 0) {
			return (a > b) ? b : a;
		} else if (a > 0 && b == 0) {
			return a;
		} else if (b > 0 && a == 0) {
			return b;
		}
		return 0;
	}
	public void undo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.FIXED) {
				changeState(s.getPos(), s.getBefore());
			} else if (step.getType() == EditType.STATE) {
				if (s.getBefore() > 0)
					pickUp(s.getPos());
				else
					placeBack();
			}
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			if (step.getType() == EditType.FIXED) {
				changeState(s.getPos(), s.getAfter());
			} else if (step.getType() == EditType.STATE) {
				if (s.getAfter() > 0)
					pickUp(s.getPos());
				else
					placeBack();
			}
		}
	}

	public int checkAnswerCode() {
		int nStone = 0;
		for (Address p : cellAddrs()) {
			if (getState(p) == Board.STONE) {
				nStone ++;
				if (getNumber(p) == 0)
					return 1;
			}
		}
		if (nStone == 0)
			return 2;
		return 0;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		else if (result == 2)
			return Messages.getString("goishi.AnswerCheckMessage2"); //$NON-NLS-1$
		else if (result == 1)
			return Messages.getString("goishi.AnswerCheckMessage1"); //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}

}
