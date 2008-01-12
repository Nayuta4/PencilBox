package pencilbox.slitherlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
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
	
	public boolean isCursorOnBoard(Address pos) {
		return board.isNumberOn(pos);
	}

	/*
	 * 「スリザーリンク」マウス操作2
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos, Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos, Board.NOLINE);
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
		if (isProblemEditMode()) {
			board.setNumber(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.NONUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.NONUMBER);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	/**
	 * SL用点対称位置の座標を取得する。
	 * @param pos　元座標
	 * @return posと点対称な位置の座標
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-2-pos.r(), board.cols()-2-pos.c());
	}

}
