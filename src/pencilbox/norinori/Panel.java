package pencilbox.norinori;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;



/**
 * 「のりのり」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Area draggingArea;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setMarkStyle(3);
		successColor = new Color(0xCCEECC); // 完成した領域の色
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

	public void drawBoard(Graphics2D g) {
		paintAreas(g);
		drawCells(g);
		drawGrid(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
	}

	private void paintAreas(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			if (board.getArea(p) == null) {
				g.setColor(noAreaColor);
				paintCell(g, p);
			} else if (board.getArea(p) == draggingArea) {
				g.setColor(draggingAreaColor);
				paintCell(g, p);
			} else if (isIndicateErrorMode()) {
				int s = board.getArea(p).getBlock().size();
				if (s == 2) { // 完成した領域の黒マス以外のマスを塗る
					g.setColor(successColor);
//				} else if (s > 2) {
//					g.setColor(getErrorColor());
//				} else if (s < 2) {
//					g.setColor(smallSizeColor);
				} else {
					g.setColor(getBackgroundColor());
				}
				paintCell(g, p);
			}
		}
	}

	/**
	 * マスの状態を描画する
	 * @param g
	 */
	protected void drawCells(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int st = board.getState(p);
			if (st == Board.BLACK) {
				g.setColor(getPaintColor());
				if (isIndicateErrorMode()) {
					if (board.getArea(p) != null) {
						int s = board.getArea(p).getBlock().size();
						if (s > 2) {
							g.setColor(getErrorColor());
//						} else if (s < 2) {
//							g.setColor(smallSizeColor);
						}
					}
				}
				paintCell(g, p);
			} else if (st == Board.WHITE) {
				g.setColor(getCircleColor());
				placeMark(g, p);
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

}
