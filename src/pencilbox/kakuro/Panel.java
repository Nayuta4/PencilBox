package pencilbox.kakuro;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelEventHandler;


  /**
   * �u�J�b�N���v�p�l���N���X
   */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean warnWrongNumber = false;
	private boolean showAllowedNumberDot = false;

	private Color inputColor = Color.BLUE;
	private Color wallColor = new Color(192,192,192);
	private Color separationColor = Color.BLACK; // �ǃ}�X�̎ΐ�
	private Color errorColor = Color.RED;

	private Font smallFont = new Font("SansSerif", Font.PLAIN, 13);

	private Address wallPos = new Address();
	private HintDot hintDot = new HintDot();
	private KakuroCursor kcursor;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(9);
		hintDot.setDot(this, 3, getCellSize());
		setCursorOn(true);
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
	 * @param showAllowedNumberDot The showAllowedNumberDot to set.
	 */
	public void setShowAllowedNumberDot(boolean showAllowedNumberDot) {
		this.showAllowedNumberDot = showAllowedNumberDot;
	}
	/**
	 * @param warnWrongNumber The warnWrongNumber to set.
	 */
	public void setWarnWrongNumber(boolean warnWrongNumber) {
		this.warnWrongNumber = warnWrongNumber;
	}
	protected void setDisplaySize(int size) {
		super.setDisplaySize(size);
		smallFont = new Font("SansSerif", Font.PLAIN, getCellSize() / 2);
		hintDot.setDotSize(getCellSize());
	}
	public CellCursor createCursor() {
		kcursor = new KakuroCursor(this);
		return kcursor;
	}
	/**
	 * �����̓��[�h�̂��肩��
	 * @param editable
	 */
	public void setProblemEditMode(boolean editable) {
		// �����̓��[�h�ɓ������Ƃ�
		if (editable) {
			setMaxInputNumber(45);
		}else{
			setMaxInputNumber(9);
		}
		super.setProblemEditMode(editable);
	}
	
	public void drawPanel(Graphics g){
		paintBackground(g);
		drawIndex(g);
		drawGrid(g);
		drawBoard(g);
		drawBorder(g);
		if(getCellCursor()!=null) {
			drawCursor(g);
		} 
	}
	/**
	 * �Ֆʂ�`�悷��
	 * @param g
	 */
	void drawBoard(Graphics g){
		int state;
		g.setFont(smallFont);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if(board.isWall(r,c)){
					drawWall(g,r,c,board.getSumH(r,c),board.getSumV(r,c));
				}
			}
		}
		g.setFont(getNumberFont());
		g.setColor(inputColor);
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				state = board.getNumber(r,c);
				if (state > 0) {
					if (warnWrongNumber && board.isMultipleNumber(r, c))
						g.setColor(errorColor);
					else
						g.setColor(inputColor);
					placeNumber(g, r, c, state);
				} else if (showAllowedNumberDot) {
					placeNumberHint(g, r, c);
				}
			}
		}
	}
	/**
	 * �ǃ}�X�̕`��
	 * @param g
	 * @param r �s���W
	 * @param c ����W
	 * @param a �ΐ��E��̐���
	 * @param b �ΐ������̐���
	 */
	void drawWall(Graphics g, int r, int c, int a, int b){

		String numS;
		g.setColor(wallColor);
		wallPos.set(r,c);
		b2p(wallPos);
		int statusA = board.getWordStatus(r,c,Direction.HORIZ);
		int statusB = board.getWordStatus(r,c,Direction.VERT);
//		r = wallPos.r;
//		c = wallPos.c;
		if (getRotation() == 4) {  // �c�������̏ꍇ
			int t = a;
			a = b;
			b = t; 
			t = statusA;
			statusA = statusB;	
			statusB = t;
		}
		g.fillRect(toX(wallPos.c)+1, toY(wallPos.r)+1, getCellSize()-1, getCellSize()-1);
		g.setColor(separationColor);
		g.drawLine(toX(wallPos.c),toY(wallPos.r), toX(wallPos.c+1), toY(wallPos.r+1));
		FontMetrics metrics = g.getFontMetrics();
		numS = Integer.toString(b);
		if (b>0) {
			if (warnWrongNumber && statusB == -1) g.setColor(errorColor);
//			else if (statusB == 1 ) g.setColor(successColor);
			else g.setColor(separationColor);
		g.drawString(
			numS,
			(toX(wallPos.c) + (getHalfCellSize() - metrics.stringWidth(numS)) / 2 + 1),
			(toY(wallPos.r) + (getHalfCellSize() - metrics.getHeight()) / 2 + metrics.getAscent())  + getHalfCellSize());
		}
		numS = Integer.toString(a);
		if (a>0) {
			if (warnWrongNumber && statusA == -1) g.setColor(errorColor);
//			else if (statusA == 1 ) g.setColor(successColor);
			else g.setColor(separationColor);
		g.drawString(
			numS,
			(toX(wallPos.c) + (getHalfCellSize() - metrics.stringWidth(numS)) / 2  + getHalfCellSize()),
			(toY(wallPos.r) + (getHalfCellSize() - metrics.getHeight()) / 2 + metrics.getAscent()) + 1);
		}
		g.setColor(separationColor);
		if (board.isWall(r, c+1)) {
			if (getRotation() == 0)
				g.drawLine(toX(wallPos.c+1),toY(wallPos.r), toX(wallPos.c+1), toY(wallPos.r+1));
			else if (getRotation() == 4)
				g.drawLine(toX(wallPos.c),toY(wallPos.r+1), toX(wallPos.c+1), toY(wallPos.r+1));
		}
		if (board.isWall(r+1, c)) {
			if (getRotation() == 0)
				g.drawLine(toX(wallPos.c),toY(wallPos.r+1), toX(wallPos.c+1), toY(wallPos.r+1));
			else if (getRotation() == 4)
				g.drawLine(toX(wallPos.c+1),toY(wallPos.r), toX(wallPos.c+1), toY(wallPos.r+1));
		}
	}
	/**
	 * �J�b�N�������͗p�J�[�\����`��
	 */
	public void drawCursor(Graphics g) {
		if (isProblemEditMode()) {
			g.setColor(getCursorColor());
			g.drawRect(
				toX(kcursor.c())+kcursor.getStair()*getHalfCellSize(),
				toY(kcursor.r())+(kcursor.getStair()^1)*getHalfCellSize(),
				getHalfCellSize(),
				getHalfCellSize());
		} else {
		}
		super.drawCursor(g);
	}
	
	void placeNumberHint(Graphics g, int r, int c) {
		if (board.getRemNo(r,c) == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, board.getRemPattern(r,c));
		}
	}

	/*
	 * �u�J�b�N���v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos.r, pos.c)) {
				board.increaseNumber(pos.r, pos.c);
			}
		}
	}
	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos.r, pos.c)) {
				board.decreaseNumber(pos.r, pos.c);
			}
		}
	}
	/*
	 * �u�J�b�N���v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (!isTransposed()) {
				if (kcursor.getStair() == KakuroCursor.LOWER)
					board.setSumV(pos.r, pos.c, num);
				else if (kcursor.getStair() == KakuroCursor.UPPER)
					board.setSumH(pos.r, pos.c, num);
			} else {
				if (kcursor.getStair() == KakuroCursor.LOWER)
					board.setSumH(pos.r, pos.c, num);
				else if (kcursor.getStair() == KakuroCursor.UPPER)
					board.setSumV(pos.r, pos.c, num);
			}
		} else {
			if (!board.isWall(pos.r, pos.c))
				board.enterNumberA(pos.r, pos.c, num);
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.removeWall(pos.r, pos.c);
		} else {
			if (!board.isWall(pos.r, pos.c))
				board.enterNumberA(pos.r, pos.c, 0);
		}
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			if (!isTransposed()) {
				if (kcursor.getStair() == KakuroCursor.LOWER)
					board.setSumV(pos.r, pos.c, 0);
				else if (kcursor.getStair() == KakuroCursor.UPPER)
					board.setSumH(pos.r, pos.c, 0);
			} else {
				if (kcursor.getStair() == KakuroCursor.LOWER)
					board.setSumH(pos.r, pos.c, 0);
				else if (kcursor.getStair() == KakuroCursor.UPPER)
					board.setSumV(pos.r, pos.c, 0);
			}
		}
	}
}
