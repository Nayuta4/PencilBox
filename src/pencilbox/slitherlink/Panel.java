package pencilbox.slitherlink;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * �u�X���U�[�����N�v�p�l���N���X
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean warnBranchedLink = false;
	private boolean reddenWrongNumber = false;
	private boolean colorForEachLink = false;

	private Color lineColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color errorColor = Color.RED;
//	private Color successColor = Color.GREEN;
	
	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
		setMaxInputNumber(3);
	}
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}
	/**
	 * �X���U�[�����N��p�J�[�\������
	 */
	public CellCursor createCursor() {
		return new SlitherLinkCursor(this);
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
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}
	/**
	 * @param warnBranchedLink The warnBranchedLink to set.
	 */
	public void setWarnBranchedLink(boolean warnBranchedLink) {
		this.warnBranchedLink = warnBranchedLink;
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		if (getCellCursor() != null && isProblemEditMode())
			drawCursor(g);
	}
	/**
	 * �r���̕ς��Ƀ}�X�̒��S�ɓ_��ł�
	 */
	public void drawGrid(Graphics g) {
		g.setColor(getGridColor());
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				g.fillRect(	toX(c) + getHalfCellSize() - 1,
					toY(r) + getHalfCellSize() - 1,
					3,
					3);
			}
		}
	}
	/**
	 * �Ֆʂ�`�悷��
	 * @param g
	 */
	void drawBoard(Graphics g) {
		int number;
		int nline;
		g.setFont(getNumberFont());
		g.setColor(getNumberColor());
		for (int r = 0; r < board.rows() - 1; r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				number = board.getNumber(r, c);
				if (number >= 0 && number <= 4) {
					if (warnBranchedLink || reddenWrongNumber) {
						nline = board.lineAround(r,c);
						if (nline > number) g.setColor(errorColor);
//						else if ( nline < state) g.setColor(errorColor);
						else g.setColor(getNumberColor());
					}
					placeNumber2(g, r, c, number);
				} else if (number == Board.UNDECIDED_NUMBER) {
					placeCircle2(g, r, c);
				}
			}
		}
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					number = board.getState(d, r, c);
					if (number == Board.LINE) {
						g.setColor(lineColor);
						if (colorForEachLink)
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
						if (warnBranchedLink && board.isBranchedLink(d,r,c))
							g.setColor(errorColor);
						placeTraversalLine(g, d, r, c);
					} else if (number == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	/**
	 * �X���U�[�����N�p�����z�u���\�b�h
	 * �}�X�ł͂Ȃ����_�ɐ�����z�u����
	 * @param g
	 * @param r
	 * @param c
	 * @param num
	 */
	void placeNumber2(Graphics g, int r, int c, int num) {
		FontMetrics metrics = g.getFontMetrics();
		String numS = Integer.toString(num);
		g.drawString(
			numS,
			(toX(c)
				+ (getCellSize() - 1 - metrics.stringWidth(numS)) / 2
				+ 1
				+ getHalfCellSize()),
			(toY(r)
				+ (getCellSize() - 1 - metrics.getHeight()) / 2
				+ metrics.getAscent())
				+ 1
				+ getHalfCellSize());
	}
	/**
	 * �X���U�[�����N�p�����z�u���\�b�h
	 * �}�X�ł͂Ȃ����_�Ɂ���z�u����
	 * @param g
	 * @param r
	 * @param c
	 */
	protected void placeCircle2(Graphics g, int r, int c) {
		int x = toX(c) + (getCellSize() - getCircleSize()) / 2 + getHalfCellSize();
		int y = toY(r) + (getCellSize() - getCircleSize()) / 2 + getHalfCellSize();
		g.drawOval(x, y, getCircleSize(), getCircleSize());
		g.drawOval(x + 1, y + 1, getCircleSize() - 2, getCircleSize() - 2);
	}
	/**
	 * �X���U�[�����N�����͗p�J�[�\����`��
	 * �������͂ނ悤�ɕ`��
	 * @param g
	 */
	public void drawCursor(Graphics g) {
		g.setColor(getCursorColor());
		g.drawRect(
			toX(getCellCursor().c()) + getHalfCellSize(),
			toY(getCellCursor().r()) + getHalfCellSize(),
			getCellSize(),
			getCellSize());
		g.drawRect(
			toX(getCellCursor().c()) + getHalfCellSize() + 1,
			toY(getCellCursor().r()) + getHalfCellSize() + 1,
			getCellSize() - 2,
			getCellSize() - 2);
	}
	static final int LINE_COLOR = 1;
	static final int CROSS_COLOR = 2;
	/**
	 * �F��ݒ肷��
	 * @param target �ݒ肷��F���w�肷��t�B�[���h��
	 * @param c �ݒ肷��F
	 */
	public void setColorI(int target, Color c) {
		switch (target) {
		case LINE_COLOR:
			lineColor = c;
			break;
		case CROSS_COLOR:
			crossColor = c;
			break;
		}
	}
	/**
	 * �F���擾����
	 * @param target �擾����F���w�肷��t�B�[���h��
	 * @return �擾�����F
	 */
	public Color getColorI(int target) {
		switch (target) {
		case LINE_COLOR:
			return lineColor;
		case CROSS_COLOR:
			return crossColor;
		}
		return null;
	}
	/*
	 * �u�X���U�[�����N�v�}�E�X���X�i�[2
	 * �ӏ㍶�N���b�N�F������̖���
	 * �ӏ�E�N���b�N�F���Ȃ��̖���
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.NOLINE);
	}

	/*
	 * �u�X���U�[�����N�v�}�E�X���X�i�[
	 * ���_A���璸�_B�փh���b�O�����Ƃ��C
	 * A��B������s�܂��͗�ɂ���΁C
	 * ���h���b�O�F A����B�܂Ő�������
	 * �E�h���b�O�F A����B�܂Ő�������
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r() == dragEnd.r() || dragStart.c() == dragEnd.c()) {
			board.determineInlineState(dragStart, dragEnd, Board.LINE);
		}
	}
	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r() == dragEnd.r() || dragStart.c() == dragEnd.c()) {
			board.determineInlineState(dragStart, dragEnd, Board.UNKNOWN);
		}
	}
	/*
	 * �}�E�X�ł̓J�[�\���ړ����Ȃ��i�b��j
	 */
	protected void moveCursor(Address pos) {
	}
	/*
	 * �u�X���U�[�����N�v�L�[���X�i�[
	 * 
	 * �����̓��[�h�̂Ƃ��̂ݐ������� 0-4: �������� 5: ������ SPACE: ��������
	 * 
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.NONUMBER);
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}
}
