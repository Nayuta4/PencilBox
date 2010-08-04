package pencilbox.heyawake;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 *  「へやわけ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int pivotR = -1;  // ドラッグ時に固定する頂点の行座標
	private int pivotC = -1;  // ドラッグ時に固定する頂点の列座標
//	private Square draggingSquare; // ドラッグして今まさに描こうとしている四角
	private int dragState = 0;
	private int currentState = Board.UNKNOWN;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	protected int getMaxInputNumber() {
		Address pos = getCellCursor().getPosition();
		Square square = board.getSquare(pos);
		if (square != null)
			return square.mx();
		return 0;
	}
	/*
	 * 「へやわけ」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Square draggingSquare;
			Square sq = board.getSquare(pos);
			if (sq == null) {
				draggingSquare = new Square(pos, pos);
			} else {
				draggingSquare = new Square(sq);
				dragState = 1; // ドラッグ開始
			}
			fixPivot(draggingSquare, pos);
			setDraggingSquare(draggingSquare);
		} else {
			toggleState(pos, Board.BLACK);
			currentState = board.getState(pos);
		}
	}

	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
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
//				draggingSquare.set(draggingSquare.r0, draggingSquare.c0, draggingSquare.r1, drggingSquare.c1());
			}
			dragState = 2; //ドラッグ中
			fixPivot(draggingSquare, pos);
		} else {
			sweepState(pos);
		}
	}

	private void fixPivot(Square s, Address pos) {
		int r = pos.r();
		int c = pos.c();
		if (pivotR == -1) {
			if (r == s.r0()) {
				pivotR = s.r1();
			} else if (r == s.r1()) {
				pivotR = s.r0();
			}
		}
		if (pivotC == -1) {
			if (c == s.c0()) {
				pivotC = s.c1();
			} else if (c == s.c1()) {
				pivotC = s.c0();
			}
		}
	}

	protected void leftReleased(Address pos) {
		if (isProblemEditMode()) {
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
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
//			Square sq = board.getSquare(pos);
//			if (sq != null)
//				board.removeSquare(sq);
		} else {
			toggleState(pos, Board.WHITE);
			currentState = board.getState(pos);
		}
	}

	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
//			Square sq = board.getSquare(pos);
//			if (sq != null)
//				board.removeSquare(sq);
		} else {
			sweepState(pos);
		}
	}

	private void resetPivot() {
		pivotR = -1;
		pivotC = -1;
	}

	/**
	 * マスの状態を 未定⇔st と変更する
	 * @param pos マスの座標
	 * @param st 切り替える状態
	 */
	private void toggleState(Address pos, int st){
		if (st == board.getState(pos))
			st = Board.UNKNOWN;
		board.changeState(pos, st);
	}

	private void sweepState(Address pos) {
		int st = board.getState(pos);
		if (currentState == st)
			return;
		if (currentState == Board.BLACK && board.isBlock(pos))
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * 「へやわけ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, num);
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Square.ANY);
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		((Panel) getPanel()).setDraggingSquare(draggingSquare);
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return ((Panel) getPanel()).getDraggingSquare();
	}
}
