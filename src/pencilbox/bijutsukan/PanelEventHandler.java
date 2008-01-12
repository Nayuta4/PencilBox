package pencilbox.bijutsukan;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u���p�فv�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;
	
	/**
	 * Panel �𐶐�����
	 */
	public PanelEventHandler() {
		setMaxInputNumber(5);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �u���p�فv�}�E�X����
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos, Board.BULB);
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos, Board.NOBULB);
		if (board.isWall(pos))
			currentState = Board.UNKNOWN;
		else
			currentState = board.getState(pos);
	}

	protected void leftDragged(Address pos) {
		// �������Ȃ�
	}

	protected void rightDragged(Address pos) {
		if (board.isWall(pos))
			return;
		if (board.getState(pos) == currentState)
			return;
		board.changeStateA(pos, currentState);
	}

	/*
	 * �u���p�فv�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.changeState(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isWall(posS))
					board.changeState(posS, Board.NONUMBER_WALL);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.UNKNOWN);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isWall(posS))
					board.changeState(posS, Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.NONUMBER_WALL);
		}
		if (isSymmetricPlacementMode()) {
			Address posS = getSymmetricPosition(pos);
			if (!board.isWall(posS))
				board.changeState(posS, Board.NONUMBER_WALL);
		}
	}

}
