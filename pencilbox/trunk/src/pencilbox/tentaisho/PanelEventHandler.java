package pencilbox.tentaisho;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「天体ショー」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int dragState = 0;
	private int currentState = -1;
	private Address pos3 = Address.NOWHERE;

	public static int INIT = 0;           // 初期状態
	public static int PRESS_NEW = 1;      // 新領域作成
	public static int PRESS_EXISTING = 2; // 既存領域選択
	public static int DRAG_ADD = 3;       // 領域拡大操作 
	public static int DRAG_REMOVE = 4;   // 領域縮小操作 

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
			dragState = PRESS_NEW;
		} else {
			dragState = PRESS_EXISTING;
		}
		setDraggingArea(area);
	}

	protected void leftDragged(Address oldPos, Address pos) {
		Area draggingArea = getDraggingArea();
		if (draggingArea == null)
			return;
		Area oldArea = board.getArea(pos);
		if (dragState == PRESS_NEW || dragState == PRESS_EXISTING) {
			if (oldArea == null || oldArea != draggingArea) {
				dragState = DRAG_ADD; // 領域拡大操作
			} else {
				dragState = DRAG_REMOVE; // 領域縮小操作
			}
		}
		if (dragState == DRAG_ADD) {
			if (oldArea != null && oldArea != draggingArea) {
				board.removeCellFromArea(pos, oldArea);
				board.addCellToArea(pos, draggingArea);
			} else if (oldArea != null && oldArea == draggingArea) {
			} else if (oldArea == null) {
				board.addCellToArea(pos, draggingArea);
			}
		} else if (dragState == DRAG_REMOVE) {
			Area oldoldArea = board.getArea(oldPos);
			if (oldoldArea!= null) {
				board.removeCellFromArea(oldPos, oldoldArea);
			}
		}
	}

	protected void leftReleased(Address pos) {
		if (dragState == PRESS_EXISTING) {
			board.removeCellFromArea(pos, board.getArea(pos));
		}
		setDraggingArea(null);
		dragState = INIT;
	}

//	protected void rightPressed(Address pos) {
//		Area oldArea = board.getArea(pos);
//		if (oldArea != null) {
//			board.removeCellFromArea(pos, oldArea);
//		}
//	}

//	protected void rightDragged(Address pos) {
//		rightPressed(pos);			
//	}

	protected void rightPressed3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		this.pos3 = sa;
	}

	protected void rightDragged3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		if (pos3.equals(sa))
			return;
		else if (pos3.r() != sa.r() && pos3.c() != sa.c())
			return;
		int dir = pos3.getDirectionTo(sa);
		Address sb =Address.nextCell(pos3, dir);
		SideAddress b = superAddress2SideAddress(sb);
		if (! isSideOn(b))
			return;
		sweepEdgeState(b, Board.LINE);
		this.pos3 = sa;
	}

	protected void rightReleased(Address pos) {
		currentState = -1;
	}

	private void sweepEdgeState(SideAddress side, int st) {
		if (currentState == -1) {
			if (board.getEdge(side) == Board.LINE) {
				currentState = Board.NOLINE;
			} else {
				currentState = Board.LINE;
			}
		}
		board.changeEdge(side, currentState);
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
