package pencilbox.bijutsukan;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;


/**
 * 「美術館」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	/**
	 * Panel を生成する
	 */
	public Panel() {
		setMarkStyle(3);
		setNumberColor(Color.WHITE);
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
			int state = board.getState(p);
			int l = board.getHorizIlluminated(p);
			int m = board.getVertIlluminated(p);
			if (isPaintIlluminatedCellMode()) {
				if (l>0 || m>0) {
					g.setColor(getIlluminatedCellColor());
					paintCell(g,p);
				}
			}
			if (state >= 0 && state <= 4) {
				g.setColor(getWallColor());
				paintCell(g, p);
				g.setColor(getNumberColor());
				if (isIndicateErrorMode()) {
					if (board.checkAdjacentBulbs(p) <= 0) {
						g.setColor(getErrorColor());
					}
				}
				placeNumber(g, p, state);
			} else if (state == Board.NONUMBER_WALL) {
				g.setColor(getWallColor());
				paintCell(g, p);
			} else if (state == Board.BULB) {
				g.setColor(getBulbColor());
				if (isIndicateErrorMode()) {
					if (board.isMultiIlluminated(p)) {
						g.setColor(getErrorColor());
					}
				}
				placeFilledCircle(g, p);
			} else if (state == Board.NOBULB) {
				g.setColor(getNoBulbColor());
				placeMark(g, p);
			}
			if (isShowBeamMode()) {
				g.setColor(getBulbColor());
				if (l > 0) {
					if (isIndicateErrorMode() && l > 1) {
						g.setColor(getErrorColor());
					}
					placeCenterLine(g, p, Direction.HORIZ);
				}
				g.setColor(getBulbColor());
				if (m > 0) {
					if (isIndicateErrorMode() && m > 1) {
						g.setColor(getErrorColor());
					}
					placeCenterLine(g, p, Direction.VERT);
				}
			}
		}
	}

}
