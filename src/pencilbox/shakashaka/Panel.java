package pencilbox.shakashaka;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * 「シャカシャカ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	/**
	 * Panel を生成する
	 */
	public Panel() {
		setMarkStyle(3);
		setGridColor(Color.GRAY);
		setNumberColor(Color.WHITE);
		setPaintColor(new Color(0x000099));
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	public void drawBoard(Graphics2D g) {
		drawCells(g);
		drawGrid(g);
		drawBoardBorder(g);
	}

	private void drawCells(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int number = board.getNumber(p);
			g.setColor(getWallColor());
			if (Board.isNumber(number)) {
				paintCell(g, p);
				if (number >= 0 && number <= 4) {
					g.setColor(getNumberColor());
					if (isIndicateErrorMode()) {
						if (board.countAdjacentTriangles(p) != number) {
							g.setColor(getErrorColor());
						}
					}
					placeNumber(g, p, number);
				}
			} else {
				if (isIndicateErrorMode()) {
					int cra = board.getArea(p).checkRectangleArea();
					if (cra > 0) {
						g.setColor(successColor);
						paintCell(g, p);
					}
				}
				int state = board.getState(p);
				if (Board.isTriangle(state)) {
					g.setColor(getPaintColor());
					paintTriangle(g, p, state);
				} else if (state == Board.WHITE) {
					g.setColor(getCircleColor());
					placeMark(g, p);
				}
			}
		}
	}

	/**
	 * 三角形を塗る
	 * @param g
	 * @param p　マスの座標
	 * @param state 塗る三角形のタイプ
	 */
	public void paintTriangle(Graphics2D g, Address p, int state) {
		int x0 = toX(p.c());
		int x1 = x0 + getCellSize();
		int y0 = toX(p.r());
		int y1 = y0 + getCellSize();
		if (state == Board.LTUP) {
			g.fillPolygon(new int[]{x1, x0, x0}, new int[]{y0, y0, y1}, 3);
		} else if (state == Board.LTDN) {
			g.fillPolygon(new int[]{x0, x0, x1}, new int[]{y0, y1, y1}, 3);
		} else if (state == Board.RTDN) {
			g.fillPolygon(new int[]{x0, x1, x1}, new int[]{y1, y1, y0}, 3);
		} else if (state == Board.RTUP) {
			g.fillPolygon(new int[]{x1, x1, x0}, new int[]{y1, y0, y0}, 3);
		}
	}
}
