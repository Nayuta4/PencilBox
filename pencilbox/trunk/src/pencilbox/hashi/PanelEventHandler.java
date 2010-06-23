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
	 * �u����������v�L�[����
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
