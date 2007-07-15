package pencilbox.shikaku;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�l�p�ɐ؂�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int pivotR = -1;  // �h���b�O���ɌŒ肷�钸�_�̍s���W
	private int pivotC = -1;  // �h���b�O���ɌŒ肷�钸�_�̗���W
//	private Square draggingSquare; // �h���b�O���č��܂��ɕ`�����Ƃ��Ă���l�p

	/**
	 * �p�l���𐶐�����
	 */
	public PanelEventHandler() {
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �u�l�p�ɐ؂�v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		Square draggingSquare;
		Square sq = board.getSquare(pos);
		if (sq == null) {
			draggingSquare = new Square(pos.r(), pos.c(), pos.r(), pos.c());
		} else {
			draggingSquare = new Square(sq);
		}
		fixPivot(draggingSquare, pos.r(), pos.c());
		setDraggingSquare(draggingSquare);
	}

	protected void leftDragged(Address pos) {
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
//			draggingSquare.set(draggingSquare.r0, draggingSquare.c0, draggingSquare.r1, drggingSquare.c1());
		}
		fixPivot(draggingSquare, pos.r(), pos.c());
	}
	
	private void fixPivot(Square s, int r, int c) {
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
	
	protected void leftDragFixed(Address pos) {
		Square draggingSquare = getDraggingSquare();
		if (draggingSquare == null)
			return;
		int rp = pivotR >= 0 ? pivotR : draggingSquare.r0(); 
		int cp = pivotC >= 0 ? pivotC : draggingSquare.c0(); 
		Square sq = board.getSquare(rp, cp);
		if (sq == null) {
			board.removeOverlappedSquares(draggingSquare, null);
			board.addSquareA(new Square(draggingSquare));
		} else {
			if (sq.r0() == draggingSquare.r0() && sq.c0() == draggingSquare.c0() && sq.r1() == draggingSquare.r1() && sq.c1() == draggingSquare.c1()) {
				;
			} else {
				board.removeOverlappedSquares(draggingSquare, sq);
				board.changeSquareA(sq, draggingSquare);
			}
		}
		setDraggingSquare(null);
		resetPivot();
	}
	
	protected void dragFailed() {
		setDraggingSquare(null);
		resetPivot();
	}

	protected void rightPressed(Address pos) {
		Square s = board.getSquare(pos);
		if(s != null) {
			board.removeSquareA(s);
		}
	}
	
	protected void rightDragged(Address pos) {
		Square s = board.getSquare(pos);
		if(s != null) {
			board.removeSquareA(s);
		}
	}
	
	private void resetPivot() {
		pivotR = -1;
		pivotC = -1;
	}

	/*
	 * �u�l�p�ɐ؂�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.setNumber(pos.r(), pos.c(), num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS.r(), posS.c()))
						board.setNumber(posS.r(), posS.c(), Board.UNDECIDED_NUMBER);
				}
			}
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS.r(), posS.c()))
					board.setNumber(posS.r(), posS.c(), 0);
			}
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS.r(), posS.c()))
					board.setNumber(posS.r(), posS.c(), Board.UNDECIDED_NUMBER);
			}
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		((Panel)getPanel()).setDraggingSquare(draggingSquare);
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return ((Panel)getPanel()).getDraggingSquare();
	}
}
