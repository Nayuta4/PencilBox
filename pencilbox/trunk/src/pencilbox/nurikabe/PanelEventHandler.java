package pencilbox.nurikabe;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�ʂ肩�ׁv�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = Board.UNKNOWN;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * �u�ʂ肩�ׁv�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
		} else {
			toggleState(pos, Board.WALL);
			if (board.isNumber(pos)) {
				currentState = Board.WALL;
			} else {
				currentState = board.getState(pos);
			}
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
		} else {
			toggleState(pos, Board.SPACE);
			if (board.isNumber(pos)) {
				currentState = Board.SPACE;
			} else {
				currentState = board.getState(pos);
			}
		}
	}

	protected void leftDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			if (isOn(dragStart)) {
				int number = board.getState(dragStart);
				if (number > 0 || number == Board.UNDECIDED_NUMBER){
					board.changeState(dragStart, Board.UNKNOWN);
					board.changeState(pos, number);
				}
			}
		} else {
			sweepState(pos);
		}
	}
	protected void rightDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
		} else {
			sweepState(pos);
		}
	}

	/**
	 * �}�X�̏�Ԃ� ���� �� st �ƕύX����
	 * @param pos �}�X���W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(Address pos, int st) {
		if (board.isNumber(pos))
			return;
		if (st == board.getState(pos)) {
			st = Board.UNKNOWN;
		}
		board.changeState(pos, st);
	}

	private void sweepState(Address pos) {
		if (board.isNumber(pos))
			return;
		if (currentState == board.getState(pos))
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * �u�ʂ肩�ׁv�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeState(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.changeState(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.UNKNOWN);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.changeState(posS, Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.changeState(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

}
