package pencilbox.heyawake;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;


/**
 *  「へやわけ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;
//	private boolean indicateContinuousRoomMode = false;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
	private Color noAreaColor = new Color(0xC0C0C0);
	private Color areaBorderColor = Color.BLACK;
	private Color continuousRoomColor = new Color(0x800000);

	private Square draggingSquare;

	/**
	 * 
	 */
	public Panel() {
		setMarkStyle(1);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return the indicateErrorMode
	 */
	public boolean isIndicateErrorMode() {
		return indicateErrorMode;
	}

	/**
	 * @param indicateErrorMode The indicateErrorMode to set.
	 */
	public void setIndicateErrorMode(boolean indicateErrorMode) {
		this.indicateErrorMode = indicateErrorMode;
	}

//	/**
//	 * @return the indicateContinuousRoomMode
//	 */
//	public boolean isIndicateContinuousRoomMode() {
//		return indicateContinuousRoomMode;
//	}
//
//	/**
//	 * @param indicateContinuousRoomMode The indicateContinuousRoomMode to set.
//	 */
//	public void setIndicateContinuousRoomMode(boolean indicateContinuousRoomMode) {
//		this.indicateContinuousRoomMode = indicateContinuousRoomMode;
//	}

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

	/**
	 * @param areaBorderColor the areaBorderColor to set
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return the areaBorderColor
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
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
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getSquare(r,c)  == null) {
					paintCell(g, r, c);
				}
			}
		}
	}

	private void drawCells(Graphics2D g) {
		int st;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				st = board.getState(r, c);
				if (st == Board.BLACK) {
					g.setColor(getPaintColor());
					if (isIndicateErrorMode()) {
						if (board.isBlock(r,c) || board.chain[r][c]==-1) {
							g.setColor(getErrorColor());
						}
					}
					paintCell(g, r, c);
				} else if (st == Board.WHITE) {
					g.setColor(getCircleColor());
					placeMark(g, r, c);
				}
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Square square : board.getSquareList()) {
			g.setColor(getAreaBorderColor());
			placeSquare(g, square.r0(), square.c0(), square.r1(), square.c1());
			if (square.getNumber() >= 0) {
				g.setColor(getNumberColor());
				if (isIndicateErrorMode()) {
					if (square.getNumber() != square.getNBlack()) {
//						g.setColor(Color.WHITE);
//						placeFilledCircle(g, square.r0, square.c0);
						if (board.isBlock(square.r0(), square.c0()) || board.chain[square.r0()][square.c0()]==-1) {
							;
						} else {
							g.setColor(getErrorColor());
						}
					}
				}
				placeNumber(g, square.r0(), square.c0(), square.getNumber());
			}
		}
		Square square = getDraggingSquare();
		if (square != null	) {
			g.setColor(areaBorderColor);
			placeSquare(g, square.r0(), square.c0(), square.r1(), square.c1());
		}
	}

	private void drawBeams(Graphics2D g) {
		if (isIndicateErrorMode()) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
//					if (board.contWH[r][c] >= 3) {
//						g.setColor(getErrorColor());
//						placeCenterLine(g,r,c,Direction.HORIZ);
//					} else
					if (board.contH[r][c] >= 3) {
						g.setColor(continuousRoomColor); 
						placeCenterLine(g,r,c,Direction.HORIZ);
					}
//					if (board.contWV[r][c] >= 3) {
//						g.setColor(getErrorColor());
//						placeCenterLine(g,r,c,Direction.VERT);
//					} else
					if (board.contV[r][c] >= 3) {
						g.setColor(continuousRoomColor); 
						placeCenterLine(g,r,c,Direction.VERT);
					}
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
