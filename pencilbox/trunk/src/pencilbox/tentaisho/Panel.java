package pencilbox.tentaisho;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;


/**
 * 「天体ショー」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean hideStarMode = false;
	private boolean showAreaBorderMode = true;
	private boolean indicateErrorMode = false;
	private boolean separateAreaColorMode = false;

	private Color areaBorderColor = new Color(0x000099);
	private Color noStarAreaColor = new Color(0xFFFF99);
	private Color whiteAreaColor = new Color(0xAAFFFF);
	private Color blackAreaColor = new Color(0xFFAAFF);
	private Color draggingAreaColor = new Color(0xCCFFFF);
	private Color starColor = Color.BLACK;
	private Color borderColor = new Color(0xFF0099);

	private int halfStarSize = 3;

	private Area draggingArea;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/**
	 * @return the draggingArea
	 */
	Area getDraggingArea() {
		return draggingArea;
	}
	/**
	 * @param draggingArea The draggingArea to set.
	 */
	void setDraggingArea(Area draggingArea) {
		this.draggingArea = draggingArea;
	}

	/**
	 * @return the halfStarSize
	 */
	public int getHalfStarSize() {
		return halfStarSize;
	}

	/**
	 * @param halfStarSize the halfStarSize to set
	 */
	public void setHalfStarSize(int halfStarSize) {
		this.halfStarSize = halfStarSize;
	}

	/**
	 * @return Returns the areaBorderColor.
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
	}

	/**
	 * @param areaBorderColor The areaBorderColor to set.
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return Returns the borderColor.
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param borderColor The borderColor to set.
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @return Returns the blackAreaColor.
	 */
	public Color getBlackAreaColor() {
		return blackAreaColor;
	}

	/**
	 * @param blackAreaColor The blackAreaColor to set.
	 */
	public void setBlackAreaColor(Color blackAreaColor) {
		this.blackAreaColor = blackAreaColor;
	}

	/**
	 * @return Returns the whiteAreaColor.
	 */
	public Color getWhiteAreaColor() {
		return whiteAreaColor;
	}

	/**
	 * @param whiteAreaColor The whiteAreaColor to set.
	 */
	public void setWhiteAreaColor(Color whiteAreaColor) {
		this.whiteAreaColor = whiteAreaColor;
	}

	/**
	 * @return the showAreaBorderMode
	 */
	public boolean isShowAreaBorderMode() {
		return showAreaBorderMode;
	}

	/**
	 * @param showAreaBorderMode The showAreaBorderMode to set.
	 */
	public void setShowAreaBorderMode(boolean showAreaBorderMode) {
		this.showAreaBorderMode = showAreaBorderMode;
	}

	/**
	 * @return the hideStarMode
	 */
	public boolean isHideStarMode() {
		return hideStarMode;
	}

	/**
	 * @param hideStarMode The hideStarMode to set.
	 */
	public void setHideStarMode(boolean hideStarMode) {
		this.hideStarMode = hideStarMode;
	}

	/**
	 * @return the indicateErrorMode
	 */
	public boolean isIndicateErrorMode() {
		return indicateErrorMode;
	}

	/**
	 * @param indicateErrorMode the indicateErrorMode to set.
	 */
	public void setIndicateErrorMode(boolean indicateErrorMode) {
		this.indicateErrorMode = indicateErrorMode;
	}

	/**
	 * @return Returns the separateAreaColorMode.
	 */
	public boolean isSeparateAreaColorMode() {
		return separateAreaColorMode;
	}

	/**
	 * @param separateAreaColorMode The separateAreaColorMode to set.
	 */
	public void setSeparateAreaColorMode(boolean separateAreaColorMode) {
		this.separateAreaColorMode = separateAreaColorMode;
	}

	public void setDisplaySize(int cellSize) {
		halfStarSize = (int) (cellSize * 0.15);
		super.setDisplaySize(cellSize);
	}
	
	public void drawBoard(Graphics2D g) {
		paintAreas(g);
		drawGrid(g);
		if (!isHideStarMode()) {
			drawStars(g);
		}
		if (isShowAreaBorderMode()) {
			drawAreaBorders(g);
		}
		drawEdges(g);
		drawBoardBorder(g);
	}

	private void paintAreas(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			if (board.getArea(p) != null) {
				if (board.getArea(p) == draggingArea) {
					g.setColor(draggingAreaColor);
				} else {
					int starType = board.getArea(p).getStarType();
					g.setColor(whiteAreaColor);
					if (isSeparateAreaColorMode()) {
						if (starType == -1) {
							g.setColor(getErrorColor());
						} else if (starType == Board.WHITESTAR) {
							g.setColor(whiteAreaColor);
						} else if (starType == Board.BLACKSTAR) {
							g.setColor(blackAreaColor);
						} else {
							g.setColor(noStarAreaColor);
						} 
					}
					if (isIndicateErrorMode()) {
						if (starType == -1) {
							g.setColor(getErrorColor());
						} else if (starType == 0) {
							g.setColor(noStarAreaColor);
						} else if (!board.getArea(p).isPointSymmetry()) {
							g.setColor(getErrorColor());
						}
					}
				}
				paintCell(g, p);
			}
		}
	}
	
	private void drawStars(Graphics2D g) {
		for (int r = 0; r < board.rows() * 2 - 1; r++) {
			for (int c = 0; c < board.cols() * 2 - 1; c++) {
				int n = board.getStar(r, c);
				if (n > 0)
					placeStar(g, r, c, n);
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(areaBorderColor);
		for (Address p : board.cellAddrs()) {
			for (int d : Direction.DN_RT) {
				Address p1 = p.nextCell(d);
				SideAddress b = SideAddress.get(p, d);
				if (board.isSideOn(b)) {
					if (board.getArea(p) != board.getArea(p1)) {
						placeSideLine(g, b);
					}
				}
			}
		}
	}

	private void drawEdges(Graphics2D g) {
		g.setColor(borderColor);
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getEdge(p);
			if (state == Board.LINE) {
				placeSideLine(g, p);
			}
		}
	}
	/**
	 * 星を配置する
	 * @param g
	 * @param r
	 * @param c
	 * @param star
	 */
	public void placeStar(Graphics2D g, int r, int c, int star) {
		if (star == Board.WHITESTAR) {
			if (isSeparateAreaColorMode()) {
				g.setColor(getWhiteAreaColor());
			} else {
				g.setColor(getBackgroundColor());
			}
		} else if (star == Board.BLACKSTAR) {
			if (isSeparateAreaColorMode()) {
				g.setColor(getBlackAreaColor());
			} else {
				g.setColor(starColor);
			}
		}
		fillCircle(g, getOffsetx() + getCellSize()*(c+1)/2, getOffsety() + getCellSize()*(r+1)/2, getHalfStarSize());
		g.setColor(starColor);
		drawCircle(g, getOffsetx() + getCellSize()*(c+1)/2, getOffsety() + getCellSize()*(r+1)/2, getHalfStarSize());
	}
	/**
	 * 天体ショー専用カーソルを描く
	 * @param g
	 */
	public void drawCursor(Graphics2D g) {
		if (isProblemEditMode()) {
			g.setColor(getCursorColor());
			g.drawRect(
				getOffsetx() + (getCellSize() * getCellCursor().c() + getHalfCellSize()) / 2,
				getOffsety() + (getCellSize() * getCellCursor().r() + getHalfCellSize()) / 2,
				getHalfCellSize(),
				getHalfCellSize());
		}
	}
}
