package pencilbox.numberlink;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * �u�i���o�[�����N�v�p�l���N���X
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private Color lineColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color lightColor = Color.GREEN;

	private boolean warnBranchedLink = false;
	private boolean colorForEachLink = false;
	private boolean highlightSelectedLink = true;

	private Link selectedLink = null;
	private int selectedNumber = 0;  // �I������Ă��Ȃ��Ƃ��� 0

	private Color numberlessLinkColor = Color.CYAN;
	private Color selectedLinkColor = Color.GREEN;
	private Color branchedLinkColor = Color.RED;
	private Color errorColor = Color.RED;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
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
	 * @param highlightSelectedLink The highlightSelectedLink to set.
	 */
	public void setHighlightSelectedLink(boolean highlightSelectedLink) {
		this.highlightSelectedLink = highlightSelectedLink;
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
		this.drawGrid(g);
		drawBorder(g);
		if (getCellCursor() != null)
			drawCursor(g);
	}

	/**
	 * �Ֆʂ�`�悷��
	 * @param g
	 */
	void drawBoard(Graphics g) {
		int number;
		int state;
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(lineColor);
						placeTraversalLine(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				number = board.getNumber(r, c);
				if (number > 0) {
					g.setColor(getBackgroundColor());
					placeFilledCircle(g, r, c);
					g.setColor(lightColor);
					g.setColor(getNumberColor());
					placeNumber(g, r, c, number);
				} else if (number == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
	}
	public void placeTraversalLine(Graphics g, int d, int r, int c) {
		Link link = board.getLink(d,r,c);
		int linkNo = link.getNumber();
		if (warnBranchedLink && board.isBranchedLink(d,r,c)) {
			g.setColor(errorColor);
		}
		else if (highlightSelectedLink && ((linkNo > 0 && linkNo == selectedNumber)|| link == selectedLink)) {
			g.setColor(selectedLinkColor);
		}			
		else if (colorForEachLink) {
			if (linkNo == 0) {
				g.setColor(numberlessLinkColor);
			}
			else if (linkNo == -1) {
				g.setColor(branchedLinkColor);
			}
			else {
				g.setColor(Colors.getDarkColor(linkNo));
			}
		}
		super.placeTraversalLine(g, d, r, c);
	}

	public void placeNumber(Graphics g, int r, int c, int n) {
		if (highlightSelectedLink && n == selectedNumber) {
			g.setColor(selectedLinkColor);
			super.paintCell(g, r, c);
			g.setColor(getNumberColor());
		}
		else if (
			warnBranchedLink
				&& ((board.getLink(r, c) != null
					&& board.getLink(r, c).getNumber() == -1)
					|| board.countLine(r, c) > 1)) {
			g.setColor(errorColor);
//			super.paintCell(g, r, c);
//			g.setColor(numberColor);
		}
		else if (colorForEachLink) {
			g.setColor(Colors.getDarkColor(board.getNumber(r,c)));
		}
		else {
			g.setColor(getNumberColor());
		}
		super.placeNumber(g, r, c, n);
	}
	
	/**
	 * �r���̃I�v�V�������
	 */
	public void drawGrid(Graphics g) {
		g.setColor(getGridColor());
		switch (getDisplayStyle()) {
			case 0 : // �ʏ�\��
				super.drawGrid(g);
				break;
			case 1 : // �o�H�\��
				for (int r = 0; r < rows(); r++) {
					g.drawLine(
						toX(0) + getCellSize() / 2,
						toY(r) + getCellSize() / 2,
						toX(cols()) - getCellSize() / 2,
						toY(r) + getCellSize() / 2);
				}
				for (int c = 0; c < cols(); c++) {
					g.drawLine(
						toX(c) + getCellSize() / 2,
						toY(0) + getCellSize() / 2,
						toX(c) + getCellSize() / 2,
						toY(rows()) - getCellSize() / 2);
				}
				break;
			case 2 : // ��\��
				break;
		}
	}

	/**
	 * �u�i���o�[�����N�v�L�[���X�i�[
	 * 
	 * �����̓��[�h�̂Ƃ��̂ݐ������͂�����
	 */

	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), num);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
		}
	}

	/**
	 * �u�i���o�[�����N�v�p�}�E�X���X�i�[
	 * �Փ����̕ӂɑ΂��đ��������
	 * ���N���b�N�F ���m��̖���
	 * �E�N���b�N�F ���Ȃ��m��̖���
	 */
//	protected void leftClicked(int dir, Address pos) {
//		board.toggleState(dir, pos.r, pos.c, Board.LINE);
//	}
//	protected void rightClickedEdge(int dir, Address pos) {
//		board.toggleState(dir, pos.r, pos.c, Board.NOLINE);
//	}

	/**
	 * �i���o�[�����N�p�}�E�X���X�i�[
	 * �}�X����}�X�Ƀh���b�O�ɂ���������
	 * ���h���b�O�F ��������
	 * �E�h���b�O�F ��������
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
	 * �N���b�N�����}�X�̐����n�C���C�g����� �����P�x�N���b�N����ƃn�C���C�g������
	 */
	protected void leftClicked(Address pos) {

		Link link = board.getLink(pos.r(), pos.c());
		int newNumber = 0;

		if (board.isNumber(pos.r(), pos.c()))
			newNumber = board.getNumber(pos.r(), pos.c());
		else {
			if(link!=null)
				newNumber = link.getNumber();
		}

		if (newNumber == selectedNumber && selectedLink == link) {
			selectedLink = null;
			selectedNumber = 0;
		} else {
			selectedLink = link;
			selectedNumber = newNumber;
		}
	}

}
