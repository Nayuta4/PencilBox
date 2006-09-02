package pencilbox.numberlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ナンバーリンク」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;
//
//	private Link selectedLink = null;
//	private int selectedNumber = 0;  // 選択されていないときは 0

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
	 * 辺に対して操作をする
	 */
//	protected void leftClicked(int dir, Address pos) {
//		board.toggleState(dir, pos.r, pos.c, Board.LINE);
//	}
//	protected void rightClickedEdge(int dir, Address pos) {
//		board.toggleState(dir, pos.r, pos.c, Board.NOLINE);
//	}

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

		Link link = board.getLink(pos.r(), pos.c());
		int newNumber = 0;

		if (board.isNumber(pos.r(), pos.c()))
			newNumber = board.getNumber(pos.r(), pos.c());
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
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), num);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
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
