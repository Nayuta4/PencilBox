package pencilbox.fillomino;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�t�B���I�~�m�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = 0; // �h���b�O���̕ӂ̏�Ԃ�\��

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * �u�t�B���I�~�m�v�}�E�X����
	 */
	private int state;

	protected void leftPressed(Address pos) {
		currentState = 1;
		state = board.getNumberOrState(pos);
	}

	protected void rightPressed(Address pos) {
		if (!board.isStable(pos))
			board.changeAnswerNumber(pos, 0);
	}

	protected void leftDragged(Address oldPos, Address pos) {
		currentState = 2;
		if (!board.isStable(pos))
			board.changeAnswerNumber(pos, state);
	}

	protected void rightDragged(Address oldPos, Address pos) {
		if (!board.isStable(pos))
			board.changeAnswerNumber(pos, 0);
	}

	protected void leftReleased(Address pos) {
		if (currentState == 1 && isOn(pos)) {
			if (!board.isStable(pos)) {
				int n = board.getNumberOrState(pos);
				board.changeAnswerNumber(pos, n + 1);
			}
		}
		currentState = 0;
	}

	/*
	 * �u�t�B���I�~�m�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeFixedNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!posS.equals(pos))
						if (!board.isStable(posS)) {
							board.changeFixedNumber(posS, Board.UNDETERMINED);
						}
				}
			}
		} else if (isCursorOn()) {
			if (num >= 0) {
				if (!board.isStable(pos)) {
					board.changeAnswerNumber(pos, num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (board.isStable(posS)) {
						board.changeFixedNumber(posS, Board.BLANK);
					}
			}
		} else if (isCursorOn()) {
			if (!board.isStable(pos)) {
				board.changeAnswerNumber(pos, 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.UNDETERMINED);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (!board.isStable(posS)) {
						board.changeFixedNumber(posS, Board.UNDETERMINED);
					}
			}
		}
	}
}
