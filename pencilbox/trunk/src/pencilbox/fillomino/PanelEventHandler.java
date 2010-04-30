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
		state = board.getNumber(pos);
	}

	protected void rightPressed(Address pos) {
		if (!board.isStable(pos))
			board.enterNumberA(pos, 0);
	}

	protected void leftDragged(Address oldPos, Address pos) {
		currentState = 2;
		if (!board.isStable(pos))
			board.enterNumberA(pos, state);
	}

	protected void rightDragged(Address oldPos, Address pos) {
		if (!board.isStable(pos))
			board.enterNumberA(pos, 0);
	}

	protected void leftReleased(Address pos) {
		if (currentState == 1 && isOn(pos))
			if (!board.isStable(pos)) {
				int n = board.getNumber(pos);
				board.enterNumberA(pos, n + 1);
			}
		currentState = 0;
	}

	/*
	 * �u�t�B���I�~�m�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos, num);
				board.setState(pos, Board.STABLE);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!posS.equals(pos))
						if (!board.isStable(posS)) {
							board.setState(posS, Board.STABLE);
							board.changeNumber(posS, Board.UNKNOWN);
						}
				}
			}
		} else if (isCursorOn()) {
			if (num >= 0) {
				if (!board.isStable(pos)) {
					board.enterNumberA(pos, num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, 0);
			board.setState(pos, Board.UNSTABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (board.isStable(posS)) {
						board.setState(posS, Board.UNSTABLE);
						board.changeNumber(posS, Board.UNKNOWN);
					}
			}
		} else if (isCursorOn()) {
			if (!board.isStable(pos)) {
				board.enterNumberA(pos, 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.UNKNOWN);
			board.setState(pos, Board.STABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (!board.isStable(posS)) {
						board.setState(posS, Board.STABLE);
						board.changeNumber(posS, Board.UNKNOWN);
					}
			}
		}
	}
}
