package pencilbox.masyu;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ましゅ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean colorForEachLink = false;
	private boolean warnBranchedLink = false;

	private Color lineColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color pearlColor = Color.BLACK;
	private Color grayPearlColor = Color.GRAY;
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
	 * @return Returns the pearlColor.
	 */
	public Color getPearlColor() {
		return pearlColor;
	}
	/**
	 * @param pearlColor The pearlColor to set.
	 */
	public void setPearlColor(Color pearlColor) {
		this.pearlColor = pearlColor;
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
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				state = board.getNumber(r, c);
				if (state == Board.WHITE_PEARL) {
					placeWhitePearl(g,r,c);
				} else if (state == Board.BLACK_PEARL) {
					placeBlackPearl(g,r,c);
				} else if (state == Board.GRAY_PEARL) {
					placeGrayPearl(g,r,c);
				}
			}
		}
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(lineColor);
						if(isColorForEachLink())
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
						if(isWarnBranchedLink() && board.isBranchedLink(d,r,c))
							g.setColor(errorColor);
						placeTraversalLine(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	
	private void placeBlackPearl(Graphics2D g, int r, int c) {
		g.setColor(pearlColor);
		if (isWarnBranchedLink()) {
			int p = board.checkBlackPearl(r,c);
			if (p==-1) g.setColor(errorColor); 
	//		else if (p==1) g.setColor(successColor);
			else g.setColor(pearlColor); 
		}
		placeFilledCircle(g, r, c);
	}
	
	private void placeWhitePearl(Graphics2D g, int r, int c) {
		g.setColor(pearlColor);
		if (isWarnBranchedLink()) {
			int p = board.checkWhitePearl(r,c);
			if (p==-1) g.setColor(errorColor);
	//		else if (p==1) g.setColor(successColor);
			else g.setColor(pearlColor);
		}
		placeBoldCircle(g, r, c);
	}
	
	private void placeGrayPearl(Graphics2D g, int r, int c) {
		g.setColor(grayPearlColor);
		placeFilledCircle(g, r, c);
	}

}
