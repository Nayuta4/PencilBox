package pencilbox.numberlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ナンバーリンク」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = INITAL; // ドラッグ中の辺の状態を表す
	private static final int INITAL  = -9;
	private static final int PRESSED = -19;

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
	protected void leftPressed(Address pos) {
		currentState = PRESSED;
	}

	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	/*
	 * クリックしたマスの線がハイライトされる もう１度クリックするとハイライト取り消し
	 */
	protected void leftReleased(Address pos) {
		if (currentState == PRESSED && isOn(pos)) {
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
		currentState = INITAL;
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
		if (currentState == PRESSED) {
			if (board.getState(side) == st) {
				currentState = Board.UNKNOWN;
			} else {
				currentState = st;
			}
		}
		for (Address p = pos0; !p.equals(pos1); p = p.nextCell(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				board.changeState(side, currentState);
		}
	}

	/*
	 * 「ナンバーリンク」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.changeNumber(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.changeNumber(posS, Board.BLANK);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.changeNumber(posS, Board.UNDECIDED_NUMBER);
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
