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

	private int currentState = Board.OUTER; // ドラッグ中の辺の状態を表す

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
	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void leftReleased(Address pos) {
		currentState = Board.OUTER;
	}

	protected void leftClicked(Address pos) {
		toggleState(pos, Board.BLACK);
	}

	protected void rightClicked(Address pos) {
		toggleState(pos, Board.WHITE);
	}
	
	/**
	 * マスの状態を 未定⇔st で切り替える
	 * @param pos マス座標
	 * @param st 切り替える状態
	 */
	private void toggleState(Address pos, int st) {
		if (board.isNumber(pos))
			return;
		if (st == board.getNumber(pos))
			st = Board.BLANK;
		if (st == Board.BLACK) {
			board.eraseLinesAroundA(pos);
		}
		board.changeStateA(pos, st);
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
		if (direction < 0)
			return;
		SideAddress side = SideAddress.get(pos0, direction);
		if (currentState == Board.OUTER) {
			if (board.getState(side) == st) {
				currentState = Board.UNKNOWN;
			} else {
				currentState = st;
			}
		}
		for (Address p = pos0; !p.equals(pos1); p.move(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				if (!board.hasNumberOrBlack(side) || st == Board.UNKNOWN)
					board.changeStateA(side, currentState);
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
