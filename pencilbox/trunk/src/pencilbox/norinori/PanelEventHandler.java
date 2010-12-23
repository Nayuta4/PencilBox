package pencilbox.norinori;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「のりのり」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int dragState = 0;
	private int currentState = Board.UNKNOWN;

	private static final int INIT = 0;           // 初期状態
	private static final int PRESS_NEW = 1;      // 新領域作成
	private static final int PRESS_EXISTING = 2; // 既存領域選択
	private static final int DRAG_ADD = 3;       // 領域拡大操作 
	private static final int DRAG_REMOVE = 4;   // 領域縮小操作 

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
	 * 「のりのり」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Area area = board.getArea(pos);
			if (area == null) {
				area = new Area();
				board.addCellToArea(pos, area);
				dragState = PRESS_NEW;
			} else {
				dragState = PRESS_EXISTING;
			}
			setDraggingArea(area);
		} else {
			toggleState(pos, Board.BLACK);
			currentState = board.getState(pos);
		}
	}

	protected void leftDragged(Address oldPos, Address pos) {
		if (isProblemEditMode()) {
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
				if (!isOn(oldPos))
					return;
				Area oldoldArea = board.getArea(oldPos);
				if (oldoldArea!= null) {
					board.removeCellFromArea(oldPos, oldoldArea);
				}
			}
		} else {
			sweepState(pos);
		}
	}

	protected void leftReleased(Address pos) {
		if (isProblemEditMode()) {
			if (dragState == PRESS_EXISTING) {
				board.removeCellFromArea(pos, board.getArea(pos));
			}
			setDraggingArea(null);
			dragState = INIT;
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
//			Area oldArea = board.getArea(pos);
//			if (oldArea != null) {
//				board.removeCellFromArea(pos, oldArea);
//			}
		} else {
			toggleState(pos, Board.WHITE);
			currentState = board.getState(pos);
		}
	}

	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
//			rightPressed(pos);
		} else {
			sweepState(pos);
		}
	}

	/**
	 * マスの状態を 未定 ⇔ st と変更する
	 * @param pos マス座標
	 * @param st 切り替える状態
	 */
	private void toggleState(Address pos, int st) {
		if (st == board.getState(pos))
			st = Board.UNKNOWN;
		board.changeState(pos, st);
	}
	
	private void sweepState(Address pos) {
		int st = board.getState(pos);
		if (st == currentState)
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * 「のりのり」キー操作
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
