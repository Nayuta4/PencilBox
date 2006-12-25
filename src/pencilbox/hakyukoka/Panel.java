package pencilbox.hakyukoka;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelBase;



/**
 * 「波及効果」パネルクラス
 */
public class Panel extends PanelBase {
	
	private Board board;

	private boolean warnWrongNumber = false;
	private boolean highlightSelectedNumber = false;

	private Color inputColor = Color.BLUE;
	private Color areaBorderColor = Color.BLACK;
	private Color errorColor = Color.RED;
	private Color noAreaColor = new Color(0xC0C0C0);

	private int selectedNumber = 0;
	private Color selectedNumberColor = new Color(0xAAFFAA);
	private Color emissionColor = new Color(0x800000);
	private Area draggingArea;

	private boolean showAllowedNumberDot = false;
	private HintDot hintDot = new HintDot();

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setCursorOn(true);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		hintDot.setDot(this, 3, getCellSize());
	}

	/**
	 * @return the selectedNumber
	 */
	public int getSelectedNumber() {
		return selectedNumber;
	}

	/**
	 * @param selectedNumber the selectedNumber to set
	 */
	public void setSelectedNumber(int selectedNumber) {
		this.selectedNumber = selectedNumber;
	}

	/**
	 * @return the draggingArea
	 */
	Area getDraggingArea() {
		return draggingArea;
	}

	/**
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Area draggingArea) {
		this.draggingArea = draggingArea;
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
	 * 盤面を描画する
	 * @param g
	 */
	protected void drawBoard(Graphics2D g) {
//		paintEmission(g);
		paintCells(g);
		drawEmission(g);
		drawNumbers(g);
		if(isShowAllowedNumberDot())
			drawDots(g);
		drawBorders(g);
	}
	
	private void drawEmission(Graphics2D g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				if (isHighlightSelectedNumber() && n > 0 && n == getSelectedNumber()) {
					for (int cc = c-n; cc <= c+n; cc++) {
						if (cc==c)
							continue;
						if (board.isOn(r,cc)) {
							g.setColor(emissionColor);
							placeCenterLine(g, r, cc, Direction.HORIZ);
//							g.setColor(selectedNumberColor2);
//							paintCell(g, r, cc);
						}
					}
					for (int rr = r-n; rr <= r+n; rr++) {
						if (rr==r)
							continue;
						if (board.isOn(rr,c)) {
							g.setColor(emissionColor);
							placeCenterLine(g, rr, c, Direction.VERT);
//							g.setColor(selectedNumberColor2);
//							paintCell(g, rr, c);
						}
					}
				}
			}
		}
	}
//	private void paintEmission(Graphics2D g) {
//		for (int r = 0; r < board.rows(); r++) {
//			for (int c = 0; c < board.cols(); c++) {
//				int number = board.getNumber(r, c);
//				if (highlightSelectedNumber && number > 0 && number == selectedNumber) {
//					for (int cc = c-number; cc <= c+number; cc++) {
//						if (cc==c) continue;
//						if (board.isOn(r,cc)) {
//							g.setColor(selectedNumberColor2);
//							paintCell(g, r, cc);
//						}
//					}
//					for (int rr = r-number; rr <= r+number; rr++) {
//						if (rr==r) continue;
//						if (board.isOn(rr,c)) {
//							g.setColor(selectedNumberColor2);
//							paintCell(g, rr, c);
//						}
//					}
//				}
//			}
//		}
//	}
	private void paintCells(Graphics2D g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r, c) == null || board.getArea(r, c) == draggingArea) {
					g.setColor(noAreaColor);
					paintCell(g, r, c);
				}
				// 選択数字配置不可のマスを色塗り
//				if (highlightSelectedNumber && selectedNumber > 0) {
//					if (!board.canPlace(r, c, selectedNumber)) {
//						g.setColor(selectedNumberColor2);
//						paintCell(g, r, c);
//					}
//				}
				if (isHighlightSelectedNumber() && getSelectedNumber() > 0 && board.getNumber(r, c) == getSelectedNumber()) {
					g.setColor(selectedNumberColor);
					paintCell(g, r, c);
				}
			}
		}
	}
	
	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				if (board.getNumber(r, c) > 0) {
					if (board.isStable(r, c)) {
						g.setColor(getNumberColor());
					} else {
						if (isWarnWrongNumber() && board.isError(r,c))
							g.setColor(errorColor);
						else
							g.setColor(inputColor);
					}
					placeNumber(g, r, c, board.getNumber(r, c));
				}
				else if (n == Board.UNKNOWN) {
					if(board.isStable(r,c)) {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					}
				}
			}
		}
	}
	
	private void drawBorders(Graphics2D g) {
		g.setColor(areaBorderColor);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				if (board.getArea(r, c) != board.getArea(r, c + 1)) {
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
				if (board.getArea(r, c) != board.getArea(r + 1, c)) {
					placeSideLine(g, Direction.HORIZ, r, c);
				}
			}
			if (board.getArea(0, c) != null)
				placeSideLine(g, Direction.HORIZ, -1, c);
			if (board.getArea(board.rows()-1, c) != null)
				placeSideLine(g, Direction.HORIZ, board.rows()-1, c);
		}
	}

	private void drawDots(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int num = board.getNumber(r, c);
				if (num == 0) {
					int pat = board.getPattern(r, c);
					if (pat == 0) {
						hintDot.placeHintCross(g, r, c);
					} else {
						hintDot.placeHintDot(g, r, c, pat);
					}
				}
			}
		}
	}

}
