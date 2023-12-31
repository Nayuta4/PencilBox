package pencilbox.satogaeri;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;



/**
 * 「さとがえり」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Area draggingArea;

	private Color errorColor2;
	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setCursorMode(false);
		setLineColor(new Color(0x0099FF));
		setMarkStyle(2);
		successColor = new Color(0xCCFFFF); // 完成した領域の色
		errorColor2   = new Color(0xFFCCCC); // 
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
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Area draggingArea) {
		this.draggingArea = draggingArea;
	}

	public void setDisplaySize(int cellSize) {
		super.setDisplaySize(cellSize);
	}

	public void drawBoard(Graphics2D g) {
		paintCells(g);
		drawGrid(g);
		drawRoutes(g);
		drawNumbers(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
	}

	private void paintCells(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			if (board.getArea(p) == null) {
				g.setColor(noAreaColor);
				paintCell(g, p);
			} else if (board.getArea(p) == draggingArea) {
				g.setColor(draggingAreaColor);
				paintCell(g, p);
			}
		}
		if (isIndicateErrorMode()) {
			for (Area a : board.getAreaList()) {
				int n = board.checkArea(a, false); // 移動後の数字のみ数える場合
//				int n = board.checkArea(a, true); // 移動前の数字も数える場合
				if (n == 1) {
//					g.setColor(areaPaintColor);
					g.setColor(successColor);
				} else if (n > 1) {
					g.setColor(errorColor2);
//					g.setColor(getBackgroundColor());
				} else {
					g.setColor(getBackgroundColor());
				}
				for (Address p : a) {
					paintCell(g, p);
				}
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int n = board.getNumber(p);
			if (n >= 0 || n == Board.UNDETERMINED) {
				g.setColor(getNumberColor());
				placeCircle(g, p, getCellSize()-2);
				if (isIndicateErrorMode()) {
					if (n >= 0) {
						int ch = board.checkNumber(p);
						if (ch == 0) {
//							g.setColor(successColor);
						} else {
							g.setColor(getErrorColor());
						}
					}
				}
				if (n >= 0) {
					placeNumber(g, p, n);
				}
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
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

	private void drawRoutes(Graphics2D g) {
		g.setColor(getLineColor());
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int state = board.getRoute(p);
			if (state != Board.NOROUTE) {
				placeRoute(g, p, state);
			}
		}
	}

	/**
	 * そのマスから出る線を描く。
	 * @param g
	 * @param p
	 * @param st
	 */
	private void placeRoute(Graphics2D g, Address p, int st) {
		g.setColor(getLineColor());

		if (st == Board.UP || st == Board.LT || st == Board.DN || st == Board.RT) {
			placeLink(g, SideAddress.get(p, st));
		} else if (st == Board.END) {
			int m = getMarkStyle();
			if (m == 2)
				placeFilledCircle(g, p);
			else if (m == 4)
				placeArrow(g, p, board.getIncomingDirection(p));
		}
	}

	private void placeArrow(Graphics2D g, Address p, int dir) {
		g.setColor(getLineColor());
//		Stroke stroke0 = g.getStroke();
//		g.setStroke(new BasicStroke(3.0F));
		int x = toX(p.c()) + getHalfCellSize();
		int y = toY(p.r()) + getHalfCellSize();
		int halfSize = getCellSize()/4;
		if (dir == Direction.UP) {
			drawLine(g, x, y, halfSize, Direction.LTDN);
			drawLine(g, x, y, halfSize, Direction.RTDN);
		} else if (dir == Direction.LT) {
			drawLine(g, x, y, halfSize, Direction.RTUP);
			drawLine(g, x, y, halfSize, Direction.RTDN);
		} else if (dir == Direction.DN) {
			drawLine(g, x, y, halfSize, Direction.LTUP);
			drawLine(g, x, y, halfSize, Direction.RTUP);
		} else if (dir == Direction.RT) {
			drawLine(g, x, y, halfSize, Direction.LTUP);
			drawLine(g, x, y, halfSize, Direction.LTDN);
		} else if (dir == Board.END) {
			drawLine(g, x, y, halfSize, Direction.LTUP);
			drawLine(g, x, y, halfSize, Direction.LTDN);
			drawLine(g, x, y, halfSize, Direction.RTDN);
			drawLine(g, x, y, halfSize, Direction.RTUP);
		}
//		g.setStroke(stroke0);
	}
	private void drawLine(Graphics2D g, int x, int y, int l, int d) {
		if (d == Direction.LTUP) {
			g.drawLine(x+1, y+1, x-l-1, y-l-1);
			g.drawLine(x  , y+1, x-l-1, y-l  );
			g.drawLine(x+1, y  , x-l  , y-l-1);
		} else if (d == Direction.LTDN) {
			g.drawLine(x+1, y-1, x-l  , y+l+1);
			g.drawLine(x  , y-1, x-l-1, y+l  );
			g.drawLine(x+1, y  , x-l  , y+l+1);
		} else if (d == Direction.RTDN) {
			g.drawLine(x-1, y-1, x+l+1, y+l+1);
			g.drawLine(x  , y-1, x+l+1, y+l  );
			g.drawLine(x-1, y  , x+l  , y+l+1);
		} else if (d == Direction.RTUP) {
			g.drawLine(x-1, y+1, x+l+1, y-l-1);
			g.drawLine(x  , y+1, x+l+1, y-l  );
			g.drawLine(x-1, y  , x+l  , y-l-1);
		}
	}
}
