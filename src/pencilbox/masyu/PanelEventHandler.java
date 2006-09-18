package pencilbox.masyu;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�܂���v�}�E�X�^�L�[���쏈���N���X
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
	 * �܂��� �ӂɑ΂���}�E�X����
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.NOLINE);
	}
	/*
	 * �܂��� �}�X�ɑ΂���}�E�X����
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
	 * �u�܂���v�L�[���� 
	 */
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode()) {
			if(n == 1 || n == 2) {
				board.setNumber(pos.r(), pos.c(), n);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS.r(), posS.c()))
						board.setNumber(posS.r(), posS.c(), Board.GRAY_PEARL);
				}
			}
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.NO_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS.r(), posS.c()))
					board.setNumber(posS.r(), posS.c(), Board.NO_PEARL);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.GRAY_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS.r(), posS.c()))
					board.setNumber(posS.r(), posS.c(), Board.GRAY_PEARL);
			}
		}
	}
}
