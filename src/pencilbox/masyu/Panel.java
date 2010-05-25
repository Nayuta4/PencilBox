package pencilbox.masyu;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ましゅ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean separateLinkColorMode = false;
	private boolean indicateErrorMode = false;

	private Color lineColor = new Color(0x000099);
	private Color crossColor = new Color(0xFF0099);
	private Color grayPearlColor = Color.GRAY;

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

	public void drawBoard(Graphics2D g) {
		drawNumbers(g);
		drawGrid(g);
		drawLinks(g);
		drawBoardBorder(g);
	}

	private void drawNumbers(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int number = board.getNumber(p);
			if (number == Board.WHITE_PEARL) {
				placeWhitePearl(g, p);
			} else if (number == Board.BLACK_PEARL) {
				placeBlackPearl(g, p);
			} else if (number == Board.GRAY_PEARL) {
				placeGrayPearl(g, p);
			}
		}
	}

	private void drawLinks(Graphics2D g) {
		int state;
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(getLineColor());
						if (isSeparateLinkColorMode())
							g.setColor(Colors.getColor(board.getLink(d,r,c).getId()));
						placeLink(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(getCrossColor());
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	
	private void placeBlackPearl(Graphics2D g, Address pp) {
		g.setColor(getNumberColor());
		if (isIndicateErrorMode()) {
			int p = board.checkBlackPearl(pp.r(), pp.c());
			if (p==-1)
				g.setColor(getErrorColor()); 
			else if (p==0)
				g.setColor(getErrorColor()); 
//			else if (p==1)
//				g.setColor(getErrorColor()); 
//			else if (p==2)
//				g.setColor(getNumberColor());
		}
		placeFilledCircle(g, pp);
	}
	
	private void placeWhitePearl(Graphics2D g, Address pp) {
		g.setColor(getNumberColor());
		if (isIndicateErrorMode()) {
			int n = board.checkWhitePearl(pp.r(), pp.c());
			if (n==-1)
				g.setColor(getErrorColor()); 
			else if (n==0)
				g.setColor(getErrorColor()); 
//			else if (p==1)
//				g.setColor(getErrorColor()); 
//			else if (p==2)
//				g.setColor(getNumberColor());
		}
		placeBoldCircle(g, pp);
	}
	
	private void placeGrayPearl(Graphics2D g, Address p) {
		g.setColor(grayPearlColor);
		placeFilledCircle(g, p);
	}

}
