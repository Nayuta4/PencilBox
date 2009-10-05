package pencilbox.hashi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u����������v�}�E�X�^�L�[���쏈���N���X
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
	 * �u����������v�}�E�X����
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart))
			return;
		if (!board.isPier(dragStart))
			return;
		int dir = Address.getDirectionTo(dragStart, dragEnd);
		if (dir >= 0) {
			board.addBridgeA(dragStart, dir);
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart))
			return;
		if (!board.isPier(dragStart))
			return;
		int dir = Address.getDirectionTo(dragStart, dragEnd);
		if (dir >= 0) {
			board.removeBridgeA(dragStart, dir);
		}
	}

	/*
	 * �u����������v�L�[����
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
