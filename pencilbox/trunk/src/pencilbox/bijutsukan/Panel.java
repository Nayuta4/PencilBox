package pencilbox.bijutsukan;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;


/**
 * 「美術館」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;
	
	private boolean warnWrongIllumination = false;
	private boolean showBeamMode = false;

	private Color wallColor = Color.BLACK;
	private Color illuminationColor = Color.BLUE;
	private Color illuminatedColor = new Color(0xAAFFFF);
	private Color noilluminationColor = Color.MAGENTA;
	private Color wallNumberColor = Color.WHITE;
	private Color errorColor = Color.RED;

	/**
	 * Panel を生成する
	 */
	public Panel() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the warnWrongIllumination.
	 */
	public boolean isWarnWrongIllumination() {
		return warnWrongIllumination;
	}

	/**
	 * @param warnWrongIllumination The warnWrongIllumination to set.
	 */
	public void setWarnWrongIllumination(boolean warnWrongIllumination) {
		this.warnWrongIllumination = warnWrongIllumination;
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
	 * @return Returns the illuminatedColor.
	 */
	public Color getIlluminatedColor() {
		return illuminatedColor;
	}

	/**
	 * @param illuminatedColor The illuminatedColor to set.
	 */
	public void setIlluminatedColor(Color illuminatedColor) {
		this.illuminatedColor = illuminatedColor;
	}

	/**
	 * @return Returns the illuminationColor.
	 */
	public Color getIlluminationColor() {
		return illuminationColor;
	}

	/**
	 * @param illuminationColor The illuminationColor to set.
	 */
	public void setIlluminationColor(Color illuminationColor) {
		this.illuminationColor = illuminationColor;
	}

	/**
	 * @return Returns the noilluminationColor.
	 */
	public Color getNoilluminationColor() {
		return noilluminationColor;
	}

	/**
	 * @param noilluminationColor The noilluminationColor to set.
	 */
	public void setNoilluminationColor(Color noilluminationColor) {
		this.noilluminationColor = noilluminationColor;
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
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int state = board.getState(r, c);
				int l = board.getHorizIlluminated(r,c);
				int m = board.getVertIlluminated(r,c);
				if (l>0 || m>0) {
					g.setColor(illuminatedColor);
					paintCell(g,r,c);
				}
				if (isShowBeamMode()) {
					g.setColor(illuminationColor);
					if (l > 0) {
						if (isWarnWrongIllumination() && l > 1) {
							g.setColor(errorColor);
						}
						placeMidline(g, r, c, Direction.HORIZ);
					}
					g.setColor(illuminationColor);
					if (m > 0) {
						if (isWarnWrongIllumination() && m > 1) {
							g.setColor(errorColor);
						}
						placeMidline(g, r, c, Direction.VERT);
					}
				}
				if (state >= 0 && state <= Board.NONUMBER_WALL) {
					g.setColor(wallColor);
					paintCell(g, r, c);
					if (state >= 0 && state <= 4) {
						g.setColor(wallNumberColor);
//						if (numberHintMode) {
//							int st = board.getWallState(r,c);
//							if (st == -1) g.setColor(errorColor);
//							else if (st == 1) g.setColor(successColor);
//						} else
						if (warnWrongIllumination == true) {
							int st = board.checkAdjacentIllumination(r,c);
							if (st == -1)
								g.setColor(errorColor);
						}
						placeNumber(g, r, c, state);
					}
				} else if (state == Board.ILLUMINATION) {
					g.setColor(illuminationColor);
					if (warnWrongIllumination == true && board.isMultiIlluminated(r,c)) {
						g.setColor(errorColor);
					}
					placeFilledCircle(g, r, c);
				} else if (state == Board.NOILLUMINATION) {
					g.setColor(noilluminationColor);
					placeCross(g, r, c);
				}
			}
		}
	}

}
