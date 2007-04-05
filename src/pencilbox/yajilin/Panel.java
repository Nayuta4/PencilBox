package pencilbox.yajilin;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ヤジリン」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;
	private boolean separateLinkColorMode = false;

	private Color circleColor = Color.MAGENTA;
	private Color lineColor = Color.BLUE;
	private Color paintColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
//	private Color wallColor = new Color(0xC0C0C0);
	
	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
		setMarkStyle(3);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the separateLinkColorMode.
	 */
	public boolean isSeparateLinkColorMode() {
		return separateLinkColorMode;
	}

	/**
	 * @param separateLinkColorMode The separateLinkColorMode to set.
	 */
	public void setSeparateLinkColorMode(boolean separateLinkColorMode) {
		this.separateLinkColorMode = separateLinkColorMode;
	}

	/**
	 * @return Returns the indicateErrorMode.
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

	/**
	 * @return Returns the blackColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}

	/**
	 * @param blackColor The blackColor to set.
	 */
	public void setPaintColor(Color blackColor) {
		this.paintColor = blackColor;
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
	public Color getCircleColor() {
		return circleColor;
	}

	/**
	 * @param whiteColor The whiteColor to set.
	 */
	public void setCircleColor(Color whiteColor) {
		this.circleColor = whiteColor;
	}

	public void drawBoard(Graphics2D g) {
		drawNumbers(g);
		drawGrid(g);
		drawLinks(g);
		drawBoardBorder(g);
	}

	private void drawNumbers(Graphics2D g) {
		int state;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				state = board.getNumber(r, c);
				if (state == Board.BLACK) {
					g.setColor(getPaintColor());
//					if (isIndicateErrorMode()) {
//						if (board.isBlock(r, c)) {
//							g.setColor(getErrorColor());
//						}
//					}
					paintCell(g, r, c);
				} else if (state == Board.WHITE) {
					g.setColor(getCircleColor());
					placeMark(g, r, c);
				} else if (state >= 0) {
					placeArrow(g, r, c, state);
				} else if (state == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
	}

	private void drawLinks(Graphics2D g) {
		int state;
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(getLineColor());
						if (isSeparateLinkColorMode())
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
//						if (isIndicateErrorMode()) {
//							if (board.isBranchedLink(d,r,c))
//								g.setColor(getErrorColor());
//							else if (board.isBuriedLink(d,r,c))
//								g.setColor(getErrorColor());
//						}
						placeLink(g, d, r, c);
//					} else if (state == Board.NOLINE) {
//						g.setColor(getCrossColor());
//						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	/**
	 * マスに矢印付き数字を配置する。
	 * @param g
	 * @param r
	 * @param c
	 * @param arrow
	 */
	private void placeArrow(Graphics2D g, int r, int c, int arrow) {
//		g.setColor(wallColor);
//		paintCell(g, r, c);
		g.setColor(getNumberColor());
//		placeSquare(g, r, c, r, c);
		if (isIndicateErrorMode()) {
			if (board.checkArrow(r, c) == 16)
				g.setColor(getErrorColor());
		}
//		g.setColor(wallColor);
//		paintCell(g, r, c);
//		g.setColor(getNumberColor());
		int direction = (arrow >> 4) & 3;
		int number = arrow & 15;
		String arrowS = getArrowString(direction);
		String numberS = Integer.toString(number);
		if (direction == Direction.UP || direction == Direction.DN) {
			drawString(g, toX(c+1) - getCellSize()*1/6, toY(r) + getHalfCellSize(), arrowS);
			drawString(g, toX(c) + getHalfCellSize() - getCellSize()*1/12, toY(r) + getHalfCellSize(), numberS);
		} else if  (direction == Direction.LT || direction == Direction.RT) {
			drawString(g, toX(c) + getHalfCellSize(), toY(r) + getCellSize()*1/6, arrowS);
			drawString(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize() + getCellSize()*1/12, numberS);
		}
	}
	
	private String getArrowString(int direction) {
		switch (direction) {
		case Direction.UP:
			return "↑";
		case Direction.DN:
			return "↓";
		case Direction.LT:
			return "←";
		case Direction.RT:
			return "→";
		default:
			return "";
		}
	}
}
