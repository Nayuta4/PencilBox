package pencilbox.hashi;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「橋をかけろ」パネルクラス
 */
public class Panel extends PanelBase {

	static final int HORIZ = Direction.HORIZ;
	static final int VERT = Direction.VERT;
	static final int UP = Direction.UP;
	static final int DN = Direction.DN;
	static final int LT = Direction.LT;
	static final int RT = Direction.RT;

	private Board board;

	private boolean colorForEachLink = false;
	private boolean showNumberHint = false;

	private Color bridgeColor = Color.BLUE;
	private Color crossBridgeColor = Color.RED;
	private Color errorColor = Color.RED;
	private Color successColor = Color.GREEN;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setDisplayStyle(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/**
	 * @return Returns the bridgeColor.
	 */
	public Color getBridgeColor() {
		return bridgeColor;
	}

	/**
	 * @param bridgeColor The bridgeColor to set.
	 */
	public void setBridgeColor(Color bridgeColor) {
		this.bridgeColor = bridgeColor;
	}

	/**
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}

	/**
	 * @param showNumberHint The showNumberHint to set.
	 */
	public void setShowNumberHint(boolean showNumberHint) {
		this.showNumberHint = showNumberHint;
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		if (getDisplayStyle() == 0)
			drawGrid(g); // 罫線なしでもよいが
		drawBoard(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g) {
		g.setFont(getNumberFont());
		g.setColor(getNumberColor());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isPier(r, c)) {
					placeBridgeAndPier(g, r, c, board.getNumber(r, c));
				}
			}
		}
	}
	
	void placeBridgeAndPier(Graphics g, int r0, int c0, int n) {

		Pier pier = board.getPier(r0,c0);
		if (colorForEachLink)
			g.setColor(Colors.getColor(pier.getChain()));
		else
			g.setColor(bridgeColor);
		int r = r0;
		int c = c0;
		if (pier.getNBridge(DN) > 0) {
			while (!board.isPier(++r, c)) {
				placeMidlines(g, r, c, VERT, pier.getNBridge(DN));
			}
		}
		r = r0;
		c = c0;
		if (pier.getNBridge(RT) > 0) {
			while (!board.isPier(r, ++c)) {
				placeMidlines(g, r, c, HORIZ, pier.getNBridge(RT));
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
	void placePier(Graphics g, int r, int c, int n) {
		if (showNumberHint) {
			if (board.checkPier(r, c) < 0) {
				g.setColor(errorColor);
				placeFilledCircle(g, r, c, getCellSize());
			} else if (board.checkPier(r, c) == 0) {
				g.setColor(successColor);
				placeFilledCircle(g, r, c, getCellSize());
			}
		}
		g.setColor(getNumberColor());
		placeCircle(g, r, c, getCellSize());
		if (n <= 8)
			placeNumber(g, r, c, n);
	}
	/**
	 * 橋を配置する
	 * @param g
	 * @param r
	 * @param c
	 */
	void placeBridge(Graphics g, int r, int c) {
		int v = board.getVertBridge(r, c);
		int h = board.getHorizBridge(r, c);
		if (v > 0 && h > 0)
			g.setColor(crossBridgeColor);
		else
			g.setColor(bridgeColor);
		if (v > 0)
			placeMidlines(g, r, c, VERT, v);
		if (h > 0)
			placeMidlines(g, r, c, HORIZ, h);
	}
	/**
	 * マスの中心に横または縦の線を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param dir 横線なら HORIZ 縦線なら VERT
	 * @param n 線の本数(1or2)
	 */
	public void placeMidlines(Graphics g, int r, int c, int dir, int n) {
		if (n == 1) {
			drawMidline(g, toX(c), toY(r), dir);
		} else if (n == 2) {
			drawMidline2(g, toX(c), toY(r), dir);
		}
	}
	/**
	 * 引数の座標を左上角とするセルに，セルの１辺の長さと同じ長さの横または縦の線を描く
	 * @param x
	 * @param y
	 * @param direction 横線か縦線か
	 */
	public void drawMidline(Graphics g, int x, int y, int direction) {
		if (direction == HORIZ) {
			g.fillRect(x, y + getHalfCellSize() - 1, getCellSize() + 1, 3);
		} else if (direction == VERT) {
			g.fillRect(x + getHalfCellSize() - 1, y, 3, getCellSize() + 1);
		}
	}
	/**
	 * 引数の座標を左上角とするセルに，セルの１辺の長さと同じ長さ+2pixelの横または縦の線を2本描く
	 * @param g
	 * @param x
	 * @param y
	 * @param direction 横線か縦線か
	 */
	public void drawMidline2(Graphics g, int x, int y, int direction) {
		if (direction == HORIZ) {
			g.fillRect(x, y + getCellSize() / 3 - 1, getCellSize() + 1, 3);
			g.fillRect(x, y + getCellSize() * 2 / 3 - 1, getCellSize() + 1, 3);
		} else if (direction == VERT) {
			g.fillRect(x + getCellSize() / 3 - 1, y, 3, getCellSize() + 1);
			g.fillRect(x + getCellSize() * 2 / 3 - 1, y, 3, getCellSize() + 1);
		}
	}

}
