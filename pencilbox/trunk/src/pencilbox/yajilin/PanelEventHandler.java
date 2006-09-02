package pencilbox.yajilin;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ヤジリン」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(9);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * 「ヤジリン」マウス操作
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r() == dragEnd.r() || dragStart.c() == dragEnd.c()) {
			board.determineInlineState(dragStart, dragEnd, Board.LINE);
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r() == dragEnd.r() || dragStart.c() == dragEnd.c()) {
			board.determineInlineState(dragStart, dragEnd, Board.UNKNOWN);
		}
	}

	protected void leftClicked(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.BLACK);
	}

	protected void rightClicked(Address position) {
		board.toggleState(position.r(), position.c(), Board.WHITE);
	}
	
	/*
	 * 「ヤジリン」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			board.enterNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.eraseNumber(pos.r(), pos.c());
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.enterNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}
}
