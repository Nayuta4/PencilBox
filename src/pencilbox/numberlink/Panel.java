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

	private boolean warnBranchedLink = false;
	private boolean colorForEachLink = false;
	private boolean highlightSelectedLink = false;

	private Link selectedLink = null;
	private int selectedNumber = 0;  // 選択されていないときは 0

	private Color numberlessLinkColor = Color.CYAN;
	private Color selectedLinkColor = Color.GREEN;
	private Color branchedLinkColor = Color.RED;
	private Color errorColor = Color.RED;

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
	 * @return the colorForEachLink
	 */
	public boolean isColorForEachLink() {
		return colorForEachLink;
	}

	/**
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}

	/**
	 * @return the highlightSelectedLink
	 */
	public boolean isHighlightSelectedLink() {
		return highlightSelectedLink;
	}

	/**
	 * @param highlightSelectedLink The highlightSelectedLink to set.
	 */
	public void setHighlightSelectedLink(boolean highlightSelectedLink) {
		this.highlightSelectedLink = highlightSelectedLink;
	}

	/**
	 * @return the warnBranchedLink
	 */
	public boolean isWarnBranchedLink() {
		return warnBranchedLink;
	}

	/**
	 * @param warnBranchedLink The warnBranchedLink to set.
	 */
	public void setWarnBranchedLink(boolean warnBranchedLink) {
		this.warnBranchedLink = warnBranchedLink;
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
		int number;
		int state;
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(lineColor);
						placeTraversalLine(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				number = board.getNumber(r, c);
				if (number > 0) {
					g.setColor(getBackgroundColor());
					placeFilledCircle(g, r, c);
					g.setColor(getNumberColor());
					placeNumber(g, r, c, number);
				} else if (number == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
	}
	public void placeTraversalLine(Graphics2D g, int d, int r, int c) {
		Link link = board.getLink(d,r,c);
		int linkNo = link.getNumber();
		if (isWarnBranchedLink() && board.isBranchedLink(d,r,c)) {
			g.setColor(errorColor);
		}
		else if (isHighlightSelectedLink() && ((linkNo > 0 && linkNo == getSelectedNumber())|| link == getSelectedLink())) {
			g.setColor(selectedLinkColor);
		}			
		else if (isColorForEachLink()) {
			if (linkNo == 0) {
				g.setColor(numberlessLinkColor);
			}
			else if (linkNo == -1) {
				g.setColor(branchedLinkColor);
			}
			else {
				g.setColor(Colors.getDarkColor(linkNo));
			}
		}
		super.placeTraversalLine(g, d, r, c);
	}

	public void placeNumber(Graphics2D g, int r, int c, int n) {
		if (isHighlightSelectedLink() && n == getSelectedNumber()) {
			g.setColor(selectedLinkColor);
			super.paintCell(g, r, c);
			g.setColor(getNumberColor());
		}
		else if (
			isWarnBranchedLink()
				&& ((board.getLink(r, c) != null
					&& board.getLink(r, c).getNumber() == -1)
					|| board.countLine(r, c) > 1)) {
			g.setColor(errorColor);
//			super.paintCell(g, r, c);
//			g.setColor(numberColor);
		}
		else if (isColorForEachLink()) {
			g.setColor(Colors.getDarkColor(board.getNumber(r,c)));
		}
		else {
			g.setColor(getNumberColor());
		}
		super.placeNumber(g, r, c, n);
	}
	
	/**
	 * 罫線のオプションを提供
	 */
	public void drawGrid(Graphics2D g) {
		g.setColor(getGridColor());
		switch (getDisplayStyle()) {
			case 0 : // 通常表示
				super.drawGrid(g);
				break;
			case 1 : // 経路表示
				for (int r = 0; r < rows(); r++) {
					g.drawLine(
						toX(0) + getCellSize() / 2,
						toY(r) + getCellSize() / 2,
						toX(cols()) - getCellSize() / 2,
						toY(r) + getCellSize() / 2);
				}
				for (int c = 0; c < cols(); c++) {
					g.drawLine(
						toX(c) + getCellSize() / 2,
						toY(0) + getCellSize() / 2,
						toX(c) + getCellSize() / 2,
						toY(rows()) - getCellSize() / 2);
				}
				break;
			case 2 : // 非表示
				break;
		}
	}

}
