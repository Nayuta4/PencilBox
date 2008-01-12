package pencilbox.masyu;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ましゅ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(2);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * ましゅ 辺に対するマウス操作
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos, Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos, Board.NOLINE);
	}
	/*
	 * ましゅ マスに対するマウス操作
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
	 * 「ましゅ」キー操作 
	 */
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode()) {
			if(n == 1 || n == 2) {
				board.setNumber(pos, n);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.setNumber(posS, Board.GRAY_PEARL);
				}
			}
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.NO_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.NO_PEARL);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.GRAY_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.GRAY_PEARL);
			}
		}
	}
}
