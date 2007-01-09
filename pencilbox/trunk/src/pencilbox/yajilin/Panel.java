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

	private boolean warnBranchedLink = false;
	private boolean colorForEachLink = false;

	private Color circleColor = Color.MAGENTA;
	private Color lineColor = Color.BLUE;
	private Color paintColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color errorColor = Color.RED;
//	private Color wallColor = new Color(0xC0C0C0);
	
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

	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	public void drawPanel(Graphics2D g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	protected void drawBoard(Graphics2D g) {
		int state;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				state = board.getNumber(r, c);
				if (state == Board.BLACK) {
					g.setColor(paintColor);
					if (isWarnBranchedLink()) {
						if (board.isBlock(r,c)) {
							g.setColor(errorColor);
						}
					}
					paintCell(g, r, c);
				} else if (state == Board.WHITE) {
					g.setColor(circleColor);
					placeCircle(g, r, c);
				} else if (state >= 0) {
					g.setColor(getNumberColor());
					placeArrow(g, r, c, state);
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
						if (isColorForEachLink())
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
						if (isWarnBranchedLink()) {
							if (board.isBranchedLink(d,r,c) || board.isBuriedLink(d,r,c))
								g.setColor(errorColor);
						}
						placeLink(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
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
		placeSquare(g, r, c, r, c);
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
