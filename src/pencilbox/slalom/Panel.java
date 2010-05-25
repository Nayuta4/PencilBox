package pencilbox.slalom;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;

/**
 * 「スラローム」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;
	private boolean separateLinkColorMode = false;

	private Color gateColor = new Color(0x808080);
	private Color circleColor = Color.BLACK;
	private Color lineColor = new Color(0x000099);
	private Color crossColor = new Color(0xFF0099);
	private Color wallColor = Color.BLACK;

	private int gateLineWidth = 3;
//	private Font smallFont = new Font("SansSerif", Font.BOLD, 14);

	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
		setNumberColor(Color.WHITE);
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
	 * @return Returns the gateColor.
	 */
	public Color getGateColor() {
		return gateColor;
	}

	/**
	 * @param gateColor The gateColor to set.
	 */
	public void setGateColor(Color gateColor) {
		this.gateColor = gateColor;
	}

	/**
	 * @param wallColor the wallColor to set
	 */
	public void setWallColor(Color wallColor) {
		this.wallColor = wallColor;
	}

	/**
	 * @return the wallColor
	 */
	public Color getWallColor() {
		return wallColor;
	}

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawGates(g);
		drawLinks(g);
		drawNumbers(g);
		drawBoardBorder(g);
	}

	private void drawGates(Graphics2D g) {
		int state;
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			state = board.getNumber(p);
			if (state >= 0) {
				placeNumber(g, p, board.getNGate());
			} else if (state == Board.GATE_HORIZ) {
				g.setColor(getGateColor());
				placeGateLine(g, p, Direction.HORIZ);
			} else if (state == Board.GATE_VERT) {
				g.setColor(getGateColor());
				placeGateLine(g, p, Direction.VERT);
			}
//			if (state == Board.GATE_HORIZ || state == Board.GATE_VERT) {
//				g.setColor(getCircleColor());
//				if (board.isGate(p)) {
//					int n = board.getGateNumber(p);
//					if (n >= -1) {
//						placeSmallNumber(g, p, board.getGateNumber(p));
//					} else {
//						placeSmallNumber(g, p, n);
//					}
//				}
//			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		int state;
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
//			if (board.getLink(p) != null) {
//				g.setColor(backLineColor);
//				paintCell(g, p);
//			}
			state = board.getNumber(p);
			if (state >= 0) {
				g.setColor(getWallColor());
				paintCell(g, p);
				if (state > 0) {
					g.setColor(getNumberColor());
					placeNumber(g, p, state);
				}
			} else if (state == Board.GOAL) {
				g.setColor(getBackgroundColor());
				placeFilledCircle(g, p, getCellSize()-2);
				g.setColor(getCircleColor());
//				placeBoldCircle(g, p, getCellSize()-2);
				placeCircle(g, p, getCellSize()-2);
				placeNumber(g, p, board.getNGate());
			}
		}
	}

//	private void placeSmallNumber(Graphics2D g, Address p, int n) {
//		g.setFont(smallFont);
//		g.setColor(Color.RED);
//		placeString(g, p, Integer.toString(n));
//		g.setFont(getNumberFont());
//	}

	private void placeGateLine(Graphics2D g, Address p, int dir) {
//		Stroke s = g.getStroke();
//		g.setStroke(dotStroke);
		if (dir == Direction.HORIZ)
			drawLineSegment(g, toX(p), toY(p) + getHalfCellSize(), dir, gateLineWidth);
		else if (dir == Direction.VERT)
			drawLineSegment(g, toX(p) + getHalfCellSize(), toY(p), dir, gateLineWidth);
//		g.setStroke(s);
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
							g.setColor(Colors.getColor(board.getLink(d, r, c).getId()));
						placeLink(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(getCrossColor());
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
}
