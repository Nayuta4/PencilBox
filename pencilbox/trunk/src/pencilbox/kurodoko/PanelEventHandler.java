package pencilbox.kurodoko;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「黒マスはどこだ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * 「黒マスはどこだ」マウス操作
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos, Board.BLACK);
		int st = board.getState(pos);
		if (st > 0 || st == Board.UNDECIDED_NUMBER)
			currentState = Board.UNKNOWN;
		else
			currentState = st;
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos, Board.WHITE);
		int st = board.getState(pos);
		if (st > 0 || st == Board.UNDECIDED_NUMBER)
			currentState = Board.WHITE;
		else
			currentState = st;
	}

	protected void leftDragged(Address pos) {
		int st = board.getState(pos);
		if (st >0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		if (currentState == Board.BLACK && board.isBlock(pos))
			return;
		board.changeStateA(pos, currentState);
	}

	protected void rightDragged(Address pos) {
		int st = board.getState(pos);
		if (st >0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeStateA(pos, currentState);
	}

	/*
	 * 「黒マスはどこだ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.setNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.setNumber(posS, Board.UNDECIDED_NUMBER);
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
					board.setNumber(posS, Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}
}
