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
		toggleState(pos, Board.BLACK);
		currentState = board.getState(pos);
	}

	protected void rightPressed(Address pos) {
		toggleState(pos, Board.WHITE);
		currentState = board.getState(pos);
	}

	protected void leftDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	/**
	 * マスの状態を 未定 ⇔ st と変更する
	 * @param pos マス座標
	 * @param st 切り替える状態
	 */
	private void toggleState(Address pos, int st) {
		if (st == board.getState(pos)) {
			st = Board.UNKNOWN;
		}
		board.changeState(pos, st);
	}

	private void sweepState(Address pos) {
		int st = board.getState(pos);
		if (currentState == st)
			return;
		if (currentState == Board.BLACK && board.isBlock(pos))
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * 「ひとりにしてくれ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.changeNumber(pos, num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.changeNumber(pos, 0);
	}

//	protected void starEntered(Address pos) {
//		if (problemEditMode)
//			board.changeNumber(pos.r, pos.c, Board.UNDEFINED_NUMBER);
//	}

}
