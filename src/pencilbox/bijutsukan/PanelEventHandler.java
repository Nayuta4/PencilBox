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
		board.toggleState(pos.r(), pos.c(), Board.ILLUMINATION);
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.NOILLUMINATION);
		if (board.isWall(pos.r(), pos.c()))
			currentState = Board.UNKNOWN;
		else
			currentState = board.getState(pos.r(), pos.c());
	}

	protected void leftDragged(Address pos) {
		// �������Ȃ�
	}

	protected void rightDragged(Address pos) {
		if (board.isWall(pos.r(), pos.c()))
			return;
		if (board.getState(pos.r(), pos.c()) == currentState)
			return;
		board.changeStateA(pos.r(), pos.c(), currentState);
	}

	/*
	 * �u���p�فv�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.changeState(pos.r(), pos.c(), num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isWall(posS.r(), posS.c()))
					board.changeState(posS.r(), posS.c(), Board.NONUMBER_WALL);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos.r(), pos.c(), Board.UNKNOWN);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isWall(posS.r(), posS.c()))
					board.changeState(posS.r(), posS.c(), Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos.r(), pos.c(), Board.NONUMBER_WALL);
		}
		if (isSymmetricPlacementMode()) {
			Address posS = getSymmetricPosition(pos);
			if (!board.isWall(posS.r(), posS.c()))
				board.changeState(posS.r(), posS.c(), Board.NONUMBER_WALL);
		}
	}

}
