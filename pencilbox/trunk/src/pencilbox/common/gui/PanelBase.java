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

import pencilbox.common.core.Address;
import pencilbox.common.core.AreaBase;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.core.Size;
import pencilbox.common.core.SquareBase;


/**
 * �y���V���p�Y���t���[�����[�N�̃p�l���N���X
 * �ʃp�Y���̃p�l���N���X�̃X�[�p�[�N���X�ƂȂ�
 * �����̃p�Y���ŋ��ʂŗ��p����郁�\�b�h���L�q���Ă���
 * �e�p�Y���ŌŗL�̑���̓T�u�N���X�ŋL�q����
 */

public class PanelBase extends JPanel implements Printable {

	public static int ANSWER_INPUT_MODE = 0;
	public static int PROBLEM_INPUT_MODE = 1;
	public static int REGION_EDIT_MODE = 3;

	private Size size;

	private int cellSize = 26;
	private int circleSize = 18;
	private int smallCrossSize = 3; // �Б��T�C�Y
	private int linkWidth = 3;

	private int offsetx = 10;
	private int offsety = 10;

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
	private boolean indexMode = false;
	private boolean cursorMode = false;
	private CellCursor cellCursor;
	private int indexStyle[] = new int[] {1, 1};

	private boolean indicateErrorMode = false;
	private boolean showBeamMode = false;
	private boolean paintIlluminatedCellMode = true;
	private boolean showAreaBorderMode = true;
	private boolean separateAreaColorMode = false;
	private boolean highlightSelectionMode = false;
	private boolean dotHintMode = false;
	private boolean separateLinkColorMode = false;
	private boolean hideSoleNumberMode = false;
	private boolean countAreaSizeMode = false;
	private boolean hideStarMode = false;
	private boolean separateTetrominoColorMode = false;

	private Color wallColor = Color.BLACK;
	private Color bulbColor = new Color(0x000099);
	private Color illuminatedCellColor = new Color(0xAAFFFF);
	private Color noBulbColor = new Color(0xFF9999);
	private Color inputColor = new Color(0x000099);
	protected Color areaBorderColor = Color.BLACK;
	protected Color successColor = new Color(0x80FFFF);
	protected Color borderColor = new Color(0xFF0099);
	private Color paintColor = new Color(0x0099FF);
	protected Color noAreaColor = new Color(0xC0C0C0);
	protected Color highlightColor = new Color(0x00FF00);
	protected Color beamColor = new Color(0x800000);
	protected Color draggingAreaColor = new Color(0xCCFFFF);
	private Color lineColor = new Color(0x000099);
	private Color circleColor = new Color(0xFF9999);
	private Color crossColor = new Color(0xFF0099);
	protected Color noStarAreaColor = new Color(0xFFFF99);
	protected Color whiteAreaColor = new Color(0xAAFFFF);
	protected Color blackAreaColor = new Color(0xFFAAFF);
	protected Color starColor = Color.BLACK;
	private Color gateColor = new Color(0x808080);
	protected Color areaPaintColor = new Color(0xAAFFFF);

	protected int selectedNumber = 0;

	private AreaBase copyRegion = new AreaBase();
	private AreaBase pasteRegion = new AreaBase();
	private Color copyRegionColor = new Color(0xFF0000);
	private Color pasteRegionColor = new Color(0xFFAAAA);

	protected char[] letters = {};
	
	/**
	 * �ҏW���[�h	 
	 */
	private int editMode = 0;

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
		if (indexMode) {
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
	 * @return Returns the problemEditMode.
	 */
	public boolean isProblemEditMode() {
		return editMode == PROBLEM_INPUT_MODE;
	}
	/**
	 * @return the editMode
	 */
	public int getEditMode() {
		return editMode;
	}
	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(int editMode) {
		this.editMode = editMode;
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
		if (editMode == REGION_EDIT_MODE)
			drawCopyPasteRegion(g);
		else 
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
	public final int toX(Address p) {
		return toX(p.c());
	}
	/**
	 * Panel��̗���W���s�N�Z��y���W�ɕϊ�����
	 * @param r Panel��̍s���W
	 * @return �ϊ���̃s�N�Z�����W
	 */
	public final int toY(int r) {
		return getOffsety() + getCellSize() * r;
	}
	public final int toY(Address p) {
		return toY(p.r());
	}

	/**
	 * Panel���x�����s�N�Z�����W���}�X���W�ɕϊ�����
	 * @param x Panel��̃s�N�Z�����W��x
	 * @return ����W�ɕϊ��������l
	 */
	private final int toC(int x) {
		return (x + getCellSize() - getOffsetx()) / getCellSize() - 1;
	}
	/**
	 * Panel��̂����s�N�Z�����W���s�����}�X���W�ɕϊ�����
	 * @param y Panel��̃s�N�Z�����W��y
	 * @return �}�X���W�ɕϊ��������l
	 */
	private final int toR(int y) {
		return (y + getCellSize() - getOffsety()) / getCellSize() - 1;
	}

	/**
	 * �����̃s�N�Z�����W�̈ʒu�̃}�X���W���擾����B
	 * @param x
	 * @param y
	 * @return
	 */
	public Address pointToAddress(int x, int y) {
		int r = toR(y);
		int c = toC(x);
		return Address.address(r, c);
	}

	/**
	 * �����̃s�N�Z�����W�ɍł��߂��Ӎ��W���擾����B
	 *                [H, r-1, c]
	 *              ���@���@��
	 *                �_�@�^�@
	 * [V, r, c-1]  �b�@�E�@�b [V, r, c] 
	 *                �^�@�_�@
	 *              ���@���@��
	 *                [H, r, c]
	 * @param x
	 * @param y
	 * @return
	 */
	public SideAddress pointToSideAddress(int x, int y) {
		int r = toR(y);
		int c = toC(x);
		int resx = x - getOffsetx() - getCellSize() * c;
		int resy = y - getOffsety() - getCellSize() * r;
		if (resx + resy < getCellSize()) {
			if (resx < resy)
				return SideAddress.sideAddress(Direction.VERT, r, c-1);
			else
				return SideAddress.sideAddress(Direction.HORIZ, r-1, c);
		} else {
			if (resy < resx)
				return SideAddress.sideAddress(Direction.VERT, r, c);
			else
				return SideAddress.sideAddress(Direction.HORIZ, r, c);
		}
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
		if (isIndexMode() == false)
			return;
		int firstIndex = 1;
		g.setFont(indexFont);
		g.setColor(indexColor);
		String letter;
		String[] indexLettersC = IndexLetters.getIndexLetters(this.indexStyle[0]).getLetters();
		for (int c = 0; c < cols(); c++) {
			if (c < indexLettersC.length) 
				letter = indexLettersC[c];
			else 
				letter = Integer.toString(c + firstIndex);
			placeString(g, -1, c, letter);
		}
		String[] indexLettersR = IndexLetters.getIndexLetters(this.indexStyle[1]).getLetters();
		for (int r = 0; r < rows(); r++) {
			if (r < indexLettersR.length) 
				letter = indexLettersR[r];
			else 
				letter = Integer.toString(r + firstIndex);
			placeString(g, r, -1, letter);
		}
	}
	/**
	 * �J�[�\����`��
	 * @param g
	 */
	public void drawCursor(Graphics2D g) {
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (isCursorMode()) {
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
	 * @param w ����
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction, int w) {
		if (w == 1) {
			if (direction == Direction.HORIZ)
				g.fillRect(x - w/2, y - w/2, cellSize + w, w);
			else if (direction == Direction.VERT)
				g.fillRect(x - w/2, y - w/2, w, cellSize + w);
		} else if (w > 1) { // �p�P�s�N�Z���������Ƃ�
			if (direction == Direction.HORIZ)
				g.fillRect(x - (w-2)/2, y - w/2, cellSize + w-2, w);
			else if (direction == Direction.VERT)
				g.fillRect(x - w/2, y - (w-2)/2, w, cellSize + w-2);
		}
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
	public void placeLetter(Graphics2D g, Address p, char letter) {
		placeLetter(g, p.r(), p.c(),letter);
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
	public void placeNumber(Graphics2D g, Address p, int number) {
		placeNumber(g, p.r(), p.c(), number);
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
	public void paintCell(Graphics2D g, Address p) {
		paintCell(g, p.r(), p.c());
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
	public void placeCircle(Graphics2D g, Address p) {
		placeCircle(g, p.r(), p.c(), getCircleSize());
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
	public void placeCircle(Graphics2D g, Address p, int circleSize) {
		placeCircle(g, p.r(), p.c(), circleSize);
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
	public void placeBoldCircle(Graphics2D g, Address p) {
		placeBoldCircle(g, p.r(), p.c(), getCircleSize());
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
	public void placeFilledCircle(Graphics2D g, Address p) {
		placeFilledCircle(g, p.r(), p.c(), getCircleSize());
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
	public void placeFilledCircle(Graphics2D g, Address p, int circleSize) {
		placeFilledCircle(g, p.r(), p.c(), circleSize);
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
	public void placeCross(Graphics2D g, Address p) {
		placeCross(g, p.r(), p.c());
	}
	/**
	 * �ӏ�ɐ���z�u����
	 * @param g
	 * @param d �Ӎ��W
	 * @param r �Ӎ��W
	 * @param c �Ӎ��W
	 * @param w ����
	 */
	public void placeSideLine(Graphics2D g, int d, int r, int c, int w) {
		if (d == Direction.VERT)
			drawLineSegment(g, toX(c + 1), toY(r), d, w);
		else if (d == Direction.HORIZ)
			drawLineSegment(g, toX(c), toY(r + 1), d, w);
	}
	public void placeSideLine(Graphics2D g, SideAddress p, int w) {
		placeSideLine(g, p.d(), p.r(), p.c(), w);
	}
	public void placeSideLine(Graphics2D g, SideAddress p) {
		placeSideLine(g, p, 3);
	}
	
	/**
	 * �ӂƌ����������z�u����
	 * @param g
	 * @param d �Ӎ��W
	 * @param r �Ӎ��W
	 * @param c �Ӎ��W
	 */
	public void placeLink(Graphics2D g, int d, int r, int c) {
		drawLineSegment(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), d ^ 1, getLinkWidth());
	}
	public void placeLink(Graphics2D g, SideAddress p) {
		placeLink(g, p.d(), p.r(), p.c());
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
	public void placeSideCross(Graphics2D g, SideAddress p) {
		placeSideCross(g, p.d(), p.r(), p.c());
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
	public void placeCenterLine(Graphics2D g, Address p, int dir) {
		placeCenterLine(g, p.r(), p.c(), dir);
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

	public void placeSquare(Graphics2D g, SquareBase sq) {
		placeSquare(g, sq.r0(), sq.c0(), sq.r1(), sq.c1());
	}

	/**
	 * �}�X�̉���� 
	 * @param g 
	 * @param p �}�X���W
	 */
	public void edgeCell(Graphics2D g, Address p, int w) {
		g.drawRect(toX(p), toY(p), getCellSize(), getCellSize());
	}

	/**
	 * �}�X�̉���� 
	 * @param g
	 * @param p �}�X���W
	 * @param w�@����
	 */
	public void edgeCell(Graphics2D g, Address p) {
		int w = 1;
		for (int i = 0; i < w; i++) {
			g.drawRect(toX(p)+i, toY(p)+i, getCellSize()-i-i, getCellSize()-i-i);
		}
	}

	/**
	 * �̈�̉���� 
	 * @param g
	 * @param area �̈�
	 */
	public void edgeArea(Graphics2D g, AreaBase area) {
		Address neighbor;
		for (Address pos : area) {
			for (int dir = 0; dir < 4; dir++) {
				neighbor = Address.nextCell(pos, dir);
				if (area.contains(neighbor)) {
					if (dir >= 2)
						placeSideLine(g, SideAddress.get(pos, dir), 1);
				} else {
					placeSideLine(g, SideAddress.get(pos, dir), 3);
				}
			}
		}
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
	public void placeMark(Graphics2D g, Address p) {
		placeMark(g, p.r(), p.c());
	}

	/* 
	 * �Ֆʈ���p���\�b�h
	 * ���͎g�p���Ȃ�
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pf, int page)
		throws PrinterException {
		if (page >= 1)
			return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
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

	public void setLinkWidth(int linkWidth) {
		this.linkWidth = linkWidth;
	}

	public int getLinkWidth() {
		return linkWidth;
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
	 * @param cursorMode The cursorMode to set.
	 */
	public void setCursorMode(boolean cursorMode) {
		this.cursorMode = cursorMode;
	}
	/**
	 * @return Returns the cursorMode.
	 */
	public boolean isCursorMode() {
		return cursorMode;
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
	 * @return the indexMode
	 */
	public boolean isIndexMode() {
		return indexMode;
	}
	/**
	 * @param indexMode the indexMode to set
	 */
	public void setIndexMode(boolean indexMode) {
		this.indexMode = indexMode;
	}

	/**
	 * @param b the indexMode to set
	 */
	public void changeIndexMode(boolean b) {
		this.indexMode = b;
		if (b == true) {
			setOffsetx(this.getCellSize());
			setOffsety(this.getCellSize());
		} else {
			setOffsetx(10);
			setOffsety(10);
		}
		updatePreferredSize();
	}
	/**
	 * @param dir
	 * @return
	 */
	public int getIndexStyle(int dir) {
		if (dir >=0 && dir<=1)
			return indexStyle[dir];
		return 0;
	}
	/**
	 * @param dir
	 * @param i
	 */
	public void setIndexStyle(int dir, int i) {
		if (dir >=0 && dir<=1)
			this.indexStyle[dir] = i;
	}
	/**
	 * @return the copyRegion
	 */
	AreaBase getCopyRegion() {
		return copyRegion;
	}
	/**
	 * @return the pasteRegion
	 */
	AreaBase getPasteRegion() {
		return pasteRegion;
	}
	
	/**
	 * �u�̈�ҏW���[�h�v�ł̕ҏW�̈��\������B
	 * @param g
	 */
	protected void drawCopyPasteRegion(Graphics2D g) {
		g.setColor(copyRegionColor);
		edgeArea(g, copyRegion);
		g.setColor(pasteRegionColor);
		edgeArea(g, pasteRegion);
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
	 * @return the paintIlluminatedCellMode
	 */
	public boolean isPaintIlluminatedCellMode() {
		return paintIlluminatedCellMode;
	}
	/**
	 * @param paintIlluminatedCellMode the paintIlluminatedCellMode to set
	 */
	public void setPaintIlluminatedCellMode(boolean paintIlluminatedCellMode) {
		this.paintIlluminatedCellMode = paintIlluminatedCellMode;
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
	public boolean isHideSoleNumberMode() {
		return hideSoleNumberMode;
	}
	/**
	 * @param hideSoleNumberMode The hideSoleNumberMode to set.
	 */
	public void setHideSoleNumberMode(boolean hideSoleNumberMode) {
		this.hideSoleNumberMode = hideSoleNumberMode;
	}
	/**
	 * @return the countAreaSizeMode
	 */
	public boolean isCountAreaSizeMode() {
		return countAreaSizeMode;
	}
	/**
	 * @param countAreaSizeMode The countAreaSizeMode to set.
	 */
	public void setCountAreaSizeMode(boolean countAreaSizeMode) {
		this.countAreaSizeMode = countAreaSizeMode;
	}
	/**
	 * @return the highlightSelectionMode
	 */
	public boolean isHighlightSelectionMode() {
		return highlightSelectionMode;
	}
	/**
	 * @param highlightSelectionMode The highlightSelectionMode to set.
	 */
	public void setHighlightSelectionMode(boolean highlightSelectionMode) {
		this.highlightSelectionMode = highlightSelectionMode;
	}
	/**
	 * @return the dotHintMode
	 */
	public boolean isDotHintMode() {
		return dotHintMode;
	}
	/**
	 * @param dotHintMode The dotHintMode to set.
	 */
	public void setDotHintMode(boolean dotHintMode) {
		this.dotHintMode = dotHintMode;
	}
	/**
	 * @return the separateLinkColorMode
	 */
	public boolean isSeparateLinkColorMode() {
		return separateLinkColorMode;
	}
	/**
	 * @return the hideStarMode
	 */
	public boolean isHideStarMode() {
		return hideStarMode;
	}

	/**
	 * @param hideStarMode The hideStarMode to set.
	 */
	public void setHideStarMode(boolean hideStarMode) {
		this.hideStarMode = hideStarMode;
	}
	/**
	 * @param separateLinkColorMode The separateLinkColorMode to set.
	 */
	public void setSeparateLinkColorMode(boolean separateLinkColorMode) {
		this.separateLinkColorMode = separateLinkColorMode;
	}
	/**
	 * @return the separateTetrominoColorMode
	 */
	public boolean isSeparateTetrominoColorMode() {
		return separateTetrominoColorMode;
	}
	/**
	 * @param separateTetrominoColorMode the separateTetrominoColorMode to set
	 */
	public void setSeparateTetrominoColorMode(boolean separateTetrominoColorMode) {
		this.separateTetrominoColorMode = separateTetrominoColorMode;
	}
	/**
	 * @return Returns the bulbColor.
	 */
	public Color getBulbColor() {
		return bulbColor;
	}
	/**
	 * @param bulbColor The bulbColor to set.
	 */
	public void setBulbColor(Color bulbColor) {
		this.bulbColor = bulbColor;
	}
	/**
	 * @return Returns the noBulbColor.
	 */
	public Color getNoBulbColor() {
		return noBulbColor;
	}
	/**
	 * @param noBulbColor The noBulbColor to set.
	 */
	public void setNoBulbColor(Color noBulbColor) {
		this.noBulbColor = noBulbColor;
	}
	/**
	 * @param wallColor the wallColor to set
	 */
	public void setWallColor(Color wallColor) {
		this.wallColor = wallColor;
	}
	/**
	 * @return the wallColor
	 */
	public Color getWallColor() {
		return wallColor;
	}
	/**
	 * @return Returns the illuminatedCellColor.
	 */
	public Color getIlluminatedCellColor() {
		return illuminatedCellColor;
	}
	/**
	 * @param illuminatedCellColor The illuminatedCellColor to set.
	 */
	public void setIlluminatedCellColor(Color illuminatedCellColor) {
		this.illuminatedCellColor = illuminatedCellColor;
	}
	/**
	 * @return the hideSoleNumberMode
	 */
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
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}
	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}
	/**
	 * @return Returns the lineColor.
	 */
	public Color getLineColor() {
		return lineColor;
	}
	/**
	 * @param lineColor The lineColor to set.
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}
	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}
	/**
	 * @return Returns the crossColor.
	 */
	public Color getCrossColor() {
		return crossColor;
	}
	/**
	 * @param crossColor The crossColor to set.
	 */
	public void setCrossColor(Color crossColor) {
		this.crossColor = crossColor;
	}
	/**
	 * @return Returns the gateColor.
	 */
	public Color getGateColor() {
		return gateColor;
	}
	/**
	 * @param gateColor The gateColor to set.
	 */
	public void setGateColor(Color gateColor) {
		this.gateColor = gateColor;
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
	 * @return Returns the blackAreaColor.
	 */
	public Color getBlackAreaColor() {
		return blackAreaColor;
	}
	/**
	 * @param blackAreaColor The blackAreaColor to set.
	 */
	public void setBlackAreaColor(Color blackAreaColor) {
		this.blackAreaColor = blackAreaColor;
	}
	/**
	 * @return Returns the whiteAreaColor.
	 */
	public Color getWhiteAreaColor() {
		return whiteAreaColor;
	}
	/**
	 * @param whiteAreaColor The whiteAreaColor to set.
	 */
	public void setWhiteAreaColor(Color whiteAreaColor) {
		this.whiteAreaColor = whiteAreaColor;
	}

	/**
	 * @return the selectedNumber
	 */
	public int getSelectedNumber() {
		return selectedNumber;
	}

	/**
	 * @param selectedNumber the selectedNumber to set
	 */
	public void setSelectedNumber(int selectedNumber) {
		this.selectedNumber = selectedNumber;
	}
	/**
	 * �����̑���Ɏg�p���錻�݂̕����W�����擾����BString�ŕԂ��B
	 * @return the letter
	 */
	public String getLetters() {
		return new String(letters);
	}
	/**
	 * �����̑���Ɏg�p���镶���W����ݒ肷��BString�Őݒ肷��B
	 * @param letters the letter to set
	 */
	public void setLetters(String string) {
		this.letters = string.toCharArray();
	}

}

