package pencilbox.yajilin;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * 「ヤジリン」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean warnBranchedLink = false;
	private boolean colorForEachLink = false;

	private Color whiteColor = Color.MAGENTA;
//	private Color backBlackColor = new Color(0xFFCCCC);
	private Color lineColor = Color.BLUE;
	private Color blackColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color errorColor = Color.RED;
	
	/**
	 * @return Returns the colorForEachLink.
	 */
	public boolean isColorForEachLink() {
		return colorForEachLink;
	}

	/**
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}

	/**
	 * @return Returns the warnBranchedLink.
	 */
	public boolean isWarnBranchedLink() {
		return warnBranchedLink;
	}

	/**
	 * @param warnBranchedLink The warnBranchedLink to set.
	 */
	public void setWarnBranchedLink(boolean warnBranchedLink) {
		this.warnBranchedLink = warnBranchedLink;
	}

	/**
	 * @return Returns the blackColor.
	 */
	public Color getBlackColor() {
		return blackColor;
	}

	/**
	 * @param blackColor The blackColor to set.
	 */
	public void setBlackColor(Color blackColor) {
		this.blackColor = blackColor;
	}

	/**
	 * @return Returns the crossColor.
	 */
	public Color getCrossColor() {
		return crossColor;
	}

	/**
	 * @param crossColor The crossColor to set.
	 */
	public void setCrossColor(Color crossColor) {
		this.crossColor = crossColor;
	}

	/**
	 * @return Returns the lineColor.
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor The lineColor to set.
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * @return Returns the whiteColor.
	 */
	public Color getWhiteColor() {
		return whiteColor;
	}

	/**
	 * @param whiteColor The whiteColor to set.
	 */
	public void setWhiteColor(Color whiteColor) {
		this.whiteColor = whiteColor;
	}

	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
		setMaxInputNumber(9);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		if (getCellCursor() != null && isProblemEditMode())
			drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g) {
		int state;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				state = board.getNumber(r, c);
//				if (board.hasLine(r,c)){
//					g.setColor(backLineColor);
//					paintCell(g,r,c);
//				}
				if (state == Board.BLACK) {
//					g.setColor(backBlackColor);
//					paintCell(g, r, c);
					g.setColor(blackColor);
					if (warnBranchedLink && board.isBlock(r,c)) {
						g.setColor(errorColor);
					}
					paintCell(g, r, c);
//					g.setColor(whiteColor);
//					placeCross(g, r, c);
				} else if (state == Board.WHITE) {
					g.setColor(whiteColor);
					placeCircle(g, r, c);
				} else if (state >= 0) {
//					g.setColor(arrowBackColor);
//					paintCell(g, r, c);
					g.setColor(getNumberColor());
					placeArrow(g, r, c, state);
//					g.drawRect(toX(c) + 1,	toY(r) + 1, cellSize - 2, cellSize - 2);
				} else if (state == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(lineColor);
						if (colorForEachLink)
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
						if (warnBranchedLink && board.isBranchedLink(d,r,c))
							g.setColor(errorColor);
						placeTraversalLine(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	/**
	 * @param g
	 * @param r
	 * @param c
	 * @param arrow
	 */
	private void placeArrow(Graphics g, int r, int c, int arrow) {
		placeSquare(g, r,c,r,c);
		g.setColor(Color.BLACK);
//		paintCell(g,r,c);
		int direction = (arrow >> 4) & 3;
		int number = arrow & 15;
//		g.setColor(Color.WHITE);
		drawArrow(g, r, c, number, direction);
	}
	
	/**
	 * @param g
	 * @param r
	 * @param c
	 * @param number
	 * @param direction
	 */
	private void drawArrow(Graphics g, int r, int c, int number, int direction) {
		char arrowChar = 0;
		if (direction == Direction.UP) {
			arrowChar = '↑';
		} else if (direction == Direction.DN) {
			arrowChar = '↓';
		} else if (direction == Direction.LT) {
			arrowChar = '←';
		} else if (direction == Direction.RT) {
			arrowChar = '→';
		}
		String arrowS = Character.toString(arrowChar);
		String numberS = Integer.toString(number);
		FontMetrics metrics = g.getFontMetrics();
		if ((direction & 1) == Direction.VERT) {
			g.drawString(
				arrowS,
				(toX(c) + (getCellSize() - 1 - metrics.charWidth(arrowChar)) / 2 + 1 +(getHalfCellSize()*4/5)),
				(toY(r)	+ (getCellSize() - 1 - metrics.getHeight()) / 2 + metrics.getAscent())+ 1);
		}
		else if ((direction & 1) == Direction.HORIZ) {
			g.drawString(
				arrowS,
				(toX(c) + (getCellSize() - 1 - metrics.charWidth(arrowChar)) / 2 + 1),
				(toY(r)	+ (getCellSize() - 1 - metrics.getHeight()) / 2 + metrics.getAscent())+ 1-(getHalfCellSize()*4/5));
		}
		g.drawString(
			numberS,
			(toX(c) + (getCellSize() - 1 - metrics.stringWidth(numberS)) / 2 + 1),
			(toY(r) + (getCellSize() - 1 - metrics.getHeight()) / 2 + metrics.getAscent()) + 1);
}
	/*
	 * 「ヤジリン」マウスリスナー
	 * 頂点Aから頂点Bへドラッグしたとき，
	 * AとBが同一行または列にあれば，
	 * 左ドラッグ： AからBまで線を引く
	 * 右ドラッグ： AからBまで線を消す
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r == dragEnd.r || dragStart.c == dragEnd.c) {
			board.determineInlineState(dragStart, dragEnd, Board.LINE);
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r == dragEnd.r || dragStart.c == dragEnd.c) {
			board.determineInlineState(dragStart, dragEnd, Board.UNKNOWN);
		}
	}

	protected void leftClicked(Address pos) {
		board.toggleState(pos.r, pos.c, Board.BLACK);
	}

	protected void rightClicked(Address position) {
		board.toggleState(position.r, position.c, Board.WHITE);
	}
	/*
	 * 「ヤジリン」キーリスナー
	 * 
	 * 問題入力モードのときのみ数字入力
	 * 0-: 数字入力
	 * SPACE: 矢印の向きを変更
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			board.enterNumber(pos.r, pos.c, num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.eraseNumber(pos.r, pos.c);
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.enterNumber(pos.r, pos.c, Board.UNDECIDED_NUMBER);
	}
}
