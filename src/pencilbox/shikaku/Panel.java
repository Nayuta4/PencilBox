package pencilbox.shikaku;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * �u�l�p�ɐ؂�v�p�l���N���X
 */
public class Panel extends PanelEventHandler {

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
	 * @param colorfulMode The colorfulMode to set.
	 */
	public void setColorfulMode(boolean colorfulMode) {
		this.colorfulMode = colorfulMode;
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
		if (getCellCursor() != null) {
			drawCursor(g);
		}
	}
	/**
	 * �Ֆʂ�`�悷��
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
				if (showAreaHint) {
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
				else if (colorfulMode) {
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
	 *  �h���b�O���̎l�p��`�悷��
	 * @param g
	 */
	private void drawDragging(Graphics g) {
		Square area = draggingArea;
		if (area == null)
			return;
		placeSquare(g, area.r0, area.c0, area.r1, area.c1);
	}

	/*
	 * �u�l�p�ɐ؂�v�}�E�X���X�i�[
	 * ���h���b�O�F�h���b�O�n�_�ƏI�_���Q�̒��_�Ƃ��钷���`��`��
	 * �E�v���X�C�h���b�O�F���̃}�X���܂ޒ����`����������
	 */
	private Address dragStart = new Address(Address.NOWEHER);

	protected void leftPressed(Address pos) {
		dragStart.set(pos);
		draggingArea =
			new Square(dragStart.r, dragStart.c, pos.r, pos.c);
	}

	protected void rightPressed(Address dragEnd) {
		board.removeSquareIncluding(dragEnd);
	}
	
	protected void leftDragged(Address dragEnd) {
		if (draggingArea == null)
			return;
		draggingArea.set(dragStart.r, dragStart.c, dragEnd.r, dragEnd.c);
	}
	
	protected void leftDragFixed(Address dragEnd) {
		if (draggingArea == null)
			return;
		draggingArea = null;
		board.addSquareSpanning(dragStart, dragEnd);
		dragStart.setNowhere();
	}
	
	protected void dragFailed() {
		draggingArea = null;
		dragStart.setNowhere();
	}

	/*
	 * �u�l�p�ɐ؂�v�L�[���X�i�[
	 * 
	 * �����̓��[�h�̂Ƃ��̂ݐ������͂�����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
			board.setNumber(pos.r, pos.c, num);
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r, pos.c, 0);
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r, pos.c, Board.UNDECIDED_NUMBER);
	}
}
