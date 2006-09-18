package pencilbox.kurodoko;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u���}�X�͂ǂ����v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �u���}�X�͂ǂ����v�}�E�X����
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.BLACK);
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.WHITE);
		int st = board.getState(pos.r(), pos.c());
		if (st > 0 || st == Board.UNDECIDED_NUMBER)
			currentState = Board.WHITE;
		else
			currentState = board.getState(pos.r(), pos.c());
	}

	protected void leftDragged(Address pos) {
		// �������Ȃ�
	}

	protected void rightDragged(Address pos) {
		int st = board.getState(pos.r(), pos.c());
		if (st >0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		board.changeStateA(pos.r(), pos.c(), currentState);
	}

	/*
	 * �u���}�X�͂ǂ����v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.setNumber(pos.r(), pos.c(), num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS.r(), posS.c()))
						board.setNumber(posS.r(), posS.c(), Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos.r(), pos.c(), Board.UNKNOWN);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS.r(), posS.c()))
					board.setNumber(posS.r(), posS.c(), Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS.r(), posS.c()))
					board.setNumber(posS.r(), posS.c(), Board.UNDECIDED_NUMBER);
			}
		}
	}
}
