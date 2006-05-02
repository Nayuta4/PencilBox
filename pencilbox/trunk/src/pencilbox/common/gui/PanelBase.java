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

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Rotation;
import pencilbox.common.core.SideAddress;
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

	private Rotation rotator = new Rotation();
	private int displayStyle = 0;
	private boolean cursorOn = false;
	private CellCursor cellCursor;

	/**
	 * true �Ŗ����̓��[�h�Cfalse �ŉ𓚓��̓��[�h	 
	 */
	private boolean problemEditMode = false;

	private Address pos0 = new Address();
	private Address pos1 = new Address();
	private SideAddress sidePos = new SideAddress();

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
		this.size = board.getSize();
		rotator.setSize(size);
		updatePreferredSize();
		setBoard(board);
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
	 * Panel�\���̉�]��Ԃ��擾����
	 * @return ���݂̉�]��Ԃ�\���l
	 */
	protected int getRotation() {
		return rotator.getRotation();
	}
	/**
	 * Panel�\���̉�]��Ԃ�ݒ肷��
	 * @param rotation �V������]��Ԃɐݒ肷��l
	 */
	protected void setRotation(int rotation) {
		rotator.setRotation(rotation);
		updatePreferredSize();
		repaint();
	}
	/**
	 * �\���T�C�Y��ύX����
	 * @param cellSize �}�X�̃T�C�Y
	 */
	protected void setDisplaySize(int cellSize) {

		this.cellSize = cellSize;
		offsetx = cellSize;
		offsety = cellSize;
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
		setPreferredSize(
			new Dimension(
				offsetx * 2 + cellSize * cols(),
				offsety * 2 + cellSize * rows()));
		revalidate();
	}
	/**
	 * ���ҏW�\���[�h�̐ݒ���s��
	 * �s���ɂ��C���ҏW���[�h�ł͉�]��W���ɖ߂�
	 * �i�X���U�[�����N�ƓV�̃V���[�̖����͂���]��ԂɑΉ����Ă��Ȃ����߁j
	 * �t���[���̑傫�����A���Ȃ��͓̂�_
	 * @param problemEditMode The problemEditMode to set.
	 */
	public void setProblemEditMode(boolean problemEditMode) {
		this.problemEditMode = problemEditMode;
		if (problemEditMode == true) {
			setRotation(0);
//			switch (getRotation()) {
//			case 2:
//			case 5:
//			case 7:
//				setRotation(0);
//				break;
//			case 1:
//			case 3:
//			case 6:
//				setRotation(4);
//				break;
//			}
		}
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
		return isTransposed() ? size.getCols() : size.getRows();
	}
	/**
	 * ���݂̉�]��Ԃɉ������񐔂��擾����
	 * @return �p�l����̔Ֆʂ̗�
	 */
	public int cols() {
		return isTransposed() ? size.getRows() : size.getCols();
	}
	/**
	 * �p�l���ォ
	 * @param r �p�l����̐����l�s���W
	 * @param c �p�l����̐����l����W
	 * @return �����ɗ^����ꂽ���W���Տ�Ȃ� true �ՊO�Ȃ� false
	 */
	protected boolean isOn(int r, int c) {
		return r >= 0 && r < rows() && c >= 0 && c < cols();
	}
	protected boolean isOn(Address address) {
		return isOn(address.r, address.c);
	}
	protected boolean isSideOn(SideAddress address) {
		if (address.d == Direction.VERT)
			return address.r >= 0
				&& address.r < rows()
				&& address.c >= 0
				&& address.c < cols() - 1;
		else if (address.d == Direction.HORIZ)
			return address.r >= 0
				&& address.r < rows() - 1
				&& address.c >= 0
				&& address.c < cols();
		return false;
	}
	/**
	 * �p�l���ォ
	 * @param r
	 * @param c
	 * @param adjustRow �s���C��
	 * @param adjustCol �񐔏C��
	 * @return �p�l����Ȃ� true
	 */
	protected boolean isOn(int r, int c, int adjustRow, int adjustCol) {
		return r >= 0
			&& r < rows() + adjustRow
			&& c >= 0
			&& c < cols() + adjustCol;
	}
	protected boolean isOn(Address address, int adjustRow, int adjustCol) {
		return isOn(address.r, address.c, adjustRow, adjustCol);
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
	 * Rotation�N���X�Ɉϑ����Ă��郁�\�b�h�Q
	 */
	/**
	 * �p�l���̏c�������Ƃ̏c���ɑ΂��ē]�u����Ă��邩�ǂ���
	 * @return �]�u����Ă���� true
	 */
	public boolean isTransposed() {
		return rotator.isTransposed();
	}
	/**
	 * �p�l����̐����l���W��Տ�̍��W�ɕϊ�����
	 * @param pos
	 */
	public void p2b(Address pos) {
		rotator.p2b(pos);
	}
	/**
	 * �p�l����̐����l���W�����݂̉�]�\����Ԃɉ������Ֆʂ̍��W�ɕϊ�����
	 * ���̍ۂɁC�ՖʃT�C�Y�� rows()+adjustRow, cols()+adjustCol �ł���Ƃ݂Ȃ�
	 * @param pos ���W
	 * @param adjustRow �Ֆʍs�T�C�Y�ɑ΂���␳�l
	 * @param adjustCol �Ֆʗ�T�C�Y�ɑ΂���␳�l
	 */
	public void p2b(Address pos, int adjustRow, int adjustCol) {
		rotator.p2b(pos, adjustRow, adjustCol);
	}
	/**
	 * �p�l����̐����l�Ӎ��W��Տ�̕Ӎ��W�ɕϊ�����
	 * @param pos
	 */
	public void p2bSide(SideAddress pos) {
		rotator.p2bSide(pos);
	}
	/**
	 * �Ֆʏ�̐����l���W�����݂̉�]�\����Ԃɉ������p�l����̍��W�ɕϊ�����
	 * ���̍ۂɁC�ՖʃT�C�Y�� rows()+adjustRow, cols()+adjustCol �ł���Ƃ݂Ȃ�
	 * @param pos ���W
	 * @param adjustRow �Ֆʍs�T�C�Y�ɑ΂���␳�l
	 * @param adjustCol �Ֆʗ�T�C�Y�ɑ΂���␳�l
	 */
	public void b2p(Address pos, int adjustRow, int adjustCol) {
		rotator.b2p(pos, adjustRow, adjustCol);
	}
	/**
	 * �Տ�̍��W���p�l����̐����l���W�ɕϊ�����
	 * @param pos
	 */
	public void b2p(Address pos) {
		rotator.b2p(pos);
	}
	/**
	 * �Տ�̕Ӎ��W���p�l����̐����l�Ӎ��W�ɕϊ�����
	 * @param pos
	 */
	public void b2pSide(SideAddress pos) {
		rotator.b2pSide(pos);
	}
	/**
	 * �Տ�̕������p�l����̕����ɕϊ�����
	 * @param direction �ϊ����̕�����\�����l
	 * @return �ϊ���̕�����\�����l
	 */
	public int rotateDirection(int direction) {
		return rotator.rotateDirection(direction);
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
		int offset = 1;
		g.setFont(indexFont);
		g.setColor(numberColor);
		for (int r = 0; r < rows(); r++) {
			placeIndexNumber(g, r, -1, r + offset);
		}
		for (int c = 0; c < cols(); c++) {
			placeIndexNumber(g, -1, c, c + offset);
		}
	}
	/**
	 * �J�[�\����`��
	 * @param g
	 */
	public void drawCursor(Graphics g) {
		if (!isProblemEditMode() && !cursorOn)
			return;
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (cursorOn) {
			g.setColor(cursorColor2);
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
	 * �}�X�̍��W��^����ƁC�K�v�ɉ����ĉ�]���āC���̃Z���̓��e��`�悷��D
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
		pos0.set(r, c);
		b2p(pos0);
		FontMetrics metrics = g.getFontMetrics();
		try {
			String string = Character.toString(letter);
			g.drawString(
				string,
				(toX(pos0.c)
					+ (cellSize - 1 - metrics.stringWidth(string)) / 2
					+ 1),
				(toY(pos0.r)
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
		pos0.set(r, c);
		b2p(pos0);
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(pos0.c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(pos0.r)
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
		pos0.set(r, c);
//		b2p(pos0);
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(pos0.c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(pos0.r)
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
		pos0.set(r, c);
		b2p(pos0);
		g.fillRect(
			toX(pos0.c) + 1,
			toY(pos0.r) + 1,
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
		pos0.set(r, c);
		b2p(pos0);
		g.drawOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		g.drawOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		int x = toX(pos0.c) + (cellSize - circleSize) / 2;
		int y = toY(pos0.r) + (cellSize - circleSize) / 2;
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
		pos0.set(r, c);
		b2p(pos0);
		int x = toX(pos0.c) + (cellSize - circleSize) / 2;
		int y = toY(pos0.r) + (cellSize - circleSize) / 2;
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
		pos0.set(r, c);
		b2p(pos0);
		g.fillOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		g.fillOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		drawCross(
			g,
			toX(pos0.c) + getHalfCellSize(),
			toY(pos0.r) + getHalfCellSize(),
			crossSize);
	}
	/**
	 * �ӏ�ɐ���z�u����
	 * @param g
	 * @param dir
	 * @param r
	 * @param c
	 */
	public void placeSideLine(Graphics g, int dir, int r, int c) {
		sidePos.set(dir, r, c);
		b2pSide(sidePos);
		drawLineSegment(
			g,
			toX(sidePos.c + (sidePos.d ^ 1)),
			toY(sidePos.r + sidePos.d),
			sidePos.d);
	}
	/**
	 * �ӂƌ����������z�u����
	 * @param g
	 * @param dir
	 * @param r
	 * @param c
	 */
	public void placeTraversalLine(Graphics g, int dir, int r, int c) {
		sidePos.set(dir, r, c);
		b2pSide(sidePos);
		drawLineSegment(
			g,
			toX(sidePos.c) + getHalfCellSize(),
			toY(sidePos.r) + getHalfCellSize(),
			sidePos.d ^ 1);
	}
	/**
	 * �ӏ�Ɂ~���z�u����
	 * @param g
	 * @param dir
	 * @param r
	 * @param c
	 */
	public void placeSideCross(Graphics g, int dir, int r, int c) {
		sidePos.set(dir, r, c);
		b2pSide(sidePos);
		if (sidePos.d == Direction.VERT)
			drawCross(
				g,
				toX(sidePos.c + 1),
				toY(sidePos.r) + getHalfCellSize(),
				smallCrossSize);
		else if (sidePos.d == Direction.HORIZ)
			drawCross(
				g,
				toX(sidePos.c) + getHalfCellSize(),
				toY(sidePos.r + 1),
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
		pos0.set(r, c);
		b2p(pos0);
		int direction = (dir == Direction.HORIZ ^ isTransposed()) ? Direction.HORIZ : Direction.VERT;
		drawMidline(
			g,
			toX(pos0.c) + getHalfCellSize(),
			toY(pos0.r) + getHalfCellSize(),
			direction);
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
		pos0.set(r0, c0);
		pos1.set(r1, c1);
		b2p(pos0);
		b2p(pos1);
		g.drawRect(
			toX((pos0.c < pos1.c) ? pos0.c : pos1.c) + 1,
			toY((pos0.r < pos1.r) ? pos0.r : pos1.r) + 1,
			cellSize
				* (((pos0.c < pos1.c) ? pos1.c - pos0.c : pos0.c - pos1.c) + 1)
				- 2,
			cellSize
				* (((pos0.r < pos1.r) ? pos1.r - pos0.r : pos0.r - pos1.r) + 1)
				- 2);
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
//		repaint();
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
}

