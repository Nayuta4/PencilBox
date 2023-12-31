package pencilbox.satogaeri;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「さとがえり」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = NULLSTATE; // ドラッグ中の辺の状態を表す
	private static final int NULLSTATE  = -9;
	private static final int BALL = -19;
	private static final int NUMBER = -20;
	private static final int MOVE = -21;

	private int dragState = 0;
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
		setMaxInputNumber(9);   // 暫定的
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
	 * 「さとがえり」マウス操作
	 * ボールを始点にドラッグすると線を伸ばす、マスを移動しなければ線を消す
	 * 数字マスを始点にドラッグすると線を作って伸ばす、移動しなければそのマス終点、そのとき他の線は消す
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
			if (board.getRoute(pos) == Board.END) {
				currentState = BALL;
			} else if (board.hasNumber(pos)) {
				currentState = NUMBER;
			} else {
				currentState = NULLSTATE;
			}
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
			if (currentState == BALL || currentState == NUMBER || currentState == MOVE) {
				changeRoute(oldPos, pos);
				currentState = MOVE;
			}
		}
	}

	protected void leftReleased(Address pos) {
		if (isProblemEditMode()) {
			if (dragState == PRESS_EXISTING) {
				board.removeCellFromArea(pos, board.getArea(pos));
			}
			setDraggingArea(null);
			dragState = INIT;
		} else {
			if (isOn(pos)) {
				if (currentState == BALL) {
					board.eraseRoute(pos);
				} else if (currentState == NUMBER) {
					changeRoute1(pos, Board.END);
				} else if (currentState == NULLSTATE) {
				}
			}
			currentState = NULLSTATE;
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
		}
	}

	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			rightPressed(pos);
		} else {
		}
	}

	private void changeRoute(Address pos0, Address pos1) {
		int direction = Address.getDirectionTo(pos0, pos1);
		if (direction < 0) {
			return;
		}
		for (Address p = pos0; !p.equals(pos1); p = p.nextCell(direction)) {
			changeRoute1(p, direction);
		}
	}
	private void changeRoute1(Address p, int d) {
		// ○を起点に線を引くときは，前にあった線は消す。
		if (board.hasNumber(p) && board.getRoute(p) != Board.END) {
//		if (board.hasNumber(p)) {
			board.eraseRoute(p);
		}
		if (board.getRoute(p) != Board.END && !board.hasNumber(p))
			return;
		if (d >= Board.UP && d <= Board.RT) {
			Address q = p.nextCell(d);
			if (!board.isOn(q))
				return;
			// 線を元に戻るとき
			if (board.getRoute(q) == (d^2)) {
//				board.changeRoute(p, d);     // 元のマスに対する操作として登録するなら
				board.changeRoute(q, Board.END);   // 隣のマスに対する操作として登録するなら
			} else if (board.getRoute(q) == Board.NOROUTE && !board.hasNumber(q)) {
				int d1 = board.getIncomingDirection(p);
				if (d1 != Board.END && d1 != d) {
	//				System.out.println(d1 + " " + d + " " + "線の途中で向きは変えられない");
					return;
				}
				board.changeRoute(p, d);
			}
		} else if (d == Board.END) {
			board.changeRoute(p, d);
//			System.out.println(p + " : " + d);
		}
//		currentState = d;
	}
	/*
	 * 「さとがえり」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				board.changeFixedNumber(posS, Board.UNDETERMINED);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				board.changeFixedNumber(posS, Board.BLANK);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.UNDETERMINED);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				board.changeFixedNumber(posS, Board.UNDETERMINED);
			}
		}
	}
}

