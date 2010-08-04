package pencilbox.fillomino;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「フィルオミノ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean showAreaBorderMode = true;
	private boolean indicateErrorMode = false;
	private boolean separateAreaColorMode = false;

	private Color inputColor = new Color(0x000099);
	private Color areaBorderColor = new Color(0x000099);
	private Color successColor = new Color(0x80FFFF);
	private Color borderColor = new Color(0xFF0099);

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
	 * @return Returns the areaBorderColor.
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
	}

	/**
	 * @param areaBorderColor The areaBorderColor to set.
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return Returns the borderColor.
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param borderColor The borderColor to set.
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
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
	 * @return Returns the showAreaBorderMode.
	 */
	public boolean isShowAreaBorderMode() {
		return showAreaBorderMode;
	}

	/**
	 * @param showAreaBorderMode The showAreaBorderMode to set.
	 */
	public void setShowAreaBorderMode(boolean showAreaBorderMode) {
		this.showAreaBorderMode = showAreaBorderMode;
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
	 * @return the separateAreaColorMode
	 */
	public boolean isSeparateAreaColorMode() {
		return separateAreaColorMode;
	}

	/**
	 * @param separateAreaColorMode the separateAreaColorMode to set
	 */
	public void setSeparateAreaColorMode(boolean separateAreaColorMode) {
		this.separateAreaColorMode = separateAreaColorMode;
	}

	public void drawBoard(Graphics2D g) {
		paintAreas(g);
		drawNumbers(g);
		drawGrid(g);
		if (isShowAreaBorderMode()) 
			drawAreaBorders(g);
		drawBoardBorder(g);
		drawEdges(g);
	}

	private void paintAreas(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int n = board.getNumberOrState(p);
			if (n > 0) {
				if (isIndicateErrorMode()) {
					int status = board.getArea(p).getStatus();
					if (status == -1) {
						g.setColor(getErrorColor());
						paintCell(g, p);
					} else if (status == 1) {
						g.setColor(successColor); 
						paintCell(g, p);
					}
				} else if (isSeparateAreaColorMode()) {
					g.setColor(Colors.getBrightColor(n));
					paintCell(g, p);
				}
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int n = board.getNumber(p);
			if (n > 0) {
				g.setColor(getNumberColor());
				placeNumber(g, p, n);
			} else if (n == Board.UNDETERMINED) {
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			} else {
				int s = board.getState(p);
				if (s > 0) {
					g.setColor(getInputColor());
					placeNumber(g, p, s);
				}
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(areaBorderColor);
		for (Address p : board.cellAddrs()) {
			for (int d : Direction.DN_RT) {
				Address p1 = p.nextCell(d);
				SideAddress b = SideAddress.get(p, d);
				if (board.isSideOn(b)) {
					if (board.getNumberOrState(p) != board.getNumberOrState(p1)) {
						placeSideLine(g, b);
					}
				}
			}
		}
	}

	private void drawEdges(Graphics2D g) {
		g.setColor(borderColor);
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getEdge(p);
			if (state == Board.LINE) {
				placeSideLine(g, p);
			}
		}
	}
}