package pencilbox.heyawake;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 *  �u�ւ�킯�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int pivotR = -1;  // �h���b�O���ɌŒ肷�钸�_�̍s���W
	private int pivotC = -1;  // �h���b�O���ɌŒ肷�钸�_�̗���W
//	private Square draggingSquare; // �h���b�O���č��܂��ɕ`�����Ƃ��Ă���l�p

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	protected int getMaxInputNumber() {
		Address pos = getCellCursor().getPosition();
		Square square = board.getSquare(pos);
		if (square != null)
			return square.mx();
		return 0;
	}
	/*
	 * �u�ւ�킯�v�}�E�X����
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Square draggingSquare;
			Square sq = board.getSquare(pos);
			if (sq == null) {
				draggingSquare = new Square(pos, pos);
			} else {
				draggingSquare = new Square(sq);
			}
			fixPivot(draggingSquare, pos);
			setDraggingSquare(draggingSquare);
		} else {
			toggleState(pos, Board.BLACK);
			currentState = board.getState(pos);
		}
	}

	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
			Square draggingSquare = getDraggingSquare();
			if (draggingSquare == null) {
				return;
			}
			if (pivotR >= 0 && pivotC >= 0) {
				draggingSquare.set(pivotR, pivotC, pos.r(), pos.c());
			} else if (pivotR >= 0 && pivotC == -1) {
				draggingSquare.set(pivotR, draggingSquare.c0(), pos.r(), draggingSquare.c1());
			} else if (pivotR == -1 && pivotC >= 0) {
				draggingSquare.set(draggingSquare.r0(), pivotC, draggingSquare.r1(), pos.c());
			} else if (pivotR == -1 && pivotC == -1) {
//				draggingSquare.set(draggingSquare.r0, draggingSquare.c0, draggingSquare.r1, drggingSquare.c1());
			}
			fixPivot(draggingSquare, pos);
		} else {
			sweepState(pos);
		}
	}

	private void fixPivot(Square s, Address pos) {
		int r = pos.r();
		int c = pos.c();
		if (pivotR == -1) {
			if (r == s.r0()) {
				pivotR = s.r1();
			} else if (r == s.r1()) {
				pivotR = s.r0();
			}
		}
		if (pivotC == -1) {
			if (c == s.c0()) {
				pivotC = s.c1();
			} else if (c == s.c1()) {
				pivotC = s.c0();
			}
		}
	}

	protected void leftReleased(Address pos) {
		if (isProblemEditMode()) {
			Square draggingSquare = getDraggingSquare();
			if (draggingSquare == null)
				return;
			int rp = pivotR >= 0 ? pivotR : draggingSquare.r0(); 
			int cp = pivotC >= 0 ? pivotC : draggingSquare.c0(); 
			Square sq = board.getSquare(rp, cp);
			if (sq == null) {
				board.removeOverlappedSquares(draggingSquare, null);
				board.addSquare(new Square(draggingSquare));
			} else {
				if (sq.r0() == draggingSquare.r0() && sq.c0() == draggingSquare.c0() && sq.r1() == draggingSquare.r1() && sq.c1() == draggingSquare.c1()) {
					;
				} else {
					board.removeOverlappedSquares(draggingSquare, sq);
					board.changeSquare(sq, draggingSquare);
				}
			}
			setDraggingSquare(null);
			resetPivot();
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Square sq = board.getSquare(pos);
			if (sq != null)
				board.removeSquare(sq);
		} else {
			toggleState(pos, Board.WHITE);
			currentState = board.getState(pos);
		}
	}

	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			Square sq = board.getSquare(pos);
			if (sq != null)
				board.removeSquare(sq);
		} else {
			sweepState(pos);
		}
	}
	
	private void resetPivot() {
		pivotR = -1;
		pivotC = -1;
	}

	/**
	 * �}�X�̏�Ԃ� �����st �ƕύX����
	 * @param pos �}�X�̍��W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(Address pos, int st){
		if (st == board.getState(pos))
			st = Board.UNKNOWN;
		board.changeStateA(pos, st);
	}

	private void sweepState(Address pos) {
		int st = board.getState(pos);
		if (currentState == st)
			return;
		if (currentState == Board.BLACK && board.isBlock(pos))
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeStateA(pos, currentState);
	}

	/*
	 * �u�ւ�킯�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			Square square = board.getSquare(pos);
			if (square != null)
				square.setNumber(num);
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			Square square = board.getSquare(pos);
			if (square != null)
				square.setNumber(Square.ANY);
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		((Panel) getPanel()).setDraggingSquare(draggingSquare);
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return ((Panel) getPanel()).getDraggingSquare();
	}
}
