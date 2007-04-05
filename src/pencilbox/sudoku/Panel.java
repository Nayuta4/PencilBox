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

	private boolean highlightSelectionMode = false;
	private boolean indicateErrorMode = false;
	private boolean dotHintMode = false;

	private Color inputColor = Color.BLUE;

	private int selectedNumber = 0;
	private Color highlightColor = Color.GREEN;
	private Color highlight2Color = new Color(0xFFFF80);

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
	 * @return the indicateErrorMode
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
	 * @return the highlightSelectionMode
	 */
	public boolean isHighlightSelectionMode() {
		return highlightSelectionMode;
	}

	/**
	 * @param highlightSelectionMode The highlightSelectionMode to set.
	 */
	public void setHighlightSelectionMode(boolean highlightSelectionMode) {
		this.highlightSelectionMode = highlightSelectionMode;
	}

	/**
	 * @return the dotHintMode
	 */
	public boolean isDotHintMode() {
		return dotHintMode;
	}

	/**
	 * @param dotHintMode The dotHintMode to set.
	 */
	public void setDotHintMode(boolean dotHintMode) {
		this.dotHintMode = dotHintMode;
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
	 * @return Returns the highlightColor.
	 */
	public Color getHighlightColor() {
		return highlightColor;
	}

	/**
	 * @param highlightColor The highlightColor to set.
	 */
	public void setHighlightColor(Color highlightColor) {
		this.highlightColor = highlightColor;
	}

	public void setDisplaySize(int cellSize) {
		super.setDisplaySize(cellSize);
		hintDot.setDotSize(cellSize);
	}

	public void drawBoard(Graphics2D g) {
		drawNumbers(g);
		drawGrid(g);
		drawAreaBorders(g);
	}

	/**
	 * ブロックの太枠線を描画する。
	 */
	public void drawAreaBorders(Graphics2D g) {
		g.setColor(getBoardBorderColor());
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

	protected void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				paintCell1(g, r, c, n);
				if (n > 0) {
					placeNumber1(g, r, c, n);
				} else if (n == Board.UNKNOWN) {
					if (isDotHintMode() && unit >= 3 && unit <=5) {
						placeHintDot(g, r, c);
					}
					if (board.isStable(r,c)) {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					} 
				}
			}
		}
	}
	// 選択数字と同じ行，列，ボックスを色塗り 
	private void paintCell1(Graphics2D g, int r, int c, int num) {
		if (isHighlightSelectionMode() && getSelectedNumber() > 0) {
			if (getSelectedNumber() == num) {
				g.setColor(highlightColor);
				paintCell(g, r, c);
			} else if (board.canPlace(r, c, getSelectedNumber())) {
				g.setColor(highlight2Color);
				paintCell(g, r, c);
			}
		}
	}
	
	private void placeNumber1(Graphics2D g, int r, int c, int num) {
		if (board.isStable(r, c)) {
			g.setColor(getNumberColor());
		} else {
			g.setColor(getInputColor());
			if (isIndicateErrorMode()) {
				if (board.isMultipleNumber(r, c)) {
					g.setColor(getErrorColor());
				}
			}
		}
		placeNumber(g, r, c, num);
	}

	private void placeHintDot(Graphics2D g, int r, int c) {
		int pat = board.getPattern(r, c);
		if (pat == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, pat);
		}
	}

}
