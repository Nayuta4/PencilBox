package pencilbox.hashi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「橋をかけろ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;
	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(8);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「橋をかけろ」マウス操作
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart))
			return;
		if (!board.isPier(dragStart))
			return;
		if (dragStart.r() == dragEnd.r()) {
			if (dragStart.c() < dragEnd.c()) {
				board.addBridgeA(dragStart, RT);
			} else if (dragStart.c() > dragEnd.c()) {
				board.addBridgeA(dragStart, LT);
			}
		} else if (dragStart.c() == dragEnd.c()) {
			if (dragStart.r() < dragEnd.r()) {
				board.addBridgeA(dragStart, DN);
			} else if (dragStart.r() > dragEnd.r()) {
				board.addBridgeA(dragStart, UP);
			}
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart))
			return;
		if (!board.isPier(dragStart))
			return;
		if (dragStart.r() == dragEnd.r()) {
			if (dragStart.c() < dragEnd.c()) {
				board.removeBridgeA(dragStart, RT);
			} else if (dragStart.c() > dragEnd.c()) {
				board.removeBridgeA(dragStart, LT);
			}
		} else if (dragStart.c() == dragEnd.c()) {
			if (dragStart.r() < dragEnd.r()) {
				board.removeBridgeA(dragStart, DN);
			} else if (dragStart.r() > dragEnd.r()) {
				board.removeBridgeA(dragStart, UP);
			}
		}
	}

	/*
	 * 「橋をかけろ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num>=1 && num<=8) {
				board.setNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isPier(posS))
						board.setNumber(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isPier(posS))
					board.setNumber(posS, 0);
			}
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isPier(posS))
					board.setNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

}
