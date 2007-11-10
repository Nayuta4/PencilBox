package pencilbox.fillomino;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「フィルオミノ」マウス／キー操作処理クラス
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
	}

	/*
	 * 「フィルオミノ」マウス操作
	 */
	private int state;

	protected void leftPressed(Address pos) {
		state = board.getNumber(pos.r(), pos.c());
	}

	protected void rightPressed(Address pos) {
		if (!board.isStable(pos.r(), pos.c()))
			board.enterNumberA(pos.r(), pos.c(), 0);
	}

	protected void leftClicked(Address pos) {
		if (!board.isStable(pos.r(), pos.c())) {
			int n = board.getNumber(pos);
			board.enterNumberA(pos.r(), pos.c(), n + 1);
		}
	}

	protected void rightClicked(Address pos) {
		if (!board.isStable(pos.r(), pos.c())) {
			int n = board.getNumber(pos);
			if (n > 0) 
				board.enterNumberA(pos.r(), pos.c(), n - 1);
		}
	}

	protected void leftDragged(Address oldPos, Address pos) {
		if (!board.isStable(pos.r(), pos.c()))
			board.enterNumberA(pos.r(), pos.c(), state);
	}

	protected void rightDragged(Address oldPos, Address pos) {
		if (!board.isStable(pos.r(), pos.c()))
			board.enterNumberA(pos.r(), pos.c(), 0);
	}

	/*
	 * 「フィルオミノ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos.r(), pos.c(), num);
				board.setState(pos.r(), pos.c(), Board.STABLE);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!posS.equals(pos))
						if (!board.isStable(posS.r(), posS.c())) {
							board.setState(posS.r(), posS.c(), Board.STABLE);
							board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
						}
				}
			}
		} else if (isCursorOn()) {
			if (num >= 0) {
				if (!board.isStable(pos.r(), pos.c())) {
					board.enterNumberA(pos.r(), pos.c(), num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (board.isStable(posS.r(), posS.c())) {
						board.setState(posS.r(), posS.c(), Board.UNSTABLE);
						board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
					}
			}
		} else if (isCursorOn()) {
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r(), pos.c(), Board.UNKNOWN);
			board.setState(pos.r(), pos.c(), Board.STABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (!board.isStable(posS.r(), posS.c())) {
						board.setState(posS.r(), posS.c(), Board.STABLE);
						board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
					}
			}
		}
	}
}
