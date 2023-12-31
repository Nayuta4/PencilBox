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
				int n = board.getState(pos);
				if (n >= getMaxInputNumber())
					board.changeAnswerNumber(pos, 0);
				else if (n >= 0)
					board.changeAnswerNumber(pos, n + 1);
			}
		}
		setSelectedNumber(board.getNumberOrState(pos));
	}

	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isStable(pos)) {
				int n = board.getState(pos);
				if (n == 0)
					board.changeAnswerNumber(pos, getMaxInputNumber());
				else if (n > 0)
					board.changeAnswerNumber(pos, n - 1);
			}
		}
		setSelectedNumber(board.getNumberOrState(pos));
	}
	/*
	 * 「数独」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeFixedNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isStable(posS)) {
						board.changeFixedNumber(posS, Board.UNDETERMINED);
					}
				}
			}
		} else if (isCursorOn()){
			if (num >= 0) {
				if (!board.isStable(pos)) {
					board.changeAnswerNumber(pos, num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isStable(posS)) {
					board.changeFixedNumber(posS, Board.BLANK);
				}
			}
		} else if (isCursorOn()){
			if (!board.isStable(pos)) {
				board.changeAnswerNumber(pos, Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.UNDETERMINED);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isStable(posS)) {
					board.changeFixedNumber(posS, Board.UNDETERMINED);
				}
			}
		}
	}
}
