package pencilbox.fillomino;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「フィルオミノ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean showAreaBorderMode = true;
	private boolean indicateErrorMode = false;
	private boolean separateAreaColorMode = false;

	private Color inputColor = Color.BLUE;
	private Color areaBorderColor = Color.BLUE;
	private Color successColor = new Color(0x80FFFF);

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the areaBorderColor.
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
	}

	/**
	 * @param areaBorderColor The areaBorderColor to set.
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return Returns the inputColor.
	 */
	public Color getInputColor() {
		return inputColor;
	}

	/**
	 * @param inputColor The inputColor to set.
	 */
	public void setInputColor(Color inputColor) {
		this.inputColor = inputColor;
	}

	/**
	 * @return Returns the showAreaBorderMode.
	 */
	public boolean isShowAreaBorderMode() {
		return showAreaBorderMode;
	}

	/**
	 * @param showAreaBorderMode The showAreaBorderMode to set.
	 */
	public void setShowAreaBorderMode(boolean showAreaBorderMode) {
		this.showAreaBorderMode = showAreaBorderMode;
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

	/**
	 * @return the separateAreaColorMode
	 */
	public boolean isSeparateAreaColorMode() {
		return separateAreaColorMode;
	}

	/**
	 * @param separateAreaColorMode the separateAreaColorMode to set
	 */
	public void setSeparateAreaColorMode(boolean separateAreaColorMode) {
		this.separateAreaColorMode = separateAreaColorMode;
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
		drawNumbers(g);
		if (isShowAreaBorderMode()) 
			drawAreaBorders(g);
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int number = board.getNumber(r, c);
				if (number > 0) {
					if (isIndicateErrorMode()) {
						int status = board.getArea(r,c).getStatus();
						if (status == -1) {
							g.setColor(getErrorColor());
							paintCell(g, r, c);
						} else if (status == 1) {
							g.setColor(successColor); 
							paintCell(g, r, c);
						}
					} else if (isSeparateAreaColorMode()) {
						g.setColor(Colors.getBrightColor(board.getNumber(r,c)));
						paintCell(g, r, c);
					}
				}
				if (number > 0) {
					if (board.isStable(r, c)) {
						g.setColor(getNumberColor());
					} else {
						g.setColor(getInputColor());
					}
					placeNumber(g, r, c, board.getNumber(r, c));
				} else if (number == Board.UNKNOWN) {
					if (board.isStable(r, c)) {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					}
				}
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(areaBorderColor);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				if (board.getNumber(r, c) != board.getNumber(r, c + 1)) {
					//				 && board.getNumber(r,c)>0 && board.getNumber(r,c+1)>0) {
					placeSideLine(g, Direction.VERT, r, c);
				}
			}
			if (board.getArea(r,0) != null)
				placeSideLine(g, Direction.VERT, r, -1);
			if (board.getArea(r,board.cols()-1) != null)
				placeSideLine(g, Direction.VERT, r, board.cols()-1);
		}
		for (int c = 0; c < board.cols(); c++) {
			for (int r = 0; r < board.rows() - 1; r++) {
				if (board.getNumber(r, c) != board.getNumber(r + 1, c)) {
					//				&& board.getNumber(r,c)>0 && board.getNumber(r+1,c)>0) {
					placeSideLine(g, Direction.HORIZ, r, c);
				}
			}
			if (board.getArea(0, c) != null)
				placeSideLine(g, Direction.HORIZ, -1, c);
			if (board.getArea(board.rows()-1, c) != null)
				placeSideLine(g, Direction.HORIZ, board.rows()-1, c);
		}
	}

}
