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
		return board.isOnStar(pos.r(), pos.c());
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
		Area area = board.getArea(pos.r(), pos.c());
		if (area == null) {
			area = new Area();
			board.addCellToAreaA(pos.r(), pos.c(), area);
		}
		setDraggingArea(area);
	}

	protected void leftDragged(Address pos) {
		Area draggingArea = getDraggingArea();
		if (draggingArea == null)
			return;
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (oldArea != null && oldArea != draggingArea) {
			board.removeCellFromAreaA(pos.r(), pos.c(), oldArea);
			board.addCellToAreaA(pos.r(), pos.c(), draggingArea);
		} else if (oldArea != null && oldArea == draggingArea) {
		} else if (oldArea == null) {
			board.addCellToAreaA(pos.r(), pos.c(), draggingArea);
		}
	}

	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (oldArea != null) {
			board.removeCellFromAreaA(pos.r(), pos.c(), oldArea);
		}
	}
	
	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}
	
	protected void leftDragFixed(Address dragEnd) {
		setDraggingArea(null);
	}
	
	protected void dragFailed() {
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
	protected void numberEntered(Address p, int n) {
		if (isProblemEditMode())
			if (n == Board.BLACKSTAR || n == Board.WHITESTAR)
				board.setStar(p.r(), p.c(), n);
	}

	protected void spaceEntered(Address p) {
		if (isProblemEditMode())
			board.setStar(p.r(), p.c(), Board.NOSTAR);
	}
}
