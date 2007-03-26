package pencilbox.nurikabe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ぬりかべ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;

	private Font countFont = new Font("SansSerif", Font.ITALIC, 13);

	private boolean separateAreaColorMode = false;
//	private boolean indicateErrorMode = false;
	private boolean countAreaSizeMode = false;

	public Panel() {
		setGridColor(Color.BLACK);
		setMarkStyle(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}
	/**
	 * @return the countAreaSizeMode
	 */
	public boolean isCountAreaSizeMode() {
		return countAreaSizeMode;
	}
	/**
	 * @param countAreaSizeMode The countAreaSizeMode to set.
	 */
	public void setCountAreaSizeMode(boolean countAreaSizeMode) {
		this.countAreaSizeMode = countAreaSizeMode;
	}
//	/**
//	 * @return the indicateErrorMode
//	 */
//	public boolean isIndicateError() {
//		return indicateErrorMode;
//	}
//	/**
//	 * @param indicateErrorMode The indicateErrorMode to set.
//	 */
//	public void setIndicateError(boolean indicateErrorMode) {
//		this.indicateErrorMode = indicateErrorMode;
//	}
	/**
	 * @return the separateAreaColorMode
	 */
	public boolean isSeparateAreaColorMode() {
		return separateAreaColorMode;
	}
	/**
	 * @param separateAreaColorMode The separateAreaColorMode to set.
	 */
	public void setSeparateAreaColorMode(boolean separateAreaColorMode) {
		this.separateAreaColorMode = separateAreaColorMode;
	}
	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}
	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}
	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}
	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}

	public void setDisplaySize(int size) {
		super.setDisplaySize(size);
		countFont = new Font("SansSerif", Font.ITALIC, getCellSize() / 2);
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
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int st = board.getState(r, c);
				if (st == Board.WALL) {
					paintWall(g, r, c);
				} else if (st == Board.SPACE) {
					paintSpace(g, r, c);
				} else if (st > 0) {
					if (getMarkStyle() == 5) {
						g.setColor(getCircleColor());
						paintCell(g, r, c);
					}
					g.setFont(getNumberFont());
					g.setColor(getNumberColor());
					placeNumber(g, r, c, st);
				} else if (st == Board.UNDECIDED_NUMBER) {
					if (getMarkStyle() == 5) {
						g.setColor(getCircleColor());
						paintCell(g, r, c);
					}
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
	}

	void paintSpace(Graphics2D g, int r, int c) {
		g.setFont(countFont);
		Area area = board.getArea(r,c);
		int number = area.getNumber();
		if (isCountAreaSizeMode()) {
			if (number == 0 || (number > 0 && area.size() < number) || number == Board.UNDECIDED_NUMBER ) {
				g.setColor(getCircleColor());
				placeCircle(g, r, c);
				placeNumber(g, r, c, area.size());
			} else if (
				number == Area.MULTIPLE_NUMBER	|| (number > 0 && area.size() > number)) {
				g.setColor(Color.RED);
				placeCircle(g, r, c);
//				placeBoldCircle(g, r, c);
				placeNumber(g, r, c, area.size());
			} else {
				g.setColor(getCircleColor());
				placeMark(g, r, c);
			}
		} else {
			g.setColor(getCircleColor());
			placeMark(g, r, c);
		}
	}

	void paintWall(Graphics2D g, int r, int c) {
		g.setColor(getPaintColor());
		if (isSeparateAreaColorMode()) {
			g.setColor(Colors.get(board.getArea(r,c).getID()));
		}
//		if (isIndicateError()) {
//			if (board.is2x2Block(r, c) ) {
//				g.setColor(getErrorColor());
//			}
//		}
		paintCell(g, r, c);
	}
}
