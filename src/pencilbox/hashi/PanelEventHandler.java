package pencilbox.hashi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「橋をかけろ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;
	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(8);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「橋をかけろ」マウス操作
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart.r(), dragStart.c()))
			return;
		if (!board.isPier(dragStart.r(), dragStart.c()))
			return;
		if (dragStart.r() == dragEnd.r()) {
			if (dragStart.c() < dragEnd.c()) {
				board.addBridgeA(dragStart.r(), dragStart.c(), RT);
			} else if (dragStart.c() > dragEnd.c()) {
				board.addBridgeA(dragStart.r(), dragStart.c(), LT);
			}
		} else if (dragStart.c() == dragEnd.c()) {
			if (dragStart.r() < dragEnd.r()) {
				board.addBridgeA(dragStart.r(), dragStart.c(), DN);
			} else if (dragStart.r() > dragEnd.r()) {
				board.addBridgeA(dragStart.r(), dragStart.c(), UP);
			}
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (!board.isOn(dragStart.r(), dragStart.c()))
			return;
		if (!board.isPier(dragStart.r(), dragStart.c()))
			return;
		if (dragStart.r() == dragEnd.r()) {
			if (dragStart.c() < dragEnd.c()) {
				board.removeBridgeA(dragStart.r(), dragStart.c(), RT);
			} else if (dragStart.c() > dragEnd.c()) {
				board.removeBridgeA(dragStart.r(), dragStart.c(), LT);
			}
		} else if (dragStart.c() == dragEnd.c()) {
			if (dragStart.r() < dragEnd.r()) {
				board.removeBridgeA(dragStart.r(), dragStart.c(), DN);
			} else if (dragStart.r() > dragEnd.r()) {
				board.removeBridgeA(dragStart.r(), dragStart.c(), UP);
			}
		}
	}

	/*
	 * 「橋をかけろ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num>=1 && num<=8)
				board.setNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}

}
