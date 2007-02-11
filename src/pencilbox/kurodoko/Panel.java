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

	private boolean showNumberHint = false;
	private boolean warnWrongWall = false;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
	private Color successColor = new Color(0x00FF00);
	private Color errorColor = new Color(0xFF0000);
	private Color error2Color = new Color(0xFFFF00);

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
	 * @return Returns the warnWrongWall.
	 */
	public boolean isWarnWrongWall() {
		return warnWrongWall;
	}
	/**
	 * @param warnWrongWall The warnWrongWall to set.
	 */
	public void setWarnWrongWall(boolean warnWrongWall) {
		this.warnWrongWall = warnWrongWall;
	}
	/**
	 * @return Returns the showNumberHint.
	 */
	public boolean isShowNumberHint() {
		return showNumberHint;
	}
	/**
	 * @param showNumberHint The showNumberHint to set.
	 */
	public void setShowNumberHint(boolean showNumberHint) {
		this.showNumberHint = showNumberHint;
	}

	public void drawPanel(Graphics2D g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	protected void drawBoard(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int st = board.getState(r, c);
				if (st == Board.BLACK) {
					paintCell1(g, r, c);
				} else if (st == Board.WHITE) {
					g.setColor(circleColor);
					placeMark(g, r, c);
				} else if (st > 0 || st == Board.UNDECIDED_NUMBER) {
					placeNumber1(g, r, c, st);
				}
			}
		}
	}
	
	private void paintCell1(Graphics2D g, int r, int c) {
		g.setColor(paintColor);	
		if (isWarnWrongWall()) {
			if (board.isBlock(r,c) || board.getChain(r,c) < 0) {
				g.setColor(errorColor);
			}
		}
		paintCell(g, r, c);
	}

	private void placeNumber1(Graphics2D g, int r, int c, int num) {
		if (getMarkStyle() == 5) {
			g.setColor(circleColor);
			paintCell(g, r, c);
		}
		g.setColor(getBackgroundColor());
		if (isShowNumberHint()) {
			if (num > 0) {
				int nSpace = board.getSumSpace(r,c);
				int nWhite = board.getSumWhite(r,c);
				if (nSpace < num) {
					g.setColor(errorColor);
				} else if (nSpace == num) {
					g.setColor(successColor);
				} else if (nWhite > num) {
					g.setColor(error2Color);
				}
			}
		}
		placeFilledCircle(g, r, c, getCellSize()-2);
		g.setColor(getNumberColor());
		placeCircle(g, r, c, getCellSize()-2);
		if (num > 0) {
			placeNumber(g, r, c, num);
		}
	}
}
