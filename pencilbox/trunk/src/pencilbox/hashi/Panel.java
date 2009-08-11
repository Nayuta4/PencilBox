package pencilbox.hashi;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「橋をかけろ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean separateLinkColorMode = false;
	private boolean indicateErrorMode = false;

	private Color lineColor = new Color(0x000099);
	private Color successColor = Color.GREEN;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setGridStyle(0);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
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
	 * @return the separateLinkColorMode
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

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawBridgesAndPiers(g);
		drawBoardBorder(g);
	}

	private void drawBridgesAndPiers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isPier(r, c)) {
					placeBridgeAndPier(g, r, c, board.getNumber(r, c));
				}
			}
		}
	}
	
	void placeBridgeAndPier(Graphics2D g, int r0, int c0, int n) {

		Pier pier = board.getPier(r0,c0);
		if (isSeparateLinkColorMode())
			g.setColor(Colors.getColor(pier.getChain()));
		else
			g.setColor(getLineColor());
		int r = r0;
		int c = c0;
		if (pier.getNBridge(Direction.DN) > 0) {
			while (!board.isPier(++r, c)) {
				placeBridge(g, r, c, Direction.VERT, pier.getNBridge(Direction.DN));
			}
		}
		r = r0;
		c = c0;
		if (pier.getNBridge(Direction.RT) > 0) {
			while (!board.isPier(r, ++c)) {
				placeBridge(g, r, c, Direction.HORIZ, pier.getNBridge(Direction.RT));
			}
		}
		placePier(g, r0, c0, n);
	}
	/**
	 * 橋脚を配置する
	 * @param g
	 * @param r
	 * @param c
	 * @param n
	 */
	void placePier(Graphics2D g, int r, int c, int n) {
		if (isIndicateErrorMode()) {
			int check = board.checkPier(r, c);
			if (check < 0) {
				g.setColor(getErrorColor());
				placeFilledCircle(g, r, c, getCellSize());
			} else if (check == 0) {
				g.setColor(successColor);
				placeFilledCircle(g, r, c, getCellSize());
			}
		}
		g.setColor(getNumberColor());
		placeCircle(g, r, c, getCellSize());
		if (n >= 1 && n <= 8) {
			placeNumber(g, r, c, n);
		}
	}
	/**
	 * マスに横または縦の線を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param dir 横線なら HORIZ 縦線なら VERT
	 * @param n 線の本数(1or2)
	 */
	public void placeBridge(Graphics2D g, int r, int c, int dir, int n) {
		if (n == 1) {
			if (dir == Direction.HORIZ) {
				drawLineSegment(g, toX(c), toY(r) + getHalfCellSize(), dir, 3);
			} else if (dir == Direction.VERT) {
				drawLineSegment(g, toX(c) + getHalfCellSize(), toY(r), dir, 3);
			}
		} else if (n == 2) {
			if (dir == Direction.HORIZ) {
				drawLineSegment(g, toX(c), toY(r) + getCellSize()/3, dir, 3);
				drawLineSegment(g, toX(c), toY(r+1) - getCellSize()/3, dir, 3);
			} else if (dir == Direction.VERT) {
				drawLineSegment(g, toX(c) + getCellSize()/3, toY(r), dir, 3);
				drawLineSegment(g, toX(c+1) - getCellSize()/3, toY(r), dir, 3);
			}
		}
	}
}
