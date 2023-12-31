package pencilbox.nurikabe;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ぬりかべ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = Board.UNKNOWN;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「ぬりかべ」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
		} else {
			toggleState(pos, Board.WALL);
			if (board.isNumber(pos)) {
				currentState = Board.WALL;
			} else {
				currentState = board.getState(pos);
			}
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
		} else {
			toggleState(pos, Board.SPACE);
			if (board.isNumber(pos)) {
				currentState = Board.SPACE;
			} else {
				currentState = board.getState(pos);
			}
		}
	}

	protected void leftDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			if (isOn(dragStart)) {
				int number = board.getState(dragStart);
				if (number > 0 || number == Board.UNDECIDED_NUMBER){
					board.changeState(dragStart, Board.UNKNOWN);
					board.changeState(pos, number);
				}
			}
		} else {
			sweepState(pos);
		}
	}
	protected void rightDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
		} else {
			sweepState(pos);
		}
	}

	/**
	 * マスの状態を 未定 ⇔ st と変更する
	 * @param pos マス座標
	 * @param st 切り替える状態
	 */
	private void toggleState(Address pos, int st) {
		if (board.isNumber(pos))
			return;
		if (st == board.getState(pos)) {
			st = Board.UNKNOWN;
		}
		board.changeState(pos, st);
	}

	private void sweepState(Address pos) {
		if (board.isNumber(pos))
			return;
		if (currentState == board.getState(pos))
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * 「ぬりかべ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeState(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.changeState(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.UNKNOWN);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.changeState(posS, Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeState(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.changeState(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

}
