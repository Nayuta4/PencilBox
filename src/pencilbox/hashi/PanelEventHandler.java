package pencilbox.hashi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「橋をかけろ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

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
		int dir = Address.getDirectionTo(dragStart, dragEnd);
		if (dir >= 0) {
			board.changeLine(dragStart, dir, board.getLineFromPier(dragStart, dir)+1);
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart))
			return;
		if (!board.isPier(dragStart))
			return;
		int dir = Address.getDirectionTo(dragStart, dragEnd);
		if (dir >= 0) {
			board.changeLine(dragStart, dir, board.getLineFromPier(dragStart, dir)-1);
		}
	}

	/*
	 * 「橋をかけろ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num>=1 && num<=8) {
				board.changeNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isPier(posS))
						board.changeNumber(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.NO_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isPier(posS))
					board.changeNumber(posS, Board.NO_NUMBER);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isPier(posS))
					board.changeNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

}
