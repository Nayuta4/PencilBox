package pencilbox.shikaku;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * �u�l�p�ɐ؂�v�p�l���N���X
 */
public class Panel extends PanelBase {

	private Board board;

	private Color areaBorderColor = Color.BLUE;
	private Color errorColor = new Color(0xFF0000);
	private Color smallSizeColor = new Color(0xFFFF80); // �ʐϏ�����
	private Color areaPaintColor   = new Color(0x80FFFF); // �W���F

	private Square draggingArea; // �h���b�O���č��܂��ɕ`�����Ƃ��Ă���l�p

	private boolean colorfulMode = false;
	private boolean showAreaHint = false;

	/**
	 * �p�l���𐶐�����
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

	public void drawPanel(Graphics2D g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawDragging(g);
		drawGrid(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * �Ֆʂ�`�悷��
	 * @param g
	 */
	protected void drawBoard(Graphics2D g) {
		Square area;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				area = board.getSquare(r,c);
				if (area == null)
					continue;
				if (isShowAreaHint()) {
					int number = area.getNumber();
					if (number == Square.MULTIPLE_NUMBER) {
						g.setColor(errorColor);
					} else if (number == Square.NO_NUMBER) {
						g.setColor(smallSizeColor);
					} else if (number == Board.UNDECIDED_NUMBER) {
						g.setColor(areaPaintColor);
					} else if (number < area.getSquareSize()) {
						g.setColor(errorColor);
					} else if (number == area.getSquareSize()) {
						g.setColor(areaPaintColor);
					} else if (number > area.getSquareSize()) {
						g.setColor(smallSizeColor);
					}
				} else if (isColorfulMode()) {
					g.setColor(Colors.getBrightColor(board.getSquare(r,c).getId()));
				} else {
					g.setColor(areaPaintColor);
				}
				paintCell(g, r, c);
			}
		}
		g.setFont(getNumberFont());
		int num;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				num = board.getNumber(r, c);
				if (num > 0) {
					placeNumber1(g, r, c, num);
				} else if (num == Board.UNDECIDED_NUMBER) {
					placeNumber1(g, r, c, num);
				}
			}
		}
		g.setColor(areaBorderColor);
		for (Iterator itr = board.getSquareListIterator(); itr.hasNext();) {
			area = (Square) itr.next();
			placeSquare(g, area.r0, area.c0, area.r1, area.c1);
		}
	}
	
	private void placeNumber1(Graphics2D g, int r, int c, int num) {
		g.setColor(Color.BLACK);
		super.placeFilledCircle(g, r, c, (int)(getCellSize()*0.85) );
		g.setColor(Color.WHITE);
		if (num > 0) {
			super.placeNumber(g, r, c, num);
		}
	}

	/**
	 *  �h���b�O���̎l�p��`�悷��
	 * @param g
	 */
	private void drawDragging(Graphics2D g) {
		Square area = getDraggingArea();
		if (area == null)
			return;
		g.setColor(areaBorderColor);	
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
