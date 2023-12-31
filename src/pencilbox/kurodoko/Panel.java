package pencilbox.kurodoko;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * 「黒マスはどこだ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	public Panel() {
		setGridColor(Color.BLACK);
		setMarkStyle(3);
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
			int st = board.getState(p);
			if (st == Board.BLACK) {
				paintCell1(g, p);
			} else if (st == Board.WHITE) {
				g.setColor(getCircleColor());
				placeMark(g, p);
			} else if (st > 0 || st == Board.UNDECIDED_NUMBER) {
				placeNumber1(g, p, st);
			}
		}
	}

	private void paintCell1(Graphics2D g, Address p) {
		g.setColor(getPaintColor());	
		if (isIndicateErrorMode()) {
			if (board.isBlock(p) || board.getChain(p) < 0) {
				g.setColor(getErrorColor());
			}
		}
		paintCell(g, p);
	}

	private void placeNumber1(Graphics2D g, Address p, int num) {
		if (getMarkStyle() == 5) {
			g.setColor(getCircleColor());
			paintCell(g, p);
		}
//		g.setColor(getBackgroundColor());
//		if (isIndicateErrorMode()) {
//			if (num > 0) {
//				int nSpace = board.getSumSpace(p);
//				int nWhite = board.getSumWhite(p);
//				if (nSpace < num) {
//					g.setColor(getErrorColor());
//				} else if (nSpace == num) {
//					g.setColor(successColor);
//				} else if (nWhite > num) {
//					g.setColor(getErrorColor());
//				}
//			}
//		}
//		placeFilledCircle(g, p, getCellSize()-2);
		g.setColor(getNumberColor());
		placeCircle(g, p, getCellSize()-2);
		if (num > 0) {
			if (isIndicateErrorMode()) {
				int nSpace = board.getNumber(p).getNSpace();
				if (nSpace != num) {
					g.setColor(getErrorColor());
				}
			}
			placeNumber(g, p, num);
		}
	}
}
