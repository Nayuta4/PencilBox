package pencilbox.goishi;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * 「碁石ひろい」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setGridStyle(1);
		setPaintColor(Color.WHITE);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawNumbers(g);
//		drawBoardBorder(g);
	}

	public void drawGrid(Graphics2D g) {
		g.setColor(getGridColor());
		int style = getGridStyle();
		if (style == 0) {
			return;
		} else {
			for (int r = 0; r < rows(); r++) {
				g.drawLine(
					toX(0) + getHalfCellSize(),
					toY(r) + getHalfCellSize(),
					toX(cols()) - getHalfCellSize(),
					toY(r) + getHalfCellSize());
			}
			for (int c = 0; c < cols(); c++) {
				g.drawLine(
					toX(c) + getHalfCellSize(),
					toY(0) + getHalfCellSize(),
					toX(c) + getHalfCellSize(),
					toY(rows()) - getHalfCellSize());
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			if (board.getState(p) == Board.STONE) {
				g.setColor(getPaintColor());
				placeFilledCircle(g, p);
				int n = board.getNumber(p);
				if (n > 0) {
					g.setColor(getInputColor());
					placeNumber(g, p, n);
				} else {
					g.setColor(getNumberColor());
					placeBoldCircle(g, p);
				}
			}
		}
	}
}
