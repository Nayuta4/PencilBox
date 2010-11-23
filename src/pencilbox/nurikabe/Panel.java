package pencilbox.nurikabe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ぬりかべ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Font countFont = new Font("SansSerif", Font.ITALIC, 13);

	public Panel() {
		setGridColor(Color.BLACK);
		setMarkStyle(3);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	public void setDisplaySize(int size) {
		super.setDisplaySize(size);
		countFont = new Font("SansSerif", Font.ITALIC, getCellSize() / 2);
	}

	public void drawBoard(Graphics2D g) {
		drawCells(g);
		drawGrid(g);
		drawBoardBorder(g);
	}

	private void drawCells(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int st = board.getState(p);
			if (st == Board.WALL) {
				paintWall(g, p);
			} else if (st == Board.SPACE) {
				paintSpace(g, p);
			} else if (st > 0) {
				if (getMarkStyle() == 5) {
					g.setColor(getCircleColor());
					paintCell(g, p);
				}
				g.setFont(getNumberFont());
				g.setColor(getNumberColor());
				placeNumber(g, p, st);
			} else if (st == Board.UNDECIDED_NUMBER) {
				if (getMarkStyle() == 5) {
					g.setColor(getCircleColor());
					paintCell(g, p);
				}
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			}
		}
	}

	void paintSpace(Graphics2D g, Address p) {
		g.setFont(countFont);
		Area area = board.getArea(p);
		int number = area.getNumber();
		if (isCountAreaSizeMode()) {
			if (number == 0 || (number > 0 && area.size() < number) || number == Board.UNDECIDED_NUMBER ) {
				g.setColor(getCircleColor());
				placeCircle(g, p);
				placeNumber(g, p, area.size());
			} else if (
				number == Area.MULTIPLE_NUMBER	|| (number > 0 && area.size() > number)) {
				g.setColor(Color.RED);
				placeCircle(g, p);
				placeNumber(g, p, area.size());
			} else {
				g.setColor(getCircleColor());
				placeMark(g, p);
			}
		} else {
			g.setColor(getCircleColor());
			placeMark(g, p);
		}
	}

	void paintWall(Graphics2D g, Address p) {
		g.setColor(getPaintColor());
		if (isSeparateAreaColorMode()) {
			g.setColor(Colors.get(board.getArea(p).getId()));
		}
//		if (isIndicateError()) {
//			if (board.is2x2Block(p) ) {
//				g.setColor(getErrorColor());
//			}
//		}
		paintCell(g, p);
	}
}
