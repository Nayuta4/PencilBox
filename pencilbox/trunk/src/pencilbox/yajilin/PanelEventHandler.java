package pencilbox.yajilin;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「ヤジリン」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = NULLSTATE; // ドラッグ中の辺の状態を表す
	private static final int NULLSTATE  = -9;
	private static final int PRE_BLACK = -19;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(9);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「ヤジリン」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (board.getNumber(pos) == Board.BLACK)
			currentState = PRE_BLACK;
	}

	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (currentState == PRE_BLACK) {
			currentState = Board.BLACK;
		}
		if (currentState == Board.BLACK) {
			sweepState(dragEnd);
		} else {
			changeLineState(dragStart, dragEnd, Board.LINE);
		}
	}

	protected void leftReleased(Address pos) {
		if (isOn(pos)) {
			if (currentState == NULLSTATE) {
				toggleState(pos, Board.BLACK);
			} else if (currentState == PRE_BLACK) {
				toggleState(pos, Board.BLACK);
			}
		}
		currentState = NULLSTATE;
	}

	protected void rightPressed(Address pos) {
		toggleState(pos, Board.WHITE);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightReleased(Address pos) {
		currentState = NULLSTATE;
	}

	/**
	 * マスの状態を 未定⇔st で切り替える
	 * @param pos マス座標
	 * @param st 切り替える状態
	 */
	private void toggleState(Address pos, int st) {
		if (board.isNumber(pos))
			return;
		if (st == board.getNumber(pos)) {
			st = Board.BLANK;
		}
		if (st == Board.BLACK) {
			board.eraseLinesAround(pos);
		}
		board.changeState(pos, st);
		currentState = st;
	}

	/**
	 * マスの状態を currentState に設定する。ただし、
	 * ・数字マスは変更しない。
	 * ・確定白マスは黒マスを上書きしない。
	 * ・黒マスは確定白マスおよび線を上書きせず、タテヨコに連続しない。
	 * @param pos
	 */
	private void sweepState(Address pos) {
		int st = board.getNumber(pos);
		if (st >=0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		if (currentState == Board.BLACK) {
			if (st == Board.WHITE)
				return;
			if (board.countLine(pos) > 0)
				return;
			if (board.isBlock(pos))
				return;
		} else if (currentState == Board.WHITE) {
			if (st == Board.BLACK)
				return;
		}
		board.changeState(pos, currentState);
	}
	/**
	 * 始点マスと終点マスを結んだ線上の状態を指定の状態に変更する
	 * 始点の辺の現在の状態が指定の状態であれば，未定に変更する
	 * @param pos0 始点マスの座標
	 * @param pos1 終点マスの座標
	 * @param st 変更後の状態
	 */
	private void changeLineState(Address pos0, Address pos1, int st) {
		int direction = pos0.getDirectionTo(pos1);
		if (direction < 0) {
			currentState = st;
			return;
		}
		SideAddress side = SideAddress.get(pos0, direction);
		if (currentState == NULLSTATE) {
			if (board.getState(side) == st) {
				currentState = Board.UNKNOWN;
			} else {
				currentState = st;
			}
		}
		for (Address p = pos0; !p.equals(pos1); p = p.nextCell(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				if (!board.hasNumberOrBlack(side) || st == Board.UNKNOWN)
					board.changeState(side, currentState);
		}
	}
	/*
	 * 「ヤジリン」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.enterNumber(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.enterNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.eraseNumber(pos);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.eraseNumber(posS);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.enterNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.enterNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}
}
