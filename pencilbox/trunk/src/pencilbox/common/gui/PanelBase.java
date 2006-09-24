package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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

	protected BufferedImage backgroundImage;
    protected BufferedImage originalImage;
    protected AffineTransform backgroundImageTransform = AffineTransform.getRotateInstance(Math.toRadians(0.0));
    protected boolean useBackgroundImage = false;

	private int cellSize = 26;
	private int halfCellSize = cellSize / 2;
	private int circleSize = 18;
	private int crossSize = 8; // �Б��T�C�Y
	private int smallCrossSize = 3; // �Б��T�C�Y
	private int offsetx = 26;
	private int offsety = 26;

	private Color backgroundColor = Color.WHITE;
	private Color borderColor = Color.BLACK;
	private Color gridColor = Color.BLACK;
	private Color numberColor = Color.BLACK;

	private Color cursorColor = new Color(0xFF0000);
	private Color cursorColor2 = new Color(0x0000FF);

	private Font indexFont = new Font("SansSerif", Font.ITALIC, 13);
	private Font numberFont = new Font("SansSerif", Font.PLAIN, 20);

	private int displayStyle = 0;
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
	 * �w�i�摜�ݒ�
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		this.backgroundImage = image;
		this.originalImage = image;
		repaint();
	}
	/**
	 * �\���T�C�Y��ύX����
	 * @param cellSize �}�X�̃T�C�Y
	 */
	protected void setDisplaySize(int cellSize) {

		this.cellSize = cellSize;
		if (showIndexMode) {
			offsetx = cellSize;
			offsety = cellSize;
		}
		setHalfCellSize(cellSize / 2);
		circleSize = (int) (cellSize * 0.7);
		crossSize = (int) (cellSize * 0.3);
		smallCrossSize = (int) (cellSize * 0.15);
		numberFont = new Font("SansSerif", Font.PLAIN, cellSize * 4 / 5);
		indexFont = new Font("SansSerif", Font.ITALIC, cellSize / 2);
		//		if (size==14){
		//		}
		//		else if (size==20) {
		//		}
		//		else if (size==26) {
		//		}
		updatePreferredSize();
		repaint();
	}
	/**
	 * �r���\���X�^�C���擾
	 * @return ���݂̔ԍ�
	 */
	protected int getDisplayStyle() {
		return displayStyle;
	}
	/**
	 * �r���\���X�^�C���ݒ�
	 * @param i �ݒ肷��ԍ�
	 */
	protected void setDisplayStyle(int i) {
		displayStyle = i;
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
		//		Graphics2D g2 = (Graphics2D) g;
		//		context = g2.getFontRenderContext();
		drawPanel(g);
	}
	/**
	 * �p�l���`�惁�\�b�h�ŁC��ʕ\���p�ƈ���p�ŋ��p����D
	 * �X�̃T�u�N���X�Ŏ�������
	 * @param g
	 */
	public void drawPanel(Graphics g) {
	}
	/**
	 * Panel��̗���W���s�N�Z��x���W�ɕϊ�����
	 * @param c Panel��̗���W
	 * @return �ϊ���̃s�N�Z�����W
	 */
	public final int toX(int c) {
		return offsetx + cellSize * c;
	}
	/**
	 * Panel��̗���W���s�N�Z��y���W�ɕϊ�����
	 * @param r Panel��̍s���W
	 * @return �ϊ���̃s�N�Z�����W
	 */
	public final int toY(int r) {
		return offsety + cellSize * r;
	}

	/*
	 * �Ֆʈꕔ�`��p���\�b�h�Q
	 */
	/**
	 * �Ֆʂ̔w�i�� backgraoundColor �œh��Ԃ�
	 * @param g
	 */
	public void paintBackground(Graphics g) {
		if (useBackgroundImage && backgroundImage != null) {
			Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(backgroundImage, backgroundImageTransform, null);
		} else {
			g.setColor(backgroundColor);
			g.fillRect(offsetx, offsety, cellSize * cols(), cellSize * rows());
		}
	}
	/**
	 * �Ֆʂ̊O�g��`��
	 * @param g
	 */
	public void drawBorder(Graphics g) {
		g.setColor(borderColor);
		g.drawRect(offsetx - 1, offsety - 1, cellSize * cols() + 2,	cellSize * rows() + 2);
		g.drawRect(offsetx, offsety, cellSize * cols(), cellSize * rows());
	}
	/**
	 * �r����`��
	 * @param g
	 */
	public void drawGrid(Graphics g) {
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
	public void drawIndex(Graphics g) {
		int firstIndex = 1;
		g.setFont(indexFont);
		g.setColor(numberColor);
		if (showIndexMode == false)
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
	public void drawCursor(Graphics g) {
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (cursorOn) {
			g.setColor(cursorColor2);
		} else {
			return;
		}
		g.drawRect(toX(cellCursor.c()), toY(cellCursor.r()), cellSize, cellSize);
		g.drawRect(toX(cellCursor.c()) + 1,	toY(cellCursor.r()) + 1, cellSize - 2, cellSize - 2);
		g.drawRect(toX(cellCursor.c()) + 2,	toY(cellCursor.r()) + 2, cellSize - 4, cellSize - 4);
	}
	/*
	 * ����}�`�`��̂��߂̃��\�b�h�Q
	 */
	/**
	 * �����̍��W�����܂��͏�̒[�_�Ƃ��āC�Z���̂P�ӂ̒����Ɠ��������̉��܂��͏c�̐���`��
	 * @param g
	 * @param x    ���S��x���W
	 * @param y    ���S��y���W
	 * @param direction �c�� �Ȃ� �c�̐��C ���� �Ȃ� ���̐� ������
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction) {
		if (direction == Direction.HORIZ)
			g.fillRect(x, y - 1, cellSize + 1, 3);
		else if (direction == Direction.VERT)
			g.fillRect(x - 1, y, 3, cellSize + 1);
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
	 * �����̍��W�𒆐S�Ƃ��āC�Z���̂P�ӂ̒����Ɠ��������̉��܂��͏c�̐���`��
	 * @param g
	 * @param x
	 * @param y
	 * @param direction
	 */
	public void drawMidline(Graphics g, int x, int y, int direction) {
		int length = getHalfCellSize();
		if (direction == Direction.HORIZ)
			g.drawLine(x - length, y, x + length, y);
		else if (direction == Direction.VERT)
			g.drawLine(x, y - length, x, y + length);
	}
	/**
	 * �����̍��W�𒆐S�Ƃ��āC�Z���̂P�ӂ̒����Ɠ��������ő���3�̉��܂��͏c�̐���`��
	 * @param g
	 * @param x
	 * @param y
	 * @param direction
	 */
	public void drawMidline3(Graphics g, int x, int y, int direction) {
		int length = getHalfCellSize();
		if (direction == Direction.HORIZ) {
			g.drawLine(x - length, y - 1, x + length, y - 1);
			g.drawLine(x - length, y, x + length, y);
			g.drawLine(x - length, y + 1, x + length, y + 1);
		} else if (direction == Direction.VERT) {
			g.drawLine(x - 1, y - length, x - 1, y + length);
			g.drawLine(x, y - length, x, y + length);
			g.drawLine(x + 1, y - length, x + 1, y + length);
		}
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
	 * �}�X�ɕ�����z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param letter �`������
	 */
	public void placeLetter(Graphics g, int r, int c, char letter) {
		FontMetrics metrics = g.getFontMetrics();
		try {
			String string = Character.toString(letter);
			g.drawString(
				string,
				(toX(c)
					+ (cellSize - 1 - metrics.stringWidth(string)) / 2
					+ 1),
				(toY(r)
					+ (cellSize - 1 - metrics.getHeight()) / 2
					+ metrics.getAscent())
					+ 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �}�X�ɐ�����z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param number �`������
	 */
	public void placeNumber(Graphics g, int r, int c, int number) {
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(r)
					+ (cellSize - 1 - metrics.getHeight()) / 2
					+ metrics.getAscent())
					+ 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	/**
	 * �}�X�ɐ�����z�u����
	 * @param g
	 * @param r �Ֆʏ�̍s���W
	 * @param c �Ֆʏ�̗���W
	 * @param number �`������
	 */
	public void placeIndexNumber(Graphics g, int r, int c, int number) {
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(r)
					+ (cellSize - 1 - metrics.getHeight()) / 2
					+ metrics.getAscent())
					+ 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �}�X��h��Ԃ�
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void paintCell(Graphics g, int r, int c) {
		g.fillRect(
			toX(c) + 1,
			toY(r) + 1,
			cellSize - 1,
			cellSize - 1);
	}

	/**
	 * �}�X�Ɂ����z�u����
	 * �傫���̓N���X�Œ�߂�W���l
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeCircle(Graphics g, int r, int c) {
		g.drawOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize,
			circleSize);
	}
	/**
	 * �}�X�Ɂ����z�u����
	 * �傫���������Ŏw�肷��
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param circleSize �z�u���遛��̒��a
	 */
	public void placeCircle(Graphics g, int r, int c, int circleSize) {
		g.drawOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize,
			circleSize);
	}
	/**
	 * �}�X�Ɂ����z�u����
	 * �傫���̓Z���ɓ��ڂ���傫���Ƃ���
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeLargeCircle(Graphics g, int r, int c) {
		placeCircle(g, r, c, cellSize - 2);
	}
	/**
	 * �}�X�ɐ���2�́����z�u����
	 * �傫���̓N���X�Œ�߂�W���l
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeBoldCircle(Graphics g, int r, int c) {
		int x = toX(c) + (cellSize - circleSize) / 2;
		int y = toY(r) + (cellSize - circleSize) / 2;
		g.drawOval(x, y, circleSize, circleSize);
		g.drawOval(x + 1, y + 1, circleSize - 2, circleSize - 2);
	}
	/**
	 * �}�X�ɐ���2�́����z�u����
	 * �傫���������Ŏw�肷��
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param circleSize �z�u���遛��̒��a
	 */
	public void placeBoldCircle(Graphics g, int r, int c, int circleSize) {
		int x = toX(c) + (cellSize - circleSize) / 2;
		int y = toY(r) + (cellSize - circleSize) / 2;
		g.drawOval(x, y, circleSize, circleSize);
		g.drawOval(x + 1, y + 1, circleSize - 2, circleSize - 2);
	}
	/**
	 * �}�X�ɓh��Ԃ��������z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeFilledCircle(Graphics g, int r, int c) {
		g.fillOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize + 1,
			circleSize + 1);
	}
	/**
	 * �}�X�ɓ��ڂ���傫���̓h��Ԃ��������z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeLargeFilledCircle(Graphics g, int r, int c) {
		placeFilledCircle(g, r, c, cellSize - 2);
	}
	/**
	 * �}�X�ɓh��Ԃ��������z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 * @param circleSize �z�u���遜��̒��a
	 */
	public void placeFilledCircle(Graphics g, int r, int c, int circleSize) {
		g.fillOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize + 1,
			circleSize + 1);
	}
	/**
	 * �}�X�Ɂ~���z�u����
	 * @param g
	 * @param r �Ֆʍs���W
	 * @param c �Ֆʗ���W
	 */
	public void placeCross(Graphics g, int r, int c) {
		drawCross(
			g,
			toX(c) + getHalfCellSize(),
			toY(r) + getHalfCellSize(),
			crossSize);
	}
	/**
	 * �ӏ�ɐ���z�u����
	 * @param g
	 * @param d
	 * @param r
	 * @param c
	 */
	public void placeSideLine(Graphics g, int d, int r, int c) {
		drawLineSegment(
			g,
			toX(c + (d ^ 1)),
			toY(r + d),
			d);
	}
	/**
	 * �ӂƌ����������z�u����
	 * @param g
	 * @param d
	 * @param r
	 * @param c
	 */
	public void placeTraversalLine(Graphics g, int d, int r, int c) {
		drawLineSegment(
			g,
			toX(c) + getHalfCellSize(),
			toY(r) + getHalfCellSize(),
			d ^ 1);
	}
	/**
	 * �ӏ�Ɂ~���z�u����
	 * @param g
	 * @param d
	 * @param r
	 * @param c
	 */
	public void placeSideCross(Graphics g, int d, int r, int c) {
		if (d == Direction.VERT)
			drawCross(
				g,
				toX(c + 1),
				toY(r) + getHalfCellSize(),
				smallCrossSize);
		else if (d == Direction.HORIZ)
			drawCross(
				g,
				toX(c) + getHalfCellSize(),
				toY(r + 1),
				smallCrossSize);
	}
	/**
	 * �}�X�̒��S�ɉ��܂��͏c�̐���z�u����
	 * @param g
	 * @param r
	 * @param c
	 * @param dir
	 */
	public void placeMidline(Graphics g, int r, int c, int dir) {
		drawMidline(
			g,
			toX(c) + getHalfCellSize(),
			toY(r) + getHalfCellSize(),
			dir);
	}
	/**
	 * �l�p��z�u���� 
	 * @param g 
	 * @param r0 �Ֆʍs���W
	 * @param c0 �Ֆʗ���W
	 * @param r1 �Ֆʍs���W
	 * @param c1 �Ֆʗ���W
	 */
	public void placeSquare(Graphics g, int r0, int c0, int r1, int c1) {
		g.drawRect(
			toX((c0 < c1) ? c0 : c1) + 1,
			toY((r0 < r1) ? r0 : r1) + 1,
			cellSize * (((c0 < c1) ? c1-c0 : c0-c1) + 1) - 2,
			cellSize * (((r0 < r1) ? r1-r0 : r0-r1) + 1) - 2);
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
	 * @param halfCellSize The halfCellSize to set.
	 */
	public void setHalfCellSize(int halfCellSize) {
		this.halfCellSize = halfCellSize;
	}
	/**
	 * @return Returns the halfCellSize.
	 */
	public int getHalfCellSize() {
		return halfCellSize;
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
	 * @param crossSize The crossSize to set.
	 */
	public void setCrossSize(int crossSize) {
		this.crossSize = crossSize;
	}
	/**
	 * @return Returns the crossSize.
	 */
	public int getCrossSize() {
		return crossSize;
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
	 * @param borderColor The borderColor to set.
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	/**
	 * @return Returns the borderColor.
	 */
	public Color getBorderColor() {
		return borderColor;
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
			setOffsetx(1);
			setOffsety(1);
		}
		updatePreferredSize();
	}
}

