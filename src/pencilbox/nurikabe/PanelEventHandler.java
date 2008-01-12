package pencilbox.nurikabe;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ぬりかべ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = Board.UNKNOWN;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * 「ぬりかべ」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode())
			return;
		board.toggleState(pos, Board.WALL);
		if (board.isNumber(pos))
			currentState = Board.UNKNOWN;
		else
			currentState = board.getState(pos);
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode())
			return;
		board.toggleState(pos, Board.SPACE);
		if (board.isNumber(pos))
			currentState = Board.SPACE;
		else
			currentState = board.getState(pos);
	}

	protected void leftDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
//			moveCursor(pos);
			if (isOn(dragStart)) {
				int number = board.getState(dragStart);
				if (number > 0 || number == Board.UNDECIDED_NUMBER){
					board.changeState(dragStart, Board.UNKNOWN);
					board.changeState(pos, number);
				}
			}
		}
		else {
			int st = board.getState(pos);
			if (st > 0 || st == Board.UNDECIDED_NUMBER)
				return;
			if (st == currentState)
				return;
			board.changeStateA(pos, currentState);
		}
	}
	protected void rightDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			return;
		} else {
			int st = board.getState(pos);
			if (st > 0 || st == Board.UNDECIDED_NUMBER)
				return;
			if (st == currentState)
				return;
			board.changeStateA(pos, currentState);
		}
	}

	/*
	 * 「ぬりかべ」キー操作
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
