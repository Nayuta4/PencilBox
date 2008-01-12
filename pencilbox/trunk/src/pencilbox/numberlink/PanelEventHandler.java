package pencilbox.numberlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ナンバーリンク」マウス／キー操作処理クラス
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
	}
	
	/*
	 * 「ナンバーリンク」マウス操作
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
	 * クリックしたマスの線がハイライトされる もう１度クリックするとハイライト取り消し
	 */
	protected void leftClicked(Address pos) {

		Link link = board.getLink(pos);
		int newNumber = 0;

		if (board.isNumber(pos))
			newNumber = board.getNumber(pos);
		else {
			if(link!=null)
				newNumber = link.getNumber();
		}

		if (newNumber == getSelectedNumber() && getSelectedLink() == link) {
			setSelectedLink(null);
			setSelectedNumber(0);
		} else {
			setSelectedLink(link);
			setSelectedNumber(newNumber);
		}
	}

	/*
	 * 「ナンバーリンク」キー操作
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
			board.setNumber(pos, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.BLANK);
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

	private Link getSelectedLink() {
		return ((Panel) getPanel()).getSelectedLink();
	}

	private void setSelectedLink(Link l) {
		((Panel) getPanel()).setSelectedLink(l);
	}
	
	private int getSelectedNumber() {
		return ((Panel) getPanel()).getSelectedNumber();
	}
	
	private void setSelectedNumber(int n) {
		((Panel) getPanel()).setSelectedNumber(n);
	}

}
