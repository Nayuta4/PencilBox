package pencilbox.goishi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「碁石ひろい」マウス／キー操作処理クラス
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
	 * 「碁石ひろい」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (isOn(pos)) {
			if (board.canPick(pos)) {
				board.pickUp(pos);
			}
		}
	}

	protected void rightPressed(Address pos) {
		if (board.pickedList.size() > 0)
			board.placeBack();
	}

	/*
	 * 「碁石ひろい」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num == 0) {
				board.removeStone(pos);
			} else if (num == 1) {
				board.addStone(pos);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.removeStone(pos);
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.addStone(pos);
		}
	}
}
