package pencilbox.heyawake;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;


/**
 *  「へやわけ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color continuousRoomColor = new Color(0x800000);
	private Square draggingSquare;

	/**
	 * 
	 */
	public Panel() {
		setMarkStyle(3);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	public void drawBoard(Graphics2D g) {
		paintAreas(g);
		drawCells(g);
		drawBeams(g);
		drawGrid(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
	}
	
	private void paintAreas(Graphics2D g) {
		g.setColor(noAreaColor);
		for (Address p : board.cellAddrs()) {
			if (board.getSquare(p)  == null) {
				paintCell(g, p);
			}
		}
		Square square = getDraggingSquare();
		if (square != null) {
			g.setColor(draggingAreaColor);
			for (int r = square.r0(); r <= square.r1(); r++) {
				for (int c = square.c0(); c <= square.c1(); c++) {
					paintCell(g, r, c);
				}
			}
		}
	}

	private void drawCells(Graphics2D g) {
		int st;
		for (Address p : board.cellAddrs()) {
			st = board.getState(p);
			if (st == Board.BLACK) {
				g.setColor(getPaintColor());
				if (isIndicateErrorMode()) {
					if (board.isBlock(p) || board.getChain(p)==-1) {
						g.setColor(getErrorColor());
					}
				}
				paintCell(g, p);
			} else if (st == Board.WHITE) {
				g.setColor(getCircleColor());
				placeMark(g, p);
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Square square : board.getSquareList()) {
			g.setColor(getAreaBorderColor());
			placeSquare(g, square);
			if (square.getNumber() >= 0) {
				g.setColor(getNumberColor());
				if (isIndicateErrorMode()) {
					if (square.getNumber() != square.getNBlack()) {
//						g.setColor(Color.WHITE);
//						placeFilledCircle(g, square.r0, square.c0);
						if (board.isBlock(square.p0()) || board.getChain(square.p0())==-1) {
							;
						} else {
							g.setColor(getErrorColor());
						}
					}
				}
				placeNumber(g, square.p0(), square.getNumber());
			}
		}
		Square square = getDraggingSquare();
		if (square != null	) {
			g.setColor(areaBorderColor);
			placeSquare(g, square);
		}
	}

	private void drawBeams(Graphics2D g) {
		if (isIndicateErrorMode()) {
			for (Address p : board.cellAddrs()) {
				if (board.getCont(p, Direction.HORIZ) >= 3) {
					g.setColor(continuousRoomColor);
					placeCenterLine(g,p,Direction.HORIZ);
				}
				if (board.getCont(p, Direction.VERT) >= 3) {
					g.setColor(continuousRoomColor);
					placeCenterLine(g,p,Direction.VERT);
				}
			}
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		this.draggingSquare = draggingSquare;
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return draggingSquare;
	}

}
