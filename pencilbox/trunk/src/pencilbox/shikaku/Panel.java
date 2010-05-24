package pencilbox.shikaku;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「四角に切れ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color areaBorderColor = new Color(0x000099);
	private Color smallSizeColor = new Color(0xFFFF99); // 面積小さい
	private Color areaPaintColor   = new Color(0xAAFFFF); // 標準色
	private Color draggingAreaColor = new Color(0xCCFFFF);

	private Square draggingSquare; // ドラッグして今まさに描こうとしている四角

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
		Square square;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				square = board.getSquare(r,c);
				if (square == null)
					continue;
				g.setColor(areaPaintColor);
				if (isIndicateErrorMode()) {
					int number = square.getNumber();
					if (number == Square.MULTIPLE_NUMBER) {
						g.setColor(getErrorColor());
					} else if (number == Square.NO_NUMBER) {
						g.setColor(smallSizeColor);
					} else if (number == Board.UNDECIDED_NUMBER) {
						g.setColor(areaPaintColor);
					} else if (number < square.getSquareSize()) {
						g.setColor(getErrorColor());
					} else if (number == square.getSquareSize()) {
						g.setColor(areaPaintColor);
					} else if (number > square.getSquareSize()) {
						g.setColor(smallSizeColor);
					}
				} else if (isSeparateAreaColorMode()) {
					g.setColor(Colors.getBrightColor(board.getSquare(r,c).getId()));
				}
				paintCell(g, r, c);
			}
		}
		square = getDraggingSquare();
		if (square != null) {
			for (int r = square.r0(); r <= square.r1(); r++) {
				for (int c = square.c0(); c <= square.c1(); c++) {
					g.setColor(draggingAreaColor);
					paintCell(g, r, c);
				}
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		int number;
		for (Address p : board.cellAddrs()) {
			number = board.getNumber(p);
			if (number > 0 || number == Board.UNDECIDED_NUMBER) {
				g.setColor(getNumberColor());
				super.placeFilledCircle(g, p, (int)(getCellSize()*0.85) );
			}
			if (number > 0) {
				g.setColor(getBackgroundColor());
				super.placeNumber(g, p, number);
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
		for (Square square : board.getSquareList()) {
			placeSquare(g, square.r0(), square.c0(), square.r1(), square.c1());
		}
		Square square = getDraggingSquare();
		if (square != null) {
			placeSquare(g, square.r0(), square.c0(), square.r1(), square.c1());
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		this.draggingSquare = draggingSquare;
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return draggingSquare;
	}

}
