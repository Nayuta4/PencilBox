package pencilbox.shikaku;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「四角に切れ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color areaBorderColor = Color.BLUE;
	private Color smallSizeColor = new Color(0xFFFF80); // 面積小さい
	private Color areaPaintColor   = new Color(0x80FFFF); // 標準色

	private Square draggingArea; // ドラッグして今まさに描こうとしている四角

	private boolean separateAreaColorMode = false;
	private boolean indicateErrorMode = false;

	/**
	 * パネルを生成する
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
	 * @return Returns the areaPaintColor.
	 */
	public Color getAreaPaintColor() {
		return areaPaintColor;
	}

	/**
	 * @param areaPaintColor The areaPaintColor to set.
	 */
	public void setAreaPaintColor(Color areaPaintColor) {
		this.areaPaintColor = areaPaintColor;
	}

	/**
	 * @return the separateAreaColorMode
	 */
	public boolean isSeparateAreaColorMode() {
		return separateAreaColorMode;
	}

	/**
	 * @param separateAreaColorMode The separateAreaColorMode to set.
	 */
	public void setSeparateAreaColorMode(boolean separateAreaColorMode) {
		this.separateAreaColorMode = separateAreaColorMode;
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
		paintAreas(g);
		drawNumbers(g);
		drawGrid(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
	}

	private void paintAreas(Graphics2D g) {
		Square area;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				area = board.getSquare(r,c);
				if (area == null)
					continue;
				g.setColor(areaPaintColor);
				if (isIndicateErrorMode()) {
					int number = area.getNumber();
					if (number == Square.MULTIPLE_NUMBER) {
						g.setColor(getErrorColor());
					} else if (number == Square.NO_NUMBER) {
						g.setColor(smallSizeColor);
					} else if (number == Board.UNDECIDED_NUMBER) {
						g.setColor(areaPaintColor);
					} else if (number < area.getSquareSize()) {
						g.setColor(getErrorColor());
					} else if (number == area.getSquareSize()) {
						g.setColor(areaPaintColor);
					} else if (number > area.getSquareSize()) {
						g.setColor(smallSizeColor);
					}
				} else if (isSeparateAreaColorMode()) {
					g.setColor(Colors.getBrightColor(board.getSquare(r,c).getId()));
				}
				paintCell(g, r, c);
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		int number;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				number = board.getNumber(r, c);
				if (number > 0 || number == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					super.placeFilledCircle(g, r, c, (int)(getCellSize()*0.85) );
				}
				if (number > 0) {
					g.setColor(getBackgroundColor());
					super.placeNumber(g, r, c, number);
				}
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		Square area;
		g.setColor(getAreaBorderColor());
		for (Iterator itr = board.getSquareListIterator(); itr.hasNext();) {
			area = (Square) itr.next();
			placeSquare(g, area.r0, area.c0, area.r1, area.c1);
		}
		area = getDraggingArea();
		if (area != null) {
			placeSquare(g, area.r0, area.c0, area.r1, area.c1);
		}
	}

	/**
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Square draggingArea) {
		this.draggingArea = draggingArea;
	}

	/**
	 * @return the draggingArea
	 */
	Square getDraggingArea() {
		return draggingArea;
	}

}
