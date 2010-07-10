package pencilbox.hakyukoka;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
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
		if (isHighlightSelectionMode()) {
			for (Address p : board.cellAddrs()) {
				int n = board.getNumberOrState(p);
				if (n > 0 && n == getSelectedNumber()) {
					for (int d = 0; d < 4; d++) {
						Address p1 = p;
						for (int k = 0; k < n; k++) {
							p1 = Address.nextCell(p1, d);
							if (board.isOn(p1)) {
								g.setColor(beamColor);
								placeCenterLine(g, p1, d&1);
//								g.setColor(selectedNumberColor2);
//								paintCell(g, p1);
							}
						}
					}
				}
			}
		}
	}

	private void paintCells(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			if (board.getArea(p) == null) {
				g.setColor(noAreaColor);
				paintCell(g, p);
			} else if (board.getArea(p) == draggingArea) {
				g.setColor(draggingAreaColor);
				paintCell(g, p);
			}
			// 選択数字配置可能なマスを色塗り
//			if (isHighlightSelectionMode()) {
//				if (getSelectedNumber() > 0 && board.canPlace(p, selectedNumber)) {
//					g.setColor(highlight2Color);
//					paintCell(g, p);
//				}
//			}
			if (isHighlightSelectionMode()) {
				if (getSelectedNumber() > 0 && board.getNumberOrState(p) == getSelectedNumber()) {
					g.setColor(highlightColor);
					paintCell(g, p);
				}
			}
		}
	}
	
	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int n = board.getNumber(p);
			int s = board.getState(p);
			if (n > 0) {
				g.setColor(getNumberColor());
				placeNumber(g, p, n);
			} else if (n == Board.UNDETERMINED) {
				if (isDotHintMode()) {
					placeHintDot(g, p);
				}
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			} else if (s > 0) {
				g.setColor(getInputColor());
				if (isIndicateErrorMode()) {
					if (board.isError(p)) {
						g.setColor(getErrorColor());
					}
				}
				placeNumber(g, p, s);
			} else {
				if (isDotHintMode()) {
					placeHintDot(g, p);
				}
			}
		}
	}
	
	private void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
		for (Address p : board.cellAddrs()) {
			for (int d : Direction.DN_RT) {
				Address p1 = p.nextCell(d);
				SideAddress b = SideAddress.get(p, d);
				if (board.isSideOn(b)) {
					if (board.getArea(p) != board.getArea(p1)) {
						placeSideLine(g, b);
					}
				}
			}
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
