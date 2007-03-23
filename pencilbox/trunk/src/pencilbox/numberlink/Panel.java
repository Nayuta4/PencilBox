package pencilbox.numberlink;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ナンバーリンク」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color lineColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;

	private boolean indicateErrorMode = false;
	private boolean separateLinkColorMode = false;
	private boolean highlightSelectionMode = false;

	private Link selectedLink = null;
	private int selectedNumber = 0;  // 選択されていないときは 0

//	private Color numberlessLinkColor = Color.CYAN;
	private Color highlightColor = Color.GREEN;

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
	 * @return Returns the crossColor.
	 */
	public Color getCrossColor() {
		return crossColor;
	}

	/**
	 * @param crossColor The crossColor to set.
	 */
	public void setCrossColor(Color crossColor) {
		this.crossColor = crossColor;
	}

	/**
	 * @return Returns the lineColor.
	 */
	public Color getLineColor() {
		return lineColor;
	}

	/**
	 * @param lineColor The lineColor to set.
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * @return the separateLinkColorMode
	 */
	public boolean isSeparateLinkColorMode() {
		return separateLinkColorMode;
	}

	/**
	 * @param separateLinkColorMode The separateLinkColorMode to set.
	 */
	public void setSeparateLinkColorMode(boolean separateLinkColorMode) {
		this.separateLinkColorMode = separateLinkColorMode;
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
	 * @return the selectedLink
	 */
	protected Link getSelectedLink() {
		return selectedLink;
	}

	/**
	 * @param selectedLink the selectedLink to set
	 */
	protected void setSelectedLink(Link selectedLink) {
		this.selectedLink = selectedLink;
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
		int state;
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						placeLink1(g, d, r, c);
//					} else if (state == Board.NOLINE) {
//						g.setColor(getCrossColor());
//						placeSideCross(g, d, r, c);
					}
				}
			}
		}
		int number;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				number = board.getNumber(r, c);
				if (number > 0) {
					g.setColor(getBackgroundColor());
					placeFilledCircle(g, r, c);
					g.setColor(getNumberColor());
					placeNumber1(g, r, c, number);
				} else if (number == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
	}

	private void placeLink1(Graphics2D g, int d, int r, int c) {
		Link link = board.getLink(d,r,c);
		int linkNo = link.getNumber();
		g.setColor(getLineColor());
		if (isSeparateLinkColorMode()) {
			if (linkNo == 0) {
				g.setColor(getLineColor());
			} else if (linkNo == -1) {
				g.setColor(getErrorColor());
			} else {
				g.setColor(Colors.getDarkColor(linkNo));
			}
		}
		if (isHighlightSelectionMode()) {
			if ((linkNo > 0 && linkNo == getSelectedNumber())
					|| link == getSelectedLink()) {
				g.setColor(highlightColor);
			}
		}
//		if (isIndicateErrorMode()) {
//			if (board.isBranchedLink(d,r,c)) {
//				g.setColor(getErrorColor());
//			}
//		}
		super.placeLink(g, d, r, c);
	}

	private void placeNumber1(Graphics2D g, int r, int c, int n) {
		if (isHighlightSelectionMode()) {
			if (n == getSelectedNumber()) {
				g.setColor(highlightColor);
				super.paintCell(g, r, c);
			}
		}
		g.setColor(getNumberColor());
		if (isSeparateLinkColorMode()) {
			g.setColor(Colors.getDarkColor(board.getNumber(r,c)));
		}
		if (isIndicateErrorMode()) {
			if (board.getLink(r, c) != null
					&& board.getLink(r, c).getNumber() == -1)
				g.setColor(getErrorColor());
//			if (board.countLine(r, c) > 1)
//				g.setColor(getErrorColor());
		}
		super.placeNumber(g, r, c, n);
	}
	
}
