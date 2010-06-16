package pencilbox.hashi;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
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
		for (Address p : board.cellAddrs()) {
			if (board.isPier(p)) {
				placeBridgeAndPier(g, p, board.getNumber(p));
			}
		}
	}
	
	void placeBridgeAndPier(Graphics2D g, Address p0, int n) {

		Pier pier = board.getPier(p0);
		if (isSeparateLinkColorMode())
			g.setColor(Colors.getColor(pier.getChain()));
		else
			g.setColor(getLineColor());
		for (int d : Direction.DN_RT) {
			Address p = p0;
			int l = pier.getNBridge(d);
			if (l > 0) {
				p = Address.nextCell(p, d);
				while (!board.isPier(p)) {
					placeBridge(g, p, d&1, l);
					p = Address.nextCell(p, d);
				}
			}
		}
		placePier(g, p0, n);
	}
	/**
	 * 橋脚を配置する
	 * @param g
	 * @param p
	 * @param n
	 */
	void placePier(Graphics2D g, Address p, int n) {
		if (isIndicateErrorMode()) {
			int check = board.checkPier(p);
			if (check < 0) {
				g.setColor(getErrorColor());
				placeFilledCircle(g, p, getCellSize());
			} else if (check == 0) {
				g.setColor(successColor);
				placeFilledCircle(g, p, getCellSize());
			}
		}
		g.setColor(getNumberColor());
		placeCircle(g, p, getCellSize());
		if (n >= 1 && n <= 8) {
			placeNumber(g, p, n);
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
	public void placeBridge(Graphics2D g, Address p, int dir, int n) {
		if (n == 1) {
			if (dir == Direction.HORIZ) {
				drawLineSegment(g, toX(p), toY(p) + getHalfCellSize(), dir, getLinkWidth());
			} else if (dir == Direction.VERT) {
				drawLineSegment(g, toX(p) + getHalfCellSize(), toY(p), dir, getLinkWidth());
			}
		} else if (n == 2) {
			if (dir == Direction.HORIZ) {
				drawLineSegment(g, toX(p), toY(p) + getCellSize()/3, dir, getLinkWidth());
				drawLineSegment(g, toX(p), toY(p) + getCellSize() - getCellSize()/3, dir, getLinkWidth());
			} else if (dir == Direction.VERT) {
				drawLineSegment(g, toX(p) + getCellSize()/3, toY(p), dir, getLinkWidth());
				drawLineSegment(g, toX(p) + getCellSize() - getCellSize()/3, toY(p), dir, getLinkWidth());
			}
		}
	}
}
