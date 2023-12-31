package pencilbox.fillomino;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * 「フィルオミノ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int state; // ドラッグ中の数字を表す
	private int currentState = -1; // ドラッグ中の辺の状態を表す
	private int dragState = 0; // ドラッグ中の辺の状態を表す
	private Address pos3 = Address.NOWHERE;
	private Address selectedPos = Address.NOWHERE;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「フィルオミノ」マウス操作
	 * 
	 * マスでボタンを押すとそのマスの数字を覚えて， その後ドラッグしたら通過したマスをその数字に合わせる
	 * マスから移動せずにボタンを離したら，数字を1ずつ増やしていく。
	 * 他のマスに移動すると数字はリセットされる
	 */
	protected void leftPressed(Address pos) {
		state = board.getNumberOrState(pos);
		dragState = 1; // ドラッグ開始
	}

	protected void leftDragged(Address oldPos, Address pos) {
		if (!board.isStable(pos))
			board.changeAnswerNumber(pos, state);
		dragState = 2; // ドラッグ中
	}

	protected void leftReleased(Address pos) {
		if (dragState == 1 && isOn(pos)) {
			if (!board.isStable(pos)) {
				int n = board.getState(pos);
				if (n == 0 || selectedPos.equals(pos)) {
					board.changeAnswerNumber(pos, n + 1);
				} else {
					board.changeAnswerNumber(pos, 0);
				}
				selectedPos = pos;
			}
		}
		dragState = 0;
	}

	public void mouseMoved(MouseEvent e) {
		Address p = pointToAddress(e);
		if (! p.equals(selectedPos)) {
			selectedPos = Address.NOWHERE;
		}
	}

//	protected void rightPressed(Address pos) {
//		if (!board.isStable(pos))
//			board.changeAnswerNumber(pos, 0);
//	}

//	protected void rightDragged(Address oldPos, Address pos) {
//		if (!board.isStable(pos))
//			board.changeAnswerNumber(pos, 0);
//	}

	protected void rightPressed3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
//		System.out.println(sa.toString());
		this.pos3 = sa;
	}

	protected void rightDragged3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		if (pos3.equals(sa))
			return;
		if (pos3.r() != sa.r() && pos3.c() != sa.c())
			return;
		int dir = pos3.getDirectionTo(sa);
		Address sb = Address.nextCell(pos3, dir);
		SideAddress b = superAddress2SideAddress(sb);
		if (! isSideOn(b))
			return;
		sweepEdgeState(b, Board.LINE);
		this.pos3 = sa;
	}

	protected void rightReleased(Address pos) {
		currentState = -1;
	}

	private void sweepEdgeState(SideAddress side, int st) {
		if (currentState == -1) {
			if (board.getEdge(side) == Board.LINE) {
				currentState = Board.NOLINE;
			} else {
				currentState = Board.LINE;
			}
		}
		board.changeEdge(side, currentState);
	}

	/*
	 * 「フィルオミノ」キー操作
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
		} else if (isCursorOn()) {
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
		} else if (isCursorOn()) {
			if (!board.isStable(pos)) {
				board.changeAnswerNumber(pos, 0);
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
