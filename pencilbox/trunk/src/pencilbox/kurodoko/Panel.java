package pencilbox.kurodoko;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * 「黒マスはどこだ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;

	private Color paintColor = new Color(0x0099FF);
	private Color circleColor = new Color(0xFF9999);
//	private Color successColor = new Color(0x00FF00);

	public Panel() {
		setGridColor(Color.BLACK);
		setMarkStyle(1);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}
	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}
	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}
	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}
	/**
	 * @return Returns the indicateErrorMode.
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
		drawCells(g);
		drawGrid(g);
		drawBoardBorder(g);
	}

	private void drawCells(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int st = board.getState(r, c);
				if (st == Board.BLACK) {
					paintCell1(g, r, c);
				} else if (st == Board.WHITE) {
					g.setColor(getCircleColor());
					placeMark(g, r, c);
				} else if (st > 0 || st == Board.UNDECIDED_NUMBER) {
					placeNumber1(g, r, c, st);
				}
			}
		}
	}
	
	private void paintCell1(Graphics2D g, int r, int c) {
		g.setColor(getPaintColor());	
		if (isIndicateErrorMode()) {
			if (board.isBlock(r,c) || board.getChain(r,c) < 0) {
				g.setColor(getErrorColor());
			}
		}
		paintCell(g, r, c);
	}

	private void placeNumber1(Graphics2D g, int r, int c, int num) {
		if (getMarkStyle() == 5) {
			g.setColor(getCircleColor());
			paintCell(g, r, c);
		}
//		g.setColor(getBackgroundColor());
//		if (isIndicateErrorMode()) {
//			if (num > 0) {
//				int nSpace = board.getSumSpace(r,c);
//				int nWhite = board.getSumWhite(r,c);
//				if (nSpace < num) {
//					g.setColor(getErrorColor());
//				} else if (nSpace == num) {
//					g.setColor(successColor);
//				} else if (nWhite > num) {
//					g.setColor(getErrorColor());
//				}
//			}
//		}
//		placeFilledCircle(g, r, c, getCellSize()-2);
		g.setColor(getNumberColor());
		placeCircle(g, r, c, getCellSize()-2);
		if (num > 0) {
			if (isIndicateErrorMode()) {
				int nSpace = board.getSumSpace(r,c);
				if (nSpace != num) {
					g.setColor(getErrorColor());
				}
			}
			placeNumber(g, r, c, num);
		}
	}
}
