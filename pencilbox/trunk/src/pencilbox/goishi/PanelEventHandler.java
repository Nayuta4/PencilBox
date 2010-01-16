package pencilbox.goishi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u��΂Ђ낢�v�}�E�X�^�L�[���쏈���N���X
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
	 * �u��΂Ђ낢�v�}�E�X����
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
	 * �u��΂Ђ낢�v�L�[����
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
