package pencilbox.bijutsukan;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;


/**
 * 「美術館」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;
	
	private boolean indicateErrorMode = false;
	private boolean showBeamMode = false;
	private boolean paintIlluminatedCellMode = true;

	private Color wallColor = Color.BLACK;
	private Color bulbColor = new Color(0x000099);
	private Color illuminatedCellColor = new Color(0xAAFFFF);
	private Color noBulbColor = new Color(0xFF9999);

	/**
	 * Panel を生成する
	 */
	public Panel() {
		setMarkStyle(3);
		setNumberColor(Color.WHITE);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the indicateErrorMode.
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
	 * @return the paintIlluminatedCellMode
	 */
	public boolean isPaintIlluminatedCellMode() {
		return paintIlluminatedCellMode;
	}

	/**
	 * @param paintIlluminatedCellMode the paintIlluminatedCellMode to set
	 */
	public void setPaintIlluminatedCellMode(boolean paintIlluminatedCellMode) {
		this.paintIlluminatedCellMode = paintIlluminatedCellMode;
	}

	/**
	 * @return the showRayMode
	 */
	public boolean isShowBeamMode() {
		return showBeamMode;
	}

	/**
	 * @param showRayMode the showRayMode to set
	 */
	public void setShowBeamMode(boolean showRayMode) {
		this.showBeamMode = showRayMode;
	}

	/**
	 * @return Returns the illuminatedCellColor.
	 */
	public Color getIlluminatedCellColor() {
		return illuminatedCellColor;
	}

	/**
	 * @param illuminatedCellColor The illuminatedCellColor to set.
	 */
	public void setIlluminatedCellColor(Color illuminatedCellColor) {
		this.illuminatedCellColor = illuminatedCellColor;
	}

	/**
	 * @return Returns the bulbColor.
	 */
	public Color getBulbColor() {
		return bulbColor;
	}

	/**
	 * @param bulbColor The bulbColor to set.
	 */
	public void setBulbColor(Color bulbColor) {
		this.bulbColor = bulbColor;
	}

	/**
	 * @return Returns the noBulbColor.
	 */
	public Color getNoBulbColor() {
		return noBulbColor;
	}

	/**
	 * @param noBulbColor The noBulbColor to set.
	 */
	public void setNoBulbColor(Color noBulbColor) {
		this.noBulbColor = noBulbColor;
	}

	/**
	 * @param wallColor the wallColor to set
	 */
	public void setWallColor(Color wallColor) {
		this.wallColor = wallColor;
	}

	/**
	 * @return the wallColor
	 */
	public Color getWallColor() {
		return wallColor;
	}

	public void drawBoard(Graphics2D g) {
		drawCells(g);
		drawGrid(g);
		drawBoardBorder(g);
	}

	private void drawCells(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				Address p = Address.address(r, c);
				int state = board.getState(p);
				int l = board.getHorizIlluminated(p);
				int m = board.getVertIlluminated(p);
				if (isPaintIlluminatedCellMode()) {
					if (l>0 || m>0) {
						g.setColor(illuminatedCellColor);
						paintCell(g,r,c);
					}
				}
				if (state >= 0 && state <= 4) {
					g.setColor(getWallColor());
					paintCell(g, r, c);
					g.setColor(getNumberColor());
					if (isIndicateErrorMode()) {
						if (board.checkAdjacentBulbs(p) <= 0) {
							g.setColor(getErrorColor());
						}
					}
					placeNumber(g, r, c, state);
				} else if (state == Board.NONUMBER_WALL) {
					g.setColor(getWallColor());
					paintCell(g, r, c);
				} else if (state == Board.BULB) {
					g.setColor(getBulbColor());
					if (isIndicateErrorMode()) {
						if (board.isMultiIlluminated(p)) {
							g.setColor(getErrorColor());
						}
					}
					placeFilledCircle(g, r, c);
				} else if (state == Board.NOBULB) {
					g.setColor(getNoBulbColor());
					placeMark(g, r, c);
				}
				if (isShowBeamMode()) {
					g.setColor(getBulbColor());
					if (l > 0) {
						if (isIndicateErrorMode() && l > 1) {
							g.setColor(getErrorColor());
						}
						placeCenterLine(g, r, c, Direction.HORIZ);
					}
					g.setColor(getBulbColor());
					if (m > 0) {
						if (isIndicateErrorMode() && m > 1) {
							g.setColor(getErrorColor());
						}
						placeCenterLine(g, r, c, Direction.VERT);
					}
				}
			}
		}
	}

}
