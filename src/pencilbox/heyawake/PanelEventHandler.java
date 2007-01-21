package pencilbox.heyawake;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 *  「へやわけ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

//	private Square draggingSquare;

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
		Square square = board.getSquare(pos.r(), pos.c());
		if (square != null)
			return square.mx();
		return 0;
	}
	/*
	 * 「へやわけ」マウス操作
	 */
	private Address dragStart = new Address(-1, -1);
	private int currentState = Board.UNKNOWN;
	
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			dragStart.set(dragEnd);
			setDraggingSquare(new Square(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c()));
		} else {
			board.toggleState(pos.r(), pos.c(), Board.BLACK);
			currentState = board.getState(pos.r(), pos.c());
		}
	}
	
	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			//			dragStart.set(dragEnd);
			board.removeSquareIncluding(dragEnd.r(), dragEnd.c());
		} else {
			board.toggleState(pos.r(), pos.c(), Board.WHITE);
			currentState = board.getState(pos.r(), pos.c());
		}
	}
	
	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
			if (getDraggingSquare() == null)
				return;
			getDraggingSquare().set(dragStart.r(), dragStart.c(), pos.r(), pos.c());
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st == currentState)
				return;
			if (currentState == Board.BLACK && board.isBlock(pos.r(), pos.c()))
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}
	
	protected void leftDragFixed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			if (getDraggingSquare() == null)
				return;
			setDraggingSquare(null);
			board.addSquareSpanning(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c());
			dragStart.setNowhere();
		}
	}
	
	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			board.removeSquareIncluding(dragEnd.r(), dragEnd.c());
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st == currentState)
				return;
			if (currentState == Board.WHITE && st == Board.BLACK)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}
	
	protected void dragFailed() {
		setDraggingSquare(null);
		dragStart.setNowhere();
	}
	/*
	 * 「へやわけ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			Square square = board.getSquare(pos.r(), pos.c());
			if (square != null)
				square.setNumber(num);
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			Square square = board.getSquare(pos.r(), pos.c());
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

