package pencilbox.lits;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * u‚k‚h‚s‚rvƒ}ƒEƒX^ƒL[‘€ìˆ—ƒNƒ‰ƒX
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;
	private int currentState = Board.UNKNOWN;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}
	
	/**
	 * @return the draggingArea
	 */
	Area getDraggingArea() {
		return ((Panel) getPanel()).getDraggingArea();
	}
	/**
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Area draggingArea) {
		((Panel) getPanel()).setDraggingArea(draggingArea);
	}

	/*
	 * u‚k‚h‚s‚rvƒ}ƒEƒX‘€ì
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Area area = board.getArea(pos.r(), pos.c());
			if (area == null) {
				area = new Area();
				board.addCellToArea(pos, area);
			}
			setDraggingArea(area);
		} else {
			board.toggleState(pos.r(), pos.c(), Board.BLACK);
			currentState = board.getState(pos.r(), pos.c());
		}
	}

	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
//			moveCursor(pos);
			Area draggingArea = getDraggingArea();
			if (draggingArea == null)
				return;
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (oldArea != null && oldArea != draggingArea) {
				board.removeCellFromArea(pos, oldArea);
				board.addCellToArea(pos, draggingArea);
			} else if (oldArea != null && oldArea == draggingArea) {
			} else if (oldArea == null) {
				board.addCellToArea(pos, draggingArea);
			}
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st == currentState)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (oldArea != null) {
				board.removeCellFromArea(pos, oldArea);
			}
		} else {
			board.toggleState(pos.r(), pos.c(), Board.WHITE);
			currentState = board.getState(pos.r(), pos.c());
		}
	}
	
	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			rightPressed(pos);
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st == currentState)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}
	
	protected void leftDragFixed(Address dragEnd) {
		setDraggingArea(null);
	}
	
	protected void dragFailed() {
		setDraggingArea(null);
	}

	/*
	 * u‚k‚h‚s‚rvƒL[‘€ì
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
		} else {
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
		} else {
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
		} else {
		}
	}
}
