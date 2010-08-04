package pencilbox.shikaku;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「四角に切れ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int pivotR = -1;  // ドラッグ時に固定する頂点の行座標
	private int pivotC = -1;  // ドラッグ時に固定する頂点の列座標
//	private Square draggingSquare; // ドラッグして今まさに描こうとしている四角
	private int dragState = 0;
	private int currentState = -1;
	private Address pos3 = Address.NOWHERE;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「四角に切れ」マウス操作
	 * 
	 * 左ドラッグボタンを離して四角を確定する
	 * 既存の四角がなければ，新しい四角を作る
	 * 既存の四角があってはじめのマスから動かずにボタンを離したなら，その四角を消す
	 * 既存の四角があってはじめのマスと別のマスでボタンを離したなら，四角を変更する
	 * 既存の四角があってはじめのマスに戻ってボタンを離したなら，何もしない
	 */
	protected void leftPressed(Address pos) {
		Square draggingSquare;
		Square sq = board.getSquare(pos);
		if (sq == null) { // 始点に四角がない場合，新しく四角を作る
			draggingSquare = new Square(pos, pos);
		} else { // 始点に既存の四角がある場合，その四角を変更する
			draggingSquare = new Square(sq);
			dragState = 1; // ドラッグ開始
		}
		fixPivot(draggingSquare, pos);
		setDraggingSquare(draggingSquare);
	}

	protected void leftDragged(Address pos) {
		Square draggingSquare = getDraggingSquare();
		if (draggingSquare == null) {
			return;
		}
		if (pivotR >= 0 && pivotC >= 0) {
			draggingSquare.set(pivotR, pivotC, pos.r(), pos.c());
		} else if (pivotR >= 0 && pivotC == -1) {
			draggingSquare.set(pivotR, draggingSquare.c0(), pos.r(), draggingSquare.c1());
		} else if (pivotR == -1 && pivotC >= 0) {
			draggingSquare.set(draggingSquare.r0(), pivotC, draggingSquare.r1(), pos.c());
		} else if (pivotR == -1 && pivotC == -1) {
//			draggingSquare.set(draggingSquare.r0, draggingSquare.c0, draggingSquare.r1, drggingSquare.c1());
		}
		dragState = 2; //ドラッグ中
		fixPivot(draggingSquare, pos); // もし現在座標が四角の端であれば，固定頂点を更新
	}

	/**
	 * ドラッグするときの固定される点を設定する。
	 * もしもドラッグしたマスが既存の四角の外周ならば，そちら側が動き，反対側が固定となる。
	 * すでに固定される点が決まっていれば，何もしない。
	 * @param s
	 * @param p
	 */
	private void fixPivot(Square s, Address p) {
		if (pivotR == -1) {
			if (p.r() == s.r0()) {
				pivotR = s.r1();
			} else if (p.r() == s.r1()) {
				pivotR = s.r0();
			}
		}
		if (pivotC == -1) {
			if (p.c() == s.c0()) {
				pivotC = s.c1();
			} else if (p.c() == s.c1()) {
				pivotC = s.c0();
			}
		}
	}

	protected void leftReleased(Address pos) {
		Square draggingSquare = getDraggingSquare();
		if (draggingSquare == null)
			return;
		int rp = pivotR >= 0 ? pivotR : draggingSquare.r0();
		int cp = pivotC >= 0 ? pivotC : draggingSquare.c0();
		Square sq = board.getSquare(rp, cp);
		if (sq == null) {
			board.removeOverlappedSquares(draggingSquare, null);
			board.addSquare(new Square(draggingSquare));
		} else {
			if (dragState == 1 && isOn(pos)) {
				board.removeSquare(sq);
			} else if (sq.equals(draggingSquare)) {
				;
			} else {
				board.removeOverlappedSquares(draggingSquare, sq);
				board.changeSquare(sq, draggingSquare.p0(), draggingSquare.p1());
			}
		}
		setDraggingSquare(null);
		resetPivot();
		dragState = 0;
	}

//	protected void rightPressed(Address pos) {
//		Square s = board.getSquare(pos);
//		if(s != null) {
//			board.removeSquare(s);
//		}
//	}

//	protected void rightDragged(Address pos) {
//		Square s = board.getSquare(pos);
//		if(s != null) {
//			board.removeSquare(s);
//		}
//	}

	private void resetPivot() {
		pivotR = -1;
		pivotC = -1;
	}

	protected void rightPressed3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		this.pos3 = sa;
	}

	protected void rightDragged3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		if (pos3.equals(sa))
			return;
		else if (pos3.r() != sa.r() && pos3.c() != sa.c())
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
	 * 「四角に切れ」キー操作
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
			board.changeNumber(pos, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.changeNumber(posS, 0);
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

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		((Panel)getPanel()).setDraggingSquare(draggingSquare);
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return ((Panel)getPanel()).getDraggingSquare();
	}
}
