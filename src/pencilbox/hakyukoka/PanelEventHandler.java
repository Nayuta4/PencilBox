package pencilbox.hakyukoka;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�g�y���ʁv�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(9);   // �b��I
	}
	
	private void setSelectedNumber(int n) {
		((Panel) getPanel()).setSelectedNumber(n);
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
	 * �u�g�y���ʁv�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Area area = board.getArea(pos.r(), pos.c());
			if (area == null) {
				area = new Area();
				board.addCellToArea(pos.r(), pos.c(), area);
			}
			setDraggingArea(area);
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.increaseNumber(pos.r(), pos.c());
			}
			setSelectedNumber(board.getNumber(pos.r(), pos.c()));
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
				board.removeCellFromArea(pos.r(), pos.c(), oldArea);
				board.addCellToArea(pos.r(), pos.c(), draggingArea);
			} else if (oldArea != null && oldArea == draggingArea) {
			} else if (oldArea == null) {
				board.addCellToArea(pos.r(), pos.c(), draggingArea);
			}
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.increaseNumber(pos.r(), pos.c());
			}
			setSelectedNumber(board.getNumber(pos.r(), pos.c()));
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (oldArea != null) {
				board.removeCellFromArea(pos.r(), pos.c(), oldArea);
			}
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.decreaseNumber(pos.r(), pos.c());
			}
			setSelectedNumber(board.getNumber(pos.r(), pos.c()));
		}
	}
	
	protected void rightDragged(Address pos) {
		if (isProblemEditMode())
			rightPressed(pos);
	}
	
	protected void leftDragFixed(Address dragEnd) {
		setDraggingArea(null);
	}
	
	protected void dragFailed() {
		setDraggingArea(null);
	}

	/*
	 * �u�g�y���ʁv�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos.r(), pos.c(), num);
				board.setState(pos.r(), pos.c(), Board.STABLE);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isStable(posS.r(), posS.c())) {
						board.setState(posS.r(), posS.c(), Board.STABLE);
						board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
					}
				}
			}
		} else if (isCursorOn()){
			if (num > 0) {
				if (!board.isStable(pos.r(), pos.c())) {
					board.enterNumberA(pos.r(), pos.c(), num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isStable(posS.r(), posS.c())) {
					board.setState(posS.r(), posS.c(), Board.UNSTABLE);
					board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
				}
			}
		} else if (isCursorOn()){
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNKNOWN);
			board.setState(pos.r(), pos.c(), Board.STABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isStable(posS.r(), posS.c())) {
					board.setState(posS.r(), posS.c(), Board.STABLE);
					board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
				}
			}
		}
	}
}
