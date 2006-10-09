package pencilbox.shikaku;

import java.awt.Color;
import java.awt.Graphics;
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
	private Color errorColor = new Color(0xFF0000);
	private Color smallSizeColor = new Color(0xFFFF80); // 面積小さい
	private Color areaPaintColor   = new Color(0x80FFFF); // 標準色

	private Square draggingArea; // ドラッグして今まさに描こうとしている四角

	private boolean colorfulMode = false;
	private boolean showAreaHint = false;

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
	 * @return the colorfulMode
	 */
	public boolean isColorfulMode() {
		return colorfulMode;
	}

	/**
	 * @param colorfulMode The colorfulMode to set.
	 */
	public void setColorfulMode(boolean colorfulMode) {
		this.colorfulMode = colorfulMode;
	}

	/**
	 * @return the showAreaHint
	 */
	public boolean isShowAreaHint() {
		return showAreaHint;
	}

	/**
	 * @param showAreaHint The showAreaHint to set.
	 */
	public void setShowAreaHint(boolean showAreaHint) {
		this.showAreaHint = showAreaHint;
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		//		if (mouseListener.draggingArea != null)
		drawDragging(g);
		drawGrid(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g) {
		g.setColor(smallSizeColor);
		Square domain;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				domain = board.getSquare(r,c);
				if (domain == null)
					continue;
				if (isShowAreaHint()) {
					int number = domain.getNumber();
					if (number == Square.MULTIPLE_NUMBER) {
						g.setColor(errorColor);
					} else if (number == Square.NO_NUMBER) {
						g.setColor(smallSizeColor);
					} else if (number == Board.UNDECIDED_NUMBER) {
						g.setColor(areaPaintColor);
					} else if (number < domain.getSquareSize()) {
						g.setColor(errorColor);
					} else if (number == domain.getSquareSize()) {
						g.setColor(areaPaintColor);
					} else if (number > domain.getSquareSize()) {
						g.setColor(smallSizeColor);
					}
				}
				else if (isColorfulMode()) {
//					g.setColor(Colors.getColor(board.areaList.indexOf(board.domain[r][c])));
					g.setColor(Colors.getBrightColor(board.getSquare(r,c).getId()));
				}
				else {
					g.setColor(areaPaintColor);
				}
				paintCell(g, r, c);
			}
		}
		g.setFont(getNumberFont());
		g.setColor(getNumberColor());
		int num;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				num = board.getNumber(r, c);
				if (num > 0) {
					placeNumber(g, r, c, num);
				} else if (num == Board.UNDECIDED_NUMBER) {
					placeNumber(g, r, c, num);
//					g.setColor(Color.BLACK);
//					placeBoldCircle(g, r, c);
				}
			}
		}
		g.setColor(areaBorderColor);
		Square area;
		for (Iterator itr = board.getSquareListIterator(); itr.hasNext();) {
			area = (Square) itr.next();
			placeSquare(g, area.r0, area.c0, area.r1, area.c1);
			//			System.out.println(++c + area.toString());
		}
	}
	
	public void placeNumber(Graphics g, int r, int c, int num) {
		g.setColor(Color.BLACK);
		super.placeFilledCircle(g, r, c, (int)(getCellSize()*0.85) );
		g.setColor(Color.WHITE);
		if (num > 0)
		super.placeNumber(g, r, c, num);
	}

	/**
	 *  ドラッグ中の四角を描画する
	 * @param g
	 */
	private void drawDragging(Graphics g) {
		Square area = getDraggingArea();
		if (area == null)
			return;
		placeSquare(g, area.r0, area.c0, area.r1, area.c1);
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
