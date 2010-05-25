package pencilbox.yajilin;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ヤジリン」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;
	private boolean separateLinkColorMode = false;

	private Color circleColor = new Color(0xFF9999);
	private Color lineColor = new Color(0x000099);
	private Color paintColor = new Color(0x0099FF);
	private Color crossColor = new Color(0xFF0099);
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
		for (Address p : board.cellAddrs()) {
			state = board.getNumber(p);
			if (state == Board.BLACK) {
				g.setColor(getPaintColor());
//					if (isIndicateErrorMode()) {
//						if (board.isBlock(p)) {
//							g.setColor(getErrorColor());
//						}
//					}
				paintCell(g, p);
			} else if (state == Board.WHITE) {
				g.setColor(getCircleColor());
				placeMark(g, p);
			} else if (state >= 0) {
				placeArrow(g, p, state);
			} else if (state == Board.UNDECIDED_NUMBER) {
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			}
		}
	}

	private void drawLinks(Graphics2D g) {
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getState(p);
			if (state == Board.LINE) {
				g.setColor(getLineColor());
				if (isSeparateLinkColorMode())
					g.setColor(Colors.getColor(board.getLink(p).getId()));
				placeLink(g, p);
//			} else if (state == Board.NOLINE) {
//				g.setColor(getCrossColor());
//				placeSideCross(g, p);
			}
		}
	}
	/**
	 * マスに矢印付き数字を配置する。
	 * @param g
	 * @param p
	 * @param arrow
	 */
	private void placeArrow(Graphics2D g, Address p, int arrow) {
//		g.setColor(wallColor);
//		paintCell(g, p);
		g.setColor(getNumberColor());
//		placeSquare(g, p);
		if (isIndicateErrorMode()) {
			if (board.checkArrow(p) == 16)
				g.setColor(getErrorColor());
		}
//		g.setColor(wallColor);
//		paintCell(g, p);
//		g.setColor(getNumberColor());
		int direction = (arrow >> 4) & 3;
		int number = arrow & 15;
		String arrowS = getArrowString(direction);
		String numberS = Integer.toString(number);
		int r=p.r(), c=p.c();
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
