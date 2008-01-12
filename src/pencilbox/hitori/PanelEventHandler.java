package pencilbox.hitori;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ひとりにしてくれ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
		setMaxInputNumber(board.getMaxNumber()); 
	}
	
	/*
	 * 「ひとりにしてくれ」マウス操作
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos, Board.BLACK);
		currentState = board.getState(pos);
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos, Board.WHITE);
		currentState = board.getState(pos);
	}

	protected void leftDragged(Address pos) {
		int st = board.getState(pos);
		if (st == currentState)
			return;
		if (currentState == Board.BLACK && board.isBlock(pos))
			return;
		board.changeStateA(pos, currentState);
	}

	protected void rightDragged(Address pos) {
		int st = board.getState(pos);
		if (st == currentState)
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeStateA(pos, currentState);
	}

	/*
	 * 「ひとりにしてくれ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.setNumber(pos, num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos, 0);
	}

//	protected void starEntered(Address pos) {
//		if (problemEditMode)
//			board.setNumber(pos.r, pos.c, Board.UNDEFINED_NUMBER);
//	}

}
