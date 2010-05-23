package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「天体ショー」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	public boolean isCursorOnBoard(Address pos) {
		return board.isOnStar(pos);
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
	 * 「天体ショー」マウス操作
	 */
	protected void leftPressed(Address pos) {
		Area area = board.getArea(pos);
		if (area == null) {
			area = new Area();
			board.addCellToArea(pos, area);
		}
		setDraggingArea(area);
	}

	protected void leftDragged(Address pos) {
		Area draggingArea = getDraggingArea();
		if (draggingArea == null)
			return;
		Area oldArea = board.getArea(pos);
		if (oldArea != null && oldArea != draggingArea) {
			board.removeCellFromArea(pos, oldArea);
			board.addCellToArea(pos, draggingArea);
		} else if (oldArea != null && oldArea == draggingArea) {
		} else if (oldArea == null) {
			board.addCellToArea(pos, draggingArea);
		}
	}

	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos);
		if (oldArea != null) {
			board.removeCellFromArea(pos, oldArea);
		}
	}

	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}

	protected void leftReleased(Address dragEnd) {
		setDraggingArea(null);
	}

	/*
	 * マウスではカーソル移動しない
	 */
	protected void moveCursor(Address pos) {
	}

	/*
	 * 「天体ショー」キー操作
	 */
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode())
			if (n == Board.BLACKSTAR || n == Board.WHITESTAR)
				board.changeStar(pos, n);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.changeStar(pos, Board.NOSTAR);
	}
}
