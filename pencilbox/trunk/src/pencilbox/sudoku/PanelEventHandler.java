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
			if (!board.isStable(pos)) {
				int n = board.getNumber(pos);
				if (n >= getMaxInputNumber())
					board.enterNumberA(pos, 0);
				else if (n >= 0)
					board.enterNumberA(pos, n + 1);
			}
		}
		setSelectedNumber(board.getNumber(pos));
	}
	
	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isStable(pos)) {
				int n = board.getNumber(pos);
				if (n == 0)
					board.enterNumberA(pos, getMaxInputNumber());
				else if (n > 0)
					board.enterNumberA(pos, n - 1);
			}
		}
		setSelectedNumber(board.getNumber(pos));
	}
	/*
	 * 「数独」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos, num);
				board.setState(pos, Board.STABLE);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!posS.equals(pos))
						if (!board.isStable(posS)) {
							board.setState(posS, Board.STABLE);
							board.changeNumber(posS, Board.UNKNOWN);
						}
				}
			}
		} else if (isCursorOn()){
			if (num >= 0) {
				if (!board.isStable(pos)) {
					board.enterNumberA(pos, num);
				}
			}
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, 0);
			board.setState(pos, Board.UNSTABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (board.isStable(posS)) {
						board.setState(posS, Board.UNSTABLE);
						board.changeNumber(posS, Board.UNKNOWN);
					}
			}
		} else if (isCursorOn()){
			if (!board.isStable(pos)) {
				board.enterNumberA(pos, Board.UNKNOWN);
			}
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.UNKNOWN);
			board.setState(pos, Board.STABLE);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!posS.equals(pos))
					if (!board.isStable(posS)) {
						board.setState(posS, Board.STABLE);
						board.changeNumber(posS, Board.UNKNOWN);
					}
			}
		} 
	}
}
