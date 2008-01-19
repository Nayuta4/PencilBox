package pencilbox.masyu;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「ましゅ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(2);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * ましゅ 辺に対するマウス操作
	 */
	protected void leftClickedEdge(SideAddress pos) {
		toggleState(pos, Board.LINE);
	}

	protected void rightClickedEdge(SideAddress pos) {
		toggleState(pos, Board.NOLINE);
	}
	/*
	 * ましゅ マスに対するマウス操作
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.UNKNOWN);
	}

	/**
	 * 辺の状態を 未定⇔st で切り替える
	 * @param pos 辺座標
	 * @param st 切り替える状態
	 */
	private void toggleState(SideAddress pos, int st) {
		if (st == board.getState(pos))
			st = Board.UNKNOWN;
		board.changeStateA(pos, st);
	}
	/**
	 * 始点マスと終点マスを結んだ線上の状態を指定の状態に変更する
	 * 始点マスと終点マスは同じ行または同じ列になければならない
	 * @param pos0 始点マスの座標
	 * @param pos1 終点マスの座標
	 * @param st 変更後の状態
	 */
	private void changeLineState(Address pos0, Address pos1, int st) {
		int direction = pos0.getDirectionTo(pos1);
		if (direction < 0)
			return;
		for (Address p = pos0; !p.equals(pos1); p.move(direction)) {
			SideAddress side = SideAddress.get(p, direction);
			if (board.getState(side) != st)
				board.changeStateA(side, st);
		}
	}

	/*
	 * 「ましゅ」キー操作 
	 */
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode()) {
			if(n == 1 || n == 2) {
				board.setNumber(pos, n);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.setNumber(posS, Board.GRAY_PEARL);
				}
			}
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.NO_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.NO_PEARL);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.GRAY_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.GRAY_PEARL);
			}
		}
	}
}
