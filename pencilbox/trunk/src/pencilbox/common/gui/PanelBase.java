package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JPanel;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Size;


/**
 * �y���V���p�Y���t���[�����[�N�̃p�l���N���X
 * �ʃp�Y���̃p�l���N���X�̃X�[�p�[�N���X�ƂȂ�
 * �����̃p�Y���ŋ��ʂŗ��p����郁�\�b�h���L�q���Ă���
 * �e�p�Y���ŌŗL�̑���̓T�u�N���X�ŋL�q����
 */

public class PanelBase extends JPanel implements Printable {

	private Size size;

	private int cellSize = 26;
	private int circleSize = 18;
	private int smallCrossSize = 3; // �Б��T�C�Y
	private int offsetx = 26;
	private int offsety = 26;

	private Color backgroundColor = Color.WHITE;
	private Color boardBorderColor = Color.BLACK;
	private Color gridColor = Color.BLACK;
	private Color numberColor = Color.BLACK;
	private Color indexColor = Color.BLACK;
	private Color errorColor = Color.RED;

	private Color cursorColor = new Color(0xFF0000);
	private Color answerCursorColor = new Color(0x0000FF);

	private Font indexFont = new Font("SansSerif", Font.ITALIC, 13);
	private Font numberFont = new Font("SansSerif", Font.PLAIN, 20);

	private int gridStyle = 1;   // 0:��\���@�P�F�\��
	private int markStyle = 1;
	private boolean showIndexMode = true;
	private boolean cursorOn = false;
	private CellCursor cellCursor;

	/**
	 * true �Ŗ����̓��[�h�Cfalse �ŉ𓚓��̓��[�h	 
	 */
	private boolean problemEditMode = false;

	/**
	 * �p�l�������R���X�g���N�^
	 */
	public PanelBase() {
		setFocusable(true);
	}
	/**
	 * �p�l���̏����ݒ���s��
	 * Board �Ɗ֘A�t����
	 * @param board �Ֆ�
	 */
	public void setup(BoardBase board) {
		size = board.getSize();
		updatePreferredSize();
		setBoard(board);
		cellCursor = createCursor();
	}
	/**
	 *  �J�[�\���𐶐�����
	 * @return ���������J�[�\��
	 */
	public CellCursor createCursor() {
		return new CellCursor();
	}
	/**
	 * �ʃN���X�̃p�l���ɌʃN���X�̔Ֆʂ�ݒ肷�邽�߂̃��\�b�h
	 * �e�ʃN���X�ŃI�[�o�[���C�h����
	 * @param board �Ֆ�
	 */
	protected void setBoard(BoardBase board) {
	}

	/**
	 * �\���T�C�Y��ύX����
	 * @param cellSize �}�X�̃T�C�Y
	 */
	public void setDisplaySize(int cellSize) {
		this.cellSize = cellSize;
		if (showIndexMode) {
			offsetx = cellSize;
			offsety = cellSize;
		}
		circleSize = (int) (cellSize * 0.7);
		smallCrossSize = (int) (cellSize * 0.15);
		numberFont = new Font("SansSerif", Font.PLAIN, cellSize * 4 / 5);
		indexFont = new Font("SansSerif", Font.ITALIC, cellSize / 2);
		updatePreferredSize();
		repaint();
	}
	/**
	 * �r���\���X�^�C���擾
	 * @return ���݂̔ԍ�
	 */
	protected int getGridStyle() {
		return gridStyle;
	}
	/**
	 * �r���\���X�^�C���ݒ�
	 * @param i �ݒ肷��ԍ�
	 */
	protected void setGridStyle(int i) {
		gridStyle = i;
	}
	/**
	 * @return the markStyle
	 */
	public int getMarkStyle() {
		return markStyle;
	}
	/**
	 * @param markStyle the markStyle to set
	 */
	public void setMarkStyle(int markStyle) {
		this.markStyle = markStyle;
	}
	/**
	 * ���݂̔Ֆʂ̏�Ԃɍ��킹�āCsetPreferredSize() ���s��
	 */
	protected void updatePreferredSize() {
		setPreferredSize(getBoardRegionSize());
		revalidate();
	}
	/**
	 * Panel�̔Ֆʗ̈敔���̃T�C�Y���擾����
	 */
	public Dimension getBoardRegionSize() {
		return new Dimension(
				offsetx * 2 + cellSize * cols() + 1,
				offsety * 2 + cellSize * rows() + 1);
	}
	/**
	 * ���ҏW�\���[�h�̐ݒ���s��
	 * @param problemEditMode The problemEditMode to set.
	 */
	public void setProblemEditMode(boolean problemEditMode) {
		this.problemEditMode = problemEditMode;
	}
	/**
	 * @return Returns the problemEditMode.
	 */
	public boolean isProblemEditMode() {
		return problemEditMode;
	}
	/**
	 * ���݂̉�]��Ԃɉ������s�����擾����
	 * @return �p�l����̔Ֆʂ̍s�� 
	 */
	public int rows() {
		return size.getRows();
	}
	/**
	 * ���݂̉�]��Ԃɉ������񐔂��擾����
	 * @return �p�l����̔Ֆʂ̗�
	 */
	public int cols() {
		return size.getCols();
	}

	/*
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawPanel((Graphics2D) g);
	}
	/**
	 * �p�l����`�悷��B
	 * ��ʕ\���p�C����p�C�摜�쐬�p�ŋ��ʂɎg�p����B
	 * @param g
	 */
	public void drawPanel(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		paintBackground(g);
		drawBoard(g);
		drawCursor(g);
		drawIndex(g);
	}
	
	/**
	 * �p�l���ɔՖʂ�`�悷��
	 * �X�̃T�u�N���X�Ŏ�������B
	 * @param g
	 */
	public void drawBoard(Graphics2D g) {
	}

	/**
	 * Panel��̗���W���s�N�Z��x���W�ɕϊ�����
	 * @param c Panel��̗���W
	 * @return �ϊ���̃s�N�Z�����W
	 */
	public final int toX(int c) {
		return getOffsetx() + getCellSize() * c;
	}
	/**
	 * Panel��̗���W���s�N�Z��y���W�ɕϊ�����
	 * @param r Panel��̍s���W
	 * @return �ϊ���̃s�N�Z�����W
	 */
	public final int toY(int r) {
		return getOffsety() + getCellSize() * r;
	}

	/*
	 * �Ֆʈꕔ�`��p���\�b�h�Q
	 */
	/**
	 * �Ֆʂ̔w�i�� backgraoundColor �œh��Ԃ�
	 * @param g
	 */
	public void paintBackground(Graphics2D g) {
		g.setColor(backgroundColor);
		g.fillRect(offsetx, offsety, cellSize * cols(), cellSize * rows());
	}
	/**
	 * �Ֆʂ̊O�g��`��
	 * @param g
	 */
	public void drawBoardBorder(Graphics2D g) {
		g.setColor(boardBorderColor);
		for (int i=0; i<=1; i++)
			g.drawRect(offsetx - i, offsety - i, cellSize * cols() + i + i,	cellSize * rows() + i + i);
	}
	/**
	 * �r����`��
	 * @param g
	 */
	public void drawGrid(Graphics2D g) {
		if (getGridStyle() == 0)
			return;
		g.setColor(gridColor);
		for (int r = 1; r < rows(); r++) {
			g.drawLine(toX(0), toY(r), toX(cols()), toY(r));
		}
		for (int c = 1; c < cols(); c++) {
			g.drawLine(toX(c), toY(0), toX(c), toY(rows()));
		}
	}
	/**
	 * �Ֆʂ̏�ƍ��̒[�ɍ��W������`��
	 * @param g
	 */
	public void drawIndex(Graphics2D g) {
		int firstIndex = 1;
		g.setFont(indexFont);
		g.setColor(indexColor);
		if (isShowIndexMode() == false)
			return;
		for (int r = 0; r < rows(); r++) {
			placeIndexNumber(g, r, -1, r + firstIndex);
		}
		for (int c = 0; c < cols(); c++) {
			placeIndexNumber(g, -1, c, c + firstIndex);
		}
	}
	/**
	 * �J�[�\����`��
	 * @param g
	 */
	public void drawCursor(Graphics2D g) {
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (cursorOn) {
			g.setColor(answerCursorColor);
		} else {
			return;
		}
		for (int i = 0; i <= 2; i++)
			g.drawRect(toX(cellCursor.c())+i, toY(cellCursor.r())+i, cellSize-i-i, cellSize-i-i);
	}
	/*
	 * ����}�`�`��̂��߂̃��\�b�h�Q
	 */
	/**
	 * �����̍��W�����܂��͏�̒[�_�Ƃ��āC�Z���̂P�ӂ̒����Ɠ��������̉��܂��͏c�̐���`���B
	 * @param g
	 * @param x �[�_��x���W
	 * @param y �[�_��y���W
	 * @param direction �c�� �Ȃ� �c�̐��C ���� �Ȃ� ���̐� ������
	 * @param width ����
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction, int width) {
		if (direction == Direction.HORIZ)
			g.fillRect(x, y - width/2, cellSize + 1, width);
		else if (direction == Direction.VERT)
			g.fillRect(x - width/2, y, width, cellSize + 1);
	}
	/**
	 * �����̍��W�����܂��͏�̒[�_�Ƃ��āC�Z���̂P�ӂ̒����̉��܂��͏c�̐���`��
	 * @param g
	 * @param x ���S��x���W
	 * @param y ���S��y���W
	 * @param direction �c�� �Ȃ� �c�̐��C ���� �Ȃ� ���̐� ������
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction) {
		drawLineSegment(g, x, y, direction, 3);
	}
	/**
	 * �����̓_�𒆐S�ɁC�����̑傫���̎l�p��`�� �i�傫���@halfSize*2+1�j
	 * @param g
	 * @param x   ���S��x���W
	 * @param y   ���S��y���W
	 * @param halfSize  �傫���i�Б��j
	 */
	public void fillSquare(Graphics g, int x, int y, int halfSize) {
		g.fillRect(x - halfSize, y - halfSize, halfSize + halfSize + 1, halfSize + halfSize + 1);
	}
	/**
	 * �����̓_�𒆐S�ɁC�����̑傫���̃o�c���`��
	 * @param g
	 * @param x    ���S��x���W
	 * @param y    ���S��y���W
	 * @param halfSize �傫���i�Б��j
	 */
	public void drawCross(Graphics g, int x, int y, int halfSize) {
		g.drawLine(x - halfSize, y - halfSize, x + halfSize, y + halfSize);
		g.drawLine(x - halfSize, y + halfSize, x + halfSize, y - halfSize);
	}
	/**
	 * ���S�̍��W�Ɣ��a��^���āC�~��`��
	 * @param g
	 * @param x ���S��x���W
	 * @param y ���S��y���W
	 * @param radius ���a
	 */
	public void drawCircle(Graphics g, int x, int y, int radius) {
		g.drawOval(x-radius, y-radius, radius+radius, radius+radius);
	}
	/**
	 * ���S�̍��W�Ɣ��a��^���āC�h��Ԃ����~��`���B
	 * @param g
	 * @param x ���S��x���W
	 * @param y ���S��y���W
	 * @param radius ���a
	 */
	public void fillCircle(Graphics g, int x, int y, int radius) {
		g.fillOval(x-radius, y-radius, radius+radius+1, radius+radius+1);		
	}
	/**
	 * �����̍��W�𒆐S�Ƃ��ĕ������`��
	 * @param g
	 * @param x ���S��x���W
	 * @param y ���S��y���W
	 * @param str �`��������
	 */
	public void drawString(Graphics g, int x, int y, String str) {
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(
				str,
				x - metrics.stringWidth(str) / 2,
				y - metrics.getHeight() / 2 + metrics.getAscent());
	}

	/*
	 * �}�X�̓��e��`�悷�邽�߂̃��\�b�h�Q
	 * �}�X�̍��W��^����ƁC���̃Z���̓��e��`�悷��D
	 * �ȉ��̃��\�b�h���p�ӂ���Ă���
	 * ������`��
	 * �h��Ԃ�
	 * ����`��
	 * ����`��
	 * �~��`��
	 * �����܂��͏c����`��
	 */
	/**
	 * �}�X�̒����ɕ������z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param string �`��������
	 */
	public void placeString(Graphics2D g, int r, int c, String string) {
		drawString(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), string);
	}
	/**
	 * �}�X�ɕ�����z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param letter �`������
	 */
	public void placeLetter(Graphics2D g, int r, int c, char letter) {
		placeString(g, r, c, Character.toString(letter));
	}
	/**
	 * �}�X�ɐ�����z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param number �`������
	 */
	public void placeNumber(Graphics2D g, int r, int c, int number) {
		placeString(g, r, c, Integer.toString(number));
	}
	/**
	 * �}�X�ɐ�����z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param number �`������
	 */
	public void placeIndexNumber(Graphics2D g, int r, int c, int number) {
		placeString(g, r, c, Integer.toString(number));
	}
	/**
	 * �}�X��h��Ԃ�
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void paintCell(Graphics2D g, int r, int c) {
		g.fillRect(toX(c), toY(r), getCellSize(), getCellSize());
	}

	/**
	 * �}�X�Ɂ����z�u����
	 * �傫���̓N���X�Œ�߂�W���l
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeCircle(Graphics2D g, int r, int c) {
		placeCircle(g, r, c, getCircleSize());
	}
	/**
	 * �}�X�Ɂ����z�u����
	 * �傫���������Ŏw�肷��
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param circleSize �z�u���遛��̒��a
	 */
	public void placeCircle(Graphics2D g, int r, int c, int circleSize) {
		drawCircle(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(),
				circleSize / 2);
	}
	/**
	 * �}�X�ɐ���2�́����z�u����
	 * �傫���̓N���X�Œ�߂�W���l
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeBoldCircle(Graphics2D g, int r, int c) {
		placeBoldCircle(g, r, c, getCircleSize());
	}
	/**
	 * �}�X�ɐ���2�́����z�u����
	 * �傫���������Ŏw�肷��
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param circleSize �z�u���遛��̒��a
	 */
	public void placeBoldCircle(Graphics2D g, int r, int c, int circleSize) {
		int x = toX(c) + getHalfCellSize();
		int y = toY(r) + getHalfCellSize();
		drawCircle(g, x, y, circleSize / 2);
		drawCircle(g, x, y, circleSize / 2 - 1);
	}
	/**
	 * �}�X�ɓh��Ԃ��������z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeFilledCircle(Graphics2D g, int r, int c) {
		placeFilledCircle(g, r, c, getCircleSize());
	}
	/**
	 * �}�X�ɓh��Ԃ��������z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param circleSize �z�u���遜��̒��a
	 */
	public void placeFilledCircle(Graphics2D g, int r, int c, int circleSize) {
		fillCircle(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(),
				circleSize / 2);
	}
	/**
	 * �}�X�̒����Ɂ���z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeFilledSquare(Graphics2D g, int r, int c) {
		fillSquare(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), getSmallCrossSize());
	}
	/**
	 * �}�X�Ɂ~���z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeCross(Graphics2D g, int r, int c) {
		drawCross(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(),
				getSmallCrossSize());
	}
	/**
	 * �ӏ�ɐ���z�u����
	 * @param g
	 * @param d �Ӎ��W
	 * @param r �Ӎ��W
	 * @param c �Ӎ��W
	 */
	public void placeSideLine(Graphics2D g, int d, int r, int c) {
		if (d == Direction.VERT)
			drawLineSegment(g, toX(c + 1), toY(r), d, 3);
		else if (d == Direction.HORIZ)
			drawLineSegment(g, toX(c), toY(r + 1), d, 3);
	}
	/**
	 * �ӂƌ����������z�u����
	 * @param g
	 * @param d �Ӎ��W
	 * @param r �Ӎ��W
	 * @param c �Ӎ��W
	 */
	public void placeLink(Graphics2D g, int d, int r, int c) {
		drawLineSegment(g, toX(c) + getHalfCellSize(), toY(r)
				+ getHalfCellSize(), d ^ 1, 3);
	}
	/**
	 * �ӏ�Ɂ~���z�u����
	 * @param g
	 * @param d �Ӎ��W
	 * @param r �Ӎ��W
	 * @param c �Ӎ��W
	 */
	public void placeSideCross(Graphics2D g, int d, int r, int c) {
		if (d == Direction.VERT)
			drawCross(g, toX(c + 1), toY(r) + getHalfCellSize(), smallCrossSize);
		else if (d == Direction.HORIZ)
			drawCross(g, toX(c) + getHalfCellSize(), toY(r + 1), smallCrossSize);
	}
	/**
	 * �}�X�̒��S�ɉ��܂��͏c�̐���z�u����
	 * @param g
	 * @param r
	 * @param c
	 * @param dir
	 */
	public void placeCenterLine(Graphics2D g, int r, int c, int dir) {
		if (dir == Direction.HORIZ)
			drawLineSegment(g, toX(c), toY(r) + getHalfCellSize(), dir, 1);
		else if (dir == Direction.VERT)
			drawLineSegment(g, toX(c) + getHalfCellSize(), toY(r), dir, 1);
	}
	/**
	 * �l�p��z�u���� 
	 * @param g 
	 * @param r0 �Ֆʍs���W
	 * @param c0 �Ֆʗ���W
	 * @param r1 �Ֆʍs���W
	 * @param c1 �Ֆʗ���W
	 */
	public void placeSquare(Graphics2D g, int r0, int c0, int r1, int c1) {
		for (int i = 0; i <= 1; i++)
			g.drawRect(
				toX((c0 < c1) ? c0 : c1) + i,
				toY((r0 < r1) ? r0 : r1) + i,
				getCellSize() * ((c0 < c1) ? c1-c0+1 : c0-c1+1) - i*2,
				getCellSize() * ((r0 < r1) ? r1-r0+1 : r0-r1+1) - i*2);
	}

	/**
	 * �}�X�̉���� 
	 * @param g 
	 * @param r0 �Ֆʍs���W
	 * @param c0 �Ֆʗ���W
	 */
	public void edgeCell(Graphics2D g, int r0, int c0) {
		g.drawRect(toX(c0), toY(r0), getCellSize(), getCellSize());
	}

	/**
	 * �}�X�ɔ��}�X�m��i�Ȃǁj�L����z�u���� �傫���̓N���X�Œ�߂�W���l
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeMark(Graphics2D g, int r, int c) {
		switch (getMarkStyle()) {
		case 1:
			placeCircle(g, r, c);
			break;
		case 2:
			placeFilledCircle(g, r, c);
			break;
		case 3:
			placeFilledSquare(g, r, c);
			break;
		case 4:
			placeCross(g, r, c);
			break;
		case 5:
			paintCell(g, r, c);
			break;
		}
	}

	/* 
	 * �Ֆʈ���p���\�b�h
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pf, int page)
		throws PrinterException {
		if (page >= 1)
			return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(pf.getImageableX(), pf.getImageableY());
		// �\���\�̈�̍���p�����W���_�Ƃ���DcoreJAVA v.2 p.652
		g2.scale(0.7, 0.7); // ������͏k�� 1pixel -> 0.7point
		//		  g2.draw(new Rectangle2D.Double(0, 0, pf.getImageableWidth(), pf.getImageableHeight()));
		drawPanel(g2);
		return Printable.PAGE_EXISTS;
	}
	/**
	 * @param cellSize The cellSize to set.
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	/**
	 * @return Returns the cellSize.
	 */
	public int getCellSize() {
		return cellSize;
	}
	/**
	 * @return Returns the halfCellSize.
	 */
	public int getHalfCellSize() {
		return cellSize / 2;
	}
	/**
	 * @param circleSize The circleSize to set.
	 */
	public void setCircleSize(int circleSize) {
		this.circleSize = circleSize;
	}
	/**
	 * @return Returns the circleSize.
	 */
	public int getCircleSize() {
		return circleSize;
	}
	/**
	 * @param smallCrossSize The smallCrossSize to set.
	 */
	public void setSmallCrossSize(int smallCrossSize) {
		this.smallCrossSize = smallCrossSize;
	}
	/**
	 * @return Returns the smallCrossSize.
	 */
	public int getSmallCrossSize() {
		return smallCrossSize;
	}
	/**
	 * @param offsetx The offsetx to set.
	 */
	public void setOffsetx(int offsetx) {
		this.offsetx = offsetx;
	}
	/**
	 * @return Returns the offsetx.
	 */
	public int getOffsetx() {
		return offsetx;
	}
	/**
	 * @param offsety The offsety to set.
	 */
	public void setOffsety(int offsety) {
		this.offsety = offsety;
	}
	/**
	 * @return Returns the offsety.
	 */
	public int getOffsety() {
		return offsety;
	}
	/**
	 * @param backgroundColor The backgroundColor to set.
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	/**
	 * @return Returns the backgroundColor.
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	/**
	 * @param boardBorderColor The boardBorderColor to set.
	 */
	public void setBoardBorderColor(Color boardBorderColor) {
		this.boardBorderColor = boardBorderColor;
	}
	/**
	 * @return Returns the boardBorderColor.
	 */
	public Color getBoardBorderColor() {
		return boardBorderColor;
	}
	/**
	 * @param gridColor The gridColor to set.
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}
	/**
	 * @return Returns the gridColor.
	 */
	public Color getGridColor() {
		return gridColor;
	}
	/**
	 * @param numberColor The numberColor to set.
	 */
	public void setNumberColor(Color numberColor) {
		this.numberColor = numberColor;
	}
	/**
	 * @return Returns the numberColor.
	 */
	public Color getNumberColor() {
		return numberColor;
	}
	/**
	 * @return Returns the errorColor.
	 */
	public Color getErrorColor() {
		return errorColor;
	}
	/**
	 * @param cursorColor The cursorColor to set.
	 */
	public void setCursorColor(Color cursorColor) {
		this.cursorColor = cursorColor;
	}
	/**
	 * @return Returns the cursorColor.
	 */
	public Color getCursorColor() {
		return cursorColor;
	}
	/**
	 * @param cursorOn The cursorOn to set.
	 */
	public void setCursorOn(boolean cursorOn) {
		this.cursorOn = cursorOn;
	}
	/**
	 * @return Returns the cursorOn.
	 */
	public boolean isCursorOn() {
		return cursorOn;
	}
	/**
	 * @param numberFont The numberFont to set.
	 */
	protected void setNumberFont(Font numberFont) {
		this.numberFont = numberFont;
	}
	/**
	 * @return Returns the numberFont.
	 */
	protected Font getNumberFont() {
		return numberFont;
	}
	/**
	 * @param cellCursor The cellCursor to set.
	 */
	protected void setCellCursor(CellCursor cellCursor) {
		this.cellCursor = cellCursor;
	}
	/**
	 * @return Returns the cellCursor.
	 */
	protected CellCursor getCellCursor() {
		return cellCursor;
	}
	/**
	 * @return the showIndex
	 */
	public boolean isShowIndexMode() {
		return showIndexMode;
	}
	/**
	 * @param showIndex the showIndex to set
	 */
	public void setShowIndexMode(boolean showIndex) {
		this.showIndexMode = showIndex;
	}

	/**
	 * @param b the showIndex to set
	 */
	public void changeShowIndexMode(boolean b) {
		this.showIndexMode = b;
		if (b == true) {
			setOffsetx(this.getCellSize());
			setOffsety(this.getCellSize());
		} else {
			setOffsetx(5);
			setOffsety(5);
		}
		updatePreferredSize();
	}
}

