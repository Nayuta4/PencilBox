package pencilbox.bijutsukan;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「美術館」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;
	
	/**
	 * Panel を生成する
	 */
	public PanelEventHandler() {
		setMaxInputNumber(5);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * 「美術館」マウス操作
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
		// 何もしない
	}

	protected void rightDragged(Address pos) {
		if (board.isWall(pos))
			return;
		if (board.getState(pos) == currentState)
			return;
		board.changeStateA(pos, currentState);
	}

	/*
	 * 「美術館」キー操作
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
