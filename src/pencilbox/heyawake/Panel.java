package pencilbox.heyawake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandler;


/**
 *  「へやわけ」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private int maxNumber = 9;  // キー入力可能数字9までとする

	private Board board;

	private boolean warnWrongWall = false;
	private boolean showContinuousRoom = false;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
	private Color noRoomColor = new Color(0xCCCCCC);
	private Color roomBorderColor = Color.BLACK;
	private Color continuousRoomColor = new Color(0x800000);
	private Color errorColor = Color.RED;
	private Color showContinuousWhiteColor = Color.RED;

	/**
	 * 
	 */
	public Panel() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(maxNumber);   // 暫定的
	}

	/**
	 * @param showContinuousRoom The showContinuousRoom to set.
	 */
	public void setShowContinuousRoom(boolean showContinuousRoom) {
		this.showContinuousRoom = showContinuousRoom;
	}

	/**
	 * @param warnWrongWall The warnWrongWall to set.
	 */
	public void setWarnWrongWall(boolean warnWrongWall) {
		this.warnWrongWall = warnWrongWall;
	}

	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}

	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}

	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}

	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		if (isProblemEditMode())
			drawDragging(g);
		if (getCellCursor() != null)
			drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * できれば，左上マスに数字が表示されるようにしたいが・・・
	 * @param g
	 */
	void drawBoard(Graphics g) {
		
		int st;
		Square square;
		g.setFont(getNumberFont());

		if (showContinuousRoom) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					g.setColor(continuousRoomColor); 
					if (board.contWH[r][c] >= 3) {
						g.setColor(showContinuousWhiteColor);
					}
					if (board.contH[r][c] >= 3) {
						placeMidline(g,r,c,Direction.HORIZ);
					}
					g.setColor(continuousRoomColor); 
					if (board.contWV[r][c] >= 3) {
						g.setColor(showContinuousWhiteColor);
					}
					if (board.contV[r][c] >= 3) {
						placeMidline(g,r,c,Direction.VERT);
					}
				}
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				st = board.getState(r, c);
				if (board.getSquare(r,c)  == null) {
					g.setColor(noRoomColor);
					paintCell(g, r, c);
				}
				if (st == Board.BLACK) {
//					g.setColor(paintColor);
					g.setColor(paintColor);
					if (warnWrongWall && (board.isBlock(r,c) || board.chain[r][c]==-1)) {
						g.setColor(errorColor);
					}
					paintCell(g, r, c);
				}
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				st = board.getState(r, c);
				if (st == Board.WHITE) {
//					g.setColor(circleColor);
					g.setColor(circleColor);
//					if (board.hcontw[r][c] >= 3) {
//						g.setColor(errorCircleColor);
//					}
//					if (board.vcontw[r][c] >= 3) {
//						g.setColor(errorCircleColor);
//					}
					placeCircle(g, r, c);
				}
			}
		}

		for (Iterator itr = board.getSquareListIterator(); itr.hasNext();) {
			g.setColor(roomBorderColor);
			square = (Square) itr.next();
			placeSquare(g, square.r0, square.c0, square.r1, square.c1);
			g.setColor(getNumberColor());
			if (square.getNumber() >= 0) {
				if (warnWrongWall && square.getNumber() < square.getNBlack()) {
					g.setColor(Color.WHITE);
					placeFilledCircle(g, square.r0, square.c0);
					g.setColor(errorColor);
			}
				placeNumber(g, square.r0, square.c0, square.getNumber());
			}
		}
	}
	/**
	 *  ドラッグ中の四角を描画する
	 * @param g
	 */
	void drawDragging(Graphics g) {
		Square square = draggingSquare;
		if (square == null)
			return;
		g.setColor(roomBorderColor);
		placeSquare(g, square.r0, square.c0, square.r1, square.c1);
	}

	/*
	 * 「へやわけ」マウス操作
	 * 解答入力用
	 * 左プレス：未定⇔黒マス
	 * 右プレス：未定⇔白マス
	 * 右ドラッグ：はじめにボタンを押したマスときの状態にあわせる
	 */
	/*
	 * 「へやわけ」マウス操作
	 * 問題入力用
	 * 左ドラッグ：ドラッグ始点と終点を２つの頂点とする長方形を描く
	 * 右プレス，ドラッグ：そのマスを含む長方形を消去する
	 */

	private Square draggingSquare;
	private Address dragStart = new Address(-1, -1);
	private int currentState = Board.UNKNOWN;
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			dragStart.set(dragEnd);
			draggingSquare =
				new Square(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c());
		} else {
			board.toggleState(pos.r(), pos.c(), Board.BLACK);
		}
	}
	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			//			dragStart.set(dragEnd);
			board.removeSquareIncluding(dragEnd.r(), dragEnd.c());
		} else {
			board.toggleState(pos.r(), pos.c(), Board.WHITE);
			currentState = board.getState(pos.r(), pos.c());
		}
	}
	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
			if (draggingSquare == null)
				return;
			draggingSquare.set(dragStart.r(), dragStart.c(), pos.r(), pos.c());
		}
	}
	protected void leftDragFixed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			if (draggingSquare == null)
				return;
			draggingSquare = null;
			board.addSquareSpanning(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c());
			dragStart.setNowhere();
		}
	}
	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			board.removeSquareIncluding(dragEnd.r(), dragEnd.c());
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st == currentState)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}
	protected void dragFailed() {
		draggingSquare = null;
		dragStart.setNowhere();
	}
	/*
	 * 「へやわけ」キー操作
	 * 
	 * 問題入力モードのときのみ，0以上の数字入力により部屋の数字設定
	 */
	protected void numberEntered(Address pos, int num) {
		if (!isProblemEditMode())
			return;
		Square square = board.getSquare(pos.r(), pos.c());
		if (square != null)
			square.setNumber(num);
	}
	protected void spaceEntered(Address pos) {
		if (!isProblemEditMode())
			return;
		Square square = board.getSquare(pos.r(), pos.c());
		if (square != null)
			square.setNumber(Square.ANY);
	}
}

