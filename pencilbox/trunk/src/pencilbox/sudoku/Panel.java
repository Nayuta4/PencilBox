package pencilbox.sudoku;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelBase;


/**
 * 「数独」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean highlightSelectedNumber = false;
	private boolean warnWrongNumber = false;
	private boolean showAllowedNumberDot = false;

	private Color inputColor = Color.BLUE;
	private Color errorColor = Color.RED;

	private int selectedNumber = 0;
	private Color selectedNumberColor = Color.GREEN;
	private Color selectedNumberColor2 = new Color(0xFFFF80);

	private HintDot hintDot = new HintDot();
	private int unit;

	/**
	 * 
	 */
	public Panel() {
		setCursorOn(true);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
		unit = board.getUnit();
		// ドットヒント表示可能なのは，サイズ3,4,5のみ
		if (unit>=3 && unit<=5)
			hintDot.setDot(this, unit, getCellSize());
	}

	/**
	 * @return the selectedNumber
	 */
	protected int getSelectedNumber() {
		return selectedNumber;
	}

	/**
	 * @param selectedNumber the selectedNumber to set
	 */
	protected void setSelectedNumber(int selectedNumber) {
		this.selectedNumber = selectedNumber;
	}

	/**
	 * @return the warnWrongNumber
	 */
	public boolean isWarnWrongNumber() {
		return warnWrongNumber;
	}

	/**
	 * @param warnWrongNumber The warnWrongNumber to set.
	 */
	public void setWarnWrongNumber(boolean warnWrongNumber) {
		this.warnWrongNumber = warnWrongNumber;
	}

	/**
	 * @return the highlightSelectedNumber
	 */
	public boolean isHighlightSelectedNumber() {
		return highlightSelectedNumber;
	}

	/**
	 * @param highlightSelectedNumber The highlightSelectedNumber to set.
	 */
	public void setHighlightSelectedNumber(boolean highlightSelectedNumber) {
		this.highlightSelectedNumber = highlightSelectedNumber;
	}

	/**
	 * @return the showAllowedNumberDot
	 */
	public boolean isShowAllowedNumberDot() {
		return showAllowedNumberDot;
	}

	/**
	 * @param showAllowedNumberDot The showAllowedNumberDot to set.
	 */
	public void setShowAllowedNumberDot(boolean showAllowedNumberDot) {
		this.showAllowedNumberDot = showAllowedNumberDot;
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
	 * @return Returns the selectedNumberColor.
	 */
	public Color getSelectedNumberColor() {
		return selectedNumberColor;
	}

	/**
	 * @param selectedNumberColor The selectedNumberColor to set.
	 */
	public void setSelectedNumberColor(Color selectedNumberColor) {
		this.selectedNumberColor = selectedNumberColor;
	}

	public void setDisplaySize(int cellSize) {
		super.setDisplaySize(cellSize);
		hintDot.setDotSize(cellSize);
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
	 * ブロックの太枠線を描画する。
	 */
	public void drawBorder(Graphics2D g) {
		g.setColor(getBorderColor());
		for (int r = 0; r <= rows(); r++) {
			if (r % board.getUnit() == 0) {
				g.drawLine(toX(0), toY(r) - 1, toX(cols()), toY(r) - 1);
				g.drawLine(toX(0), toY(r), toX(cols()), toY(r));
				g.drawLine(toX(0), toY(r) + 1, toX(cols()), toY(r) + 1);
			}
		}
		for (int c = 0; c <= cols(); c++) {
			if (c % board.getUnit() == 0) {
				g.drawLine(toX(c) - 1, toY(0), toX(c) - 1, toY(rows()));
				g.drawLine(toX(c), toY(0), toX(c), toY(rows()));
				g.drawLine(toX(c) + 1, toY(0), toX(c) + 1, toY(rows()));
			}
		}
	}

	/**
	 * 盤面を描画する
	 * @param g
	 */
	protected void drawBoard(Graphics2D g) {
		int num;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				num = board.getNumber(r, c);
				paintCell1(g, r, c, num);
				if (num > 0) {
					placeNumber1(g, r, c, num);
				} else if (num == Board.UNKNOWN) {
					if (board.isStable(r,c)) {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					} else if (isShowAllowedNumberDot() && unit >= 3 && unit <=5)
						drawHintDot(g, r, c);
				}
			}
		}
	}
	// 選択数字と同じ行，列，ボックスを色塗り 
	private void paintCell1(Graphics2D g, int r, int c, int num) {
		if (isHighlightSelectedNumber() && getSelectedNumber() > 0) {
			if (getSelectedNumber() == num) {
				g.setColor(selectedNumberColor);
				paintCell(g, r, c);
			} else if (board.canPlace(r, c, getSelectedNumber())) {
				g.setColor(selectedNumberColor2);
				paintCell(g, r, c);
			}
		}
	}
	
	private void placeNumber1(Graphics2D g, int r, int c, int num) {
		if (board.isStable(r, c)) {
			g.setColor(getNumberColor());
		} else {
			g.setColor(inputColor);
			if (isWarnWrongNumber()) {
				if (board.isMultipleNumber(r, c)) {
					g.setColor(errorColor);
				}
			}
		}
		placeNumber(g, r, c, num);
	}

	private void drawHintDot(Graphics2D g, int r, int c) {
		int pat = board.getPattern(r, c);
		if (pat == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, pat);
		}
	}

}
