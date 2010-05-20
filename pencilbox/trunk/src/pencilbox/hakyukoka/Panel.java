package pencilbox.hakyukoka;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelBase;



/**
 * 「波及効果」パネルクラス
 */
public class Panel extends PanelBase {
	
	private Board board;

	private boolean indicateErrorMode = false;
	private boolean highlightSelectionMode = false;

	private Color inputColor = new Color(0x000099);
	private Color areaBorderColor = Color.BLACK;
	private Color noAreaColor = new Color(0xC0C0C0);

	private int selectedNumber = 0;
	private Color highlightColor = new Color(0x00FF00);
//	private Color highlight2Color = new Color(0xFFFF80);
	private Color beamColor = new Color(0x800000);
	private Color draggingAreaColor = new Color(0xCCFFFF);
	private Area draggingArea;

	private boolean dotHintMode = false;
	private HintDot hintDot = new HintDot();

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setCursorMode(true);
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
	 * @param areaBorderColor the areaBorderColor to set
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return the areaBorderColor
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
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

	public void setDisplaySize(int cellSize) {
		super.setDisplaySize(cellSize);
		hintDot.setDotSize(cellSize);
	}

	public void drawBoard(Graphics2D g) {
		paintCells(g);
		drawBeams(g);
		drawNumbers(g);
		drawGrid(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
	}

	private void drawBeams(Graphics2D g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				if (isHighlightSelectionMode()) {
					if (n > 0 && n == getSelectedNumber()) {
						for (int cc = c-n; cc <= c+n; cc++) {
							if (cc==c)
								continue;
							if (board.isOn(r,cc)) {
								g.setColor(beamColor);
								placeCenterLine(g, r, cc, Direction.HORIZ);
//								g.setColor(selectedNumberColor2);
//								paintCell(g, r, cc);
							}
						}
						for (int rr = r-n; rr <= r+n; rr++) {
							if (rr==r)
								continue;
							if (board.isOn(rr,c)) {
								g.setColor(beamColor);
								placeCenterLine(g, rr, c, Direction.VERT);
//								g.setColor(selectedNumberColor2);
//								paintCell(g, rr, c);
							}
						}
					}
				}
			}
		}
	}

	private void paintCells(Graphics2D g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r, c) == null) {
					g.setColor(noAreaColor);
					paintCell(g, r, c);
				} else if (board.getArea(r, c) == draggingArea) {
					g.setColor(draggingAreaColor);
					paintCell(g, r, c);
				}
				// 選択数字配置可能なマスを色塗り
//				if (isHighlightSelectionMode()) {
//					if (getSelectedNumber() > 0 && board.canPlace(r, c, selectedNumber)) {
//						g.setColor(highlight2Color);
//						paintCell(g, r, c);
//					}
//				}
				if (isHighlightSelectionMode()) {
					if (getSelectedNumber() > 0 && board.getNumber(r, c) == getSelectedNumber()) {
						g.setColor(highlightColor);
						paintCell(g, r, c);
					}
				}
			}
		}
	}
	
	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int n = board.getNumber(p);
			if (n > 0) {
				if (board.isStable(p)) {
					g.setColor(getNumberColor());
				} else {
					g.setColor(getInputColor());
					if (isIndicateErrorMode()) {
						if (board.isError(Address.address(p)))
							g.setColor(getErrorColor());
					}
				}
				placeNumber(g, p, n);
			} else if (n == Board.UNKNOWN) {
				if (isDotHintMode()) {
					placeHintDot(g, p);
				}
				if (board.isStable(p)) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, p);
				}
			}
		}
	}
	
	private void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
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

	private void placeHintDot(Graphics2D g, Address p) {
		int pat = board.getPattern(p);
		if (pat == 0) {
			hintDot.placeHintCross(g, p);
		} else {
			hintDot.placeHintDot(g, p, pat);
		}
	}

}
