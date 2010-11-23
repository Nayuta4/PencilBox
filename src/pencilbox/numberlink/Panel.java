package pencilbox.numberlink;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ナンバーリンク」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Link selectedLink = null;
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

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawLinks(g);
		drawNumbers(g);
		drawBoardBorder(g);
	}

	private void drawLinks(Graphics2D g) {
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getState(p);
			if (state == Board.LINE) {
				placeLink1(g, p);
//			} else if (state == Board.NOLINE) {
//				g.setColor(getCrossColor());
//				placeSideCross(g, p);
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		int number;
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			number = board.getNumber(p);
			if (number > 0) {
				g.setColor(getBackgroundColor());
				placeFilledCircle(g, p);
				g.setColor(getNumberColor());
				placeNumber1(g, p, number);
			} else if (number == Board.UNDECIDED_NUMBER) {
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			}
		}
	}

	private void placeLink1(Graphics2D g, SideAddress p) {
		Link link = board.getLink(p);
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
		super.placeLink(g, p);
	}

	private void placeNumber1(Graphics2D g, Address p, int n) {
		if (isHighlightSelectionMode()) {
			if (n == getSelectedNumber()) {
				g.setColor(highlightColor);
				super.paintCell(g, p);
			}
		}
		g.setColor(getNumberColor());
		if (isSeparateLinkColorMode()) {
			g.setColor(Colors.getDarkColor(board.getNumber(p)));
		}
//		if (isIndicateErrorMode()) {
//			if (board.getLink(p) != null
//					&& board.getLink(p).getNumber() == -1)
//				g.setColor(getErrorColor());
////			if (board.countLine(p) > 1)
////				g.setColor(getErrorColor());
//		}
		super.placeNumber(g, p, n);
	}
	
}
