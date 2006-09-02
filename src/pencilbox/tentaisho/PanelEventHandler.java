package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「天体ショー」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private Area draggingArea;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	public CellCursor createCursor() {
		return new TentaishoCursor(this);
	}

	/*
	 * 「天体ショー」マウス操作
	 */
	protected void leftPressed(Address pos) {
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (draggingArea == null) {
			//  ここの if 文を有効にすれば，既存のAreaを内側から広げることができる
			//  ただし，undo と整合をどうするかが問題				
//			if (oldArea != null)
//				draggingArea = oldArea;
//			else
			draggingArea = new Area();
		}
		if (oldArea != null && oldArea != draggingArea) {
			board.removeAreaA(oldArea);
		}
		board.setArea(pos.r(), pos.c(), draggingArea);
		draggingArea.add(pos);
	}
	
	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (oldArea != null) {
			board.removeAreaA(oldArea);
		}
	}
	
	protected void leftDragged(Address pos) {
		leftPressed(pos);			
	}
	
	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}
	
	protected void leftDragFixed(Address dragEnd) {
		if (draggingArea == null)
			return;
		board.addAreaA(draggingArea);
		draggingArea = null;
	}
	
	protected void rightDragFixed(Address dragStart, Address dragEnd) {
		//			board.removeSquare(dragStart.r, dragStart.c, dragEnd.r, dragEnd.c);
		draggingArea = null;
	}

	protected void dragFailed() {
		if (draggingArea == null)
			return;
		board.addAreaA(draggingArea);
		draggingArea = null;
	}
	/*
	 * マウスではカーソル移動しない（暫定）
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
