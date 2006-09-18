package pencilbox.sudoku;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「数独」マウス／キー操作処理クラス
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
		setMaxInputNumber(board.rows());
	}
	
	private void setSelectedNumber(int n) {
		((Panel) getPanel()).setSelectedNumber(n);
	}

	/*
	 * 「数独」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isStable(pos.r(), pos.c()))
				board.increaseNumber(pos.r(), pos.c());
		}
		setSelectedNumber(board.getNumber(pos.r(), pos.c()));
	}
	
	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isStable(pos.r(), pos.c()))
				board.decreaseNumber(pos.r(), pos.c());
		}
		setSelectedNumber(board.getNumber(pos.r(), pos.c()));
	}
	/*
	 * 「数独」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos.r(), pos.c(), num);
				board.setState(pos.r(), pos.c(), Board.STABLE);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!posS.equals(pos))
						if (!board.isStable(posS.r(), posS.c())) {
							board.setState(posS.r(), posS.c(), Board.STABLE);
							board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
						}
				}
			}
		} else if (isCursorOn()){
			if (num > 0) {
				if (!board.isStable(pos.r(), pos.c())) {
					board.enterNumberA(pos.r(), pos.c(), num);
				}
			}
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (board.isStable(posS.r(), posS.c())) {
						board.setState(posS.r(), posS.c(), Board.UNSTABLE);
						board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
					}
			}
		} else if (isCursorOn()){
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), Board.UNKNOWN);
			}
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r(), pos.c(), Board.UNKNOWN);
			board.setState(pos.r(), pos.c(), Board.STABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (!board.isStable(posS.r(), posS.c())) {
						board.setState(posS.r(), posS.c(), Board.STABLE);
						board.changeNumber(posS.r(), posS.c(), Board.UNKNOWN);
					}
			}
		} 
	}
}
