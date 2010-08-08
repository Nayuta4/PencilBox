package pencilbox.shikaku;

import java.util.LinkedList;
import java.util.List;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BorderEditStep;
import pencilbox.common.core.CellEditStep;
import pencilbox.common.core.SideAddress;
import pencilbox.common.core.SquareEditStep;
import pencilbox.common.core.AbstractStep.EditType;
import pencilbox.resource.Messages;
import pencilbox.util.ArrayUtil;


/**
 * �u�l�p�ɐ؂�v�ՖʃN���X
 */
public class Board extends BoardBase {

	static final int UNDECIDED_NUMBER = -1;
	static final int LINE = 1;
	static final int NOLINE = 0;

	private int[][] number;
	private int[][][] edge;
	private Square[][] square;
	private List<Square> squareList;

	protected void setup() {
		super.setup();
		number = new int[rows()][cols()];
		square = new Square[rows()][cols()];
		squareList = new LinkedList<Square>();
		edge = new int[2][][];
		edge[0] = new int[rows()][cols() - 1];
		edge[1] = new int[rows() - 1][cols()];
	}

	public void clearBoard() {
		super.clearBoard();
		squareList.clear();
		ArrayUtil.initArrayObject2(square, null);
		for (SideAddress p : borderAddrs()) {
			setEdge(p, NOLINE);
		}
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

	public void initBoard() {
		for (Square sq : squareList) {
			initSquareNumber(sq);
		}
	}

	/**
	 * �l�p�Ɋ܂܂�鐔���}�X��ݒ肷��
	 * @param sq
	 */
	public void initSquareNumber(Square sq) {
		int n = 0;
		for (Address p : sq.cellSet()) {
			if (isNumber(p)) {
				if (n != 0) {
					n = Square.MULTIPLE_NUMBER;
				} else {
					n = getNumber(p);
				}
			}
		}
		sq.setNumber(n);
	}

	/**
	 * @return Returns the squareList.
	 */
	List<Square> getSquareList() {
		return squareList;
	}

	/**
	 * �}�X�̐������擾����
	 * @param r �s���W
	 * @param c ����W
	 * @return �}�X�̐���
	 */
	public int getNumber(int r, int c) {
		return number[r][c];
	}

	public int getNumber(Address pos) {
		return getNumber(pos.r(), pos.c());
	}
	/**
	 * �}�X�ɐ�����ݒ肷��
	 * @param r �s���W
	 * @param c ����W
	 * @param n �ݒ肷�鐔��
	 */
	public void setNumber(int r, int c, int n) {
		number[r][c] = n;
	}

	public void setNumber(Address pos, int n) {
		setNumber(pos.r(), pos.c(), n);
	}
	/**
	 * ���̃}�X�ɐ��������邩
	 * @param p ���W
	 * @return�@���̃}�X�ɐ���������� true
	 */
	public boolean isNumber(Address pos) {
		int n = getNumber(pos);
		return n > 0 || n == Board.UNDECIDED_NUMBER;
	}
	/**
	 * ���̃}�X�̑����� Square ��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @return�@���̃}�X�̑����� Square
	 */
	public Square getSquare(int r, int c) {
		return square[r][c];
	}

	public Square getSquare(Address p) {
		return square[p.r()][p.c()];
	}

	public void setSquare(int r, int c, Square s) {
		square[r][c] = s;
	}

	public void setSquare(Address p, Square s) {
		square[p.r()][p.c()] = s;
	}

	public int getEdge(SideAddress p) {
		return edge[p.d()][p.r()][p.c()];
	}

	public void setEdge(SideAddress p, int i) {
		edge[p.d()][p.r()][p.c()] = i;
	}

	/**
	 * �l�p�`�͈̔͂Ɋ܂܂��}�X��Square�I�u�W�F�N�g��ݒ肷��
	 * @param region �ݒ肷��l�p�`�͈̔�
	 * @param sq �ݒ肷��Square�I�u�W�F�N�g
	 */
	public void setSquare(Square region, Square sq) {
		for (Address p : region.cellSet()) {
			setSquare(p, sq);
		}
	}
	/**
	 * �l�p��ǉ��C�ύX�����Ƃ��ɂ��łɂ��鑼�̎l�p�Əd�Ȃ�ꍇ�C���̎l�p����������B
	 * @param sq �ǉ�,�ύX����l�p
	 * @param org �ύX����ꍇ�̂��Ƃ̎l�p
	 */
	public void removeOverlappedSquares(Square sq, Square org) {
		for (Address p : sq.cellSet()) {
			Square s = getSquare(p);
			if (s != null && s != org) {
				removeSquare(s);
			}
		}
	}
	/**
	 * �����̐�����ύX����B
	 * @param p
	 * @param n
	 */
	public void changeNumber(Address p, int n) {
		int prev = getNumber(p);
		if (n == prev)
			return;
		if (isRecordUndo()) {
			fireUndoableEditUpdate(new CellEditStep(EditType.FIXED, p, prev, n));
		}
		setNumber(p, n);
		if (getSquare(p) != null)
			initSquareNumber(getSquare(p));
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
		if (step instanceof SquareEditStep) {
			SquareEditStep s = (SquareEditStep) step;
			if (s.getOperation() == SquareEditStep.ADDED) {
				removeSquare(getSquare(s.getQ0()));
			} else if (s.getOperation() == SquareEditStep.REMOVED) {
				addSquare(new Square(s.getP0(), s.getP1()));
			} else if (s.getOperation() == SquareEditStep.CHANGED) {
				changeSquare(getSquare(s.getQ0()), s.getP0(), s.getP1());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getBefore());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getBefore());
		}
	}

	public void redo(AbstractStep step) {
		if (step instanceof SquareEditStep) {
			SquareEditStep s = (SquareEditStep) step;
			if (s.getOperation() == SquareEditStep.ADDED) {
				addSquare(new Square(s.getQ0(), s.getQ1()));
			} else if (s.getOperation() == SquareEditStep.REMOVED) {
				removeSquare(getSquare(s.getP0()));
			} else if (s.getOperation() == SquareEditStep.CHANGED) {
				changeSquare(getSquare(s.getP0()), s.getQ0(), s.getQ1());
			}
		} else if (step instanceof BorderEditStep) {
			BorderEditStep s = (BorderEditStep) step;
			changeEdge(s.getPos(), s.getAfter());
		} else if (step instanceof CellEditStep) {
			CellEditStep s = (CellEditStep) step;
			changeNumber(s.getPos(), s.getAfter());
		}
	}
	/**
	 * �l�p��ǉ�����
	 * @param sq �ǉ�����l�p
	 */
	public void addSquare(Square sq) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new SquareEditStep(sq.p0(), sq.p1(), SquareEditStep.ADDED));
		setSquare(sq, sq);
		initSquareNumber(sq);
		squareList.add(sq);
	}
	/**
	 * �l�p��ύX����
	 * @param sq �ύX�����l�p
	 * @param q0 �ύX��̎l�p�̊p�̍��W
	 * @param q1 �ύX��̎l�p�̊p�̍��W
	 */
	public void changeSquare(Square sq, Address q0, Address q1) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new SquareEditStep(sq.p0(), sq.p1(), q0, q1, SquareEditStep.CHANGED));
		setSquare(sq, null);
		sq.set(q0, q1);
		setSquare(sq, sq);
		initSquareNumber(sq);
	}
	/**
	 * �l�p����������
	 * @param sq ��������l�p
	 */
	public void removeSquare(Square sq) {
		if (isRecordUndo())
			fireUndoableEditUpdate(new SquareEditStep(sq.p0(), sq.p1(), SquareEditStep.REMOVED));
		setSquare(sq, null);
		squareList.remove(sq);
	}

	public int checkAnswerCode() {
		int errorCode = 0;
		int nNumber = 0;
		for (Square sq : squareList) {
			int n = sq.getNumber();
			if (n == Square.MULTIPLE_NUMBER) {
				errorCode |= 1;
			} else if (n == Square.NO_NUMBER) {
				errorCode |= 2;
			} else if (n == UNDECIDED_NUMBER) {
				;
			} else if (n < sq.getSquareSize()) {
				errorCode |= 4;
			} else if (n > sq.getSquareSize()) {
				errorCode |= 8;
			}
		}
		for (Address p : cellAddrs()) {
			if (isNumber(p))
				nNumber ++;
				if (getSquare(p) == null)
					errorCode |= 16;
		}
		if (nNumber==0)
			errorCode = 32;
		return errorCode;
	}

	public String checkAnswerString() {
		int result = checkAnswerCode();
		if (result == 0)
			return COMPLETE_MESSAGE;
		if (result == 32)
			return Messages.getString("shikaku.AnswerCheckMessage6"); //$NON-NLS-1$
		StringBuffer message = new StringBuffer();
		if ((result & 1) == 1)
			message.append(Messages.getString("shikaku.AnswerCheckMessage1")); //$NON-NLS-1$
		if ((result & 2) == 2)
			message.append(Messages.getString("shikaku.AnswerCheckMessage2")); //$NON-NLS-1$
		if ((result & 4) == 4)
			message.append(Messages.getString("shikaku.AnswerCheckMessage3")); //$NON-NLS-1$
		if ((result & 8) == 8)
			message.append(Messages.getString("shikaku.AnswerCheckMessage4")); //$NON-NLS-1$
		if ((result & 16) == 16)
			message.append(Messages.getString("shikaku.AnswerCheckMessage5")); //$NON-NLS-1$
		return message.toString();
	}
}
