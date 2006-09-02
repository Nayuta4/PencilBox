package pencilbox.slitherlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「スリザーリンク」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		super();
		setMaxInputNumber(3);
	}
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * スリザーリンク専用カーソル生成
	 */
	public CellCursor createCursor() {
		return new SlitherLinkCursor(this);
	}

	/*
	 * 「スリザーリンク」マウス操作2
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.NOLINE);
	}

	/*
	 * 「スリザーリンク」マウス操作
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
	/*
	 * マウスではカーソル移動しない
	 */
	protected void moveCursor(Address pos) {
	}
	/*
	 * 「スリザーリンク」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.NONUMBER);
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}
}
