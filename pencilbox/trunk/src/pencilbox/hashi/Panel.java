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

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawBridgesAndPiers(g);
		drawBoardBorder(g);
	}

	private void drawBridgesAndPiers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			if (board.isPier(p)) {
				Pier pier = board.getPier(p);
				if (isSeparateLinkColorMode())
					g.setColor(Colors.getColor(pier.getChain()));
				else
					g.setColor(getLineColor());
				for (int d : Direction.DN_RT) {
					int l = pier.getLine(d);
					if (l > 0) {
						Address pp = Address.nextCell(p, d);
						while (!board.isPier(pp)) {
							placeBridge(g, pp, d&1, l);
							pp = Address.nextCell(pp, d);
						}
					}
				}
			}
		}
		for (Address p : board.cellAddrs()) {
			if (board.isPier(p)) {
				if (isIndicateErrorMode()) {
					int check = board.checkNumber(p);
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
				int n = board.getNumber(p);
				if (n >= 1 && n <= 8) {
					placeNumber(g, p, n);
				}
			}
		}
	}
	/**
	 * マスに横または縦の線を配置する
	 * @param g
	 * @param p 盤面座標
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
