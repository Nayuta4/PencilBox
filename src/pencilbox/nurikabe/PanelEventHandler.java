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
		board.toggleState(pos.r(), pos.c(), Board.WALL);
		if (board.isNumber(pos.r(), pos.c()))
			currentState = Board.UNKNOWN;
		else
			currentState = board.getState(pos.r(), pos.c());
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode())
			return;
		board.toggleState(pos.r(), pos.c(), Board.SPACE);
		if (board.isNumber(pos.r(), pos.c()))
			currentState = Board.SPACE;
		else
			currentState = board.getState(pos.r(), pos.c());
	}

	protected void leftDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			getCellCursor().setPosition(pos);
			if (isOn(dragStart)) {
				int number = board.getState(dragStart.r(), dragStart.c());
				if (number > 0 || number == Board.UNDECIDED_NUMBER){
					board.changeState(dragStart.r(), dragStart.c(), Board.UNKNOWN);
					board.changeState(pos.r(), pos.c(), number);
				}
			}
		}
		else {
			int st = board.getState(pos.r(), pos.c());
			if (st > 0 || st == Board.UNDECIDED_NUMBER)
				return;
			if (st == currentState)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}
	protected void rightDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			return;
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st > 0 || st == Board.UNDECIDED_NUMBER)
				return;
			if (st == currentState)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}

	/*
	 * 「ぬりかべ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.changeState(pos.r(), pos.c(), num);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.changeState(pos.r(), pos.c(), Board.UNKNOWN);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.changeState(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}

}
