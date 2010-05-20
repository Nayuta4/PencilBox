package pencilbox.hitori;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * �u�ЂƂ�ɂ��Ă���v�p�l���N���X
 */
public class Panel extends PanelBase {

	private Board board;

	private Color paintColor = new Color(0x0099FF);
	private Color circleColor = new Color(0xFF9999);
	private Color soleNumberColor = new Color(0xC0C0C0);

	private boolean hideSoleNumberMode = false;
//	private boolean indicateRedundantNumberMode = false;
	private boolean indicateErrorMode = false;

	private char[] letters = {
	};

	/**
	 * 
	 */
	public Panel() {
		setMarkStyle(2);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
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
	 * @return the indicateErrorMode
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

//	/**
//	 * @return the indicateRedundantNumberMode
//	 */
//	public boolean isIndicateRedundantNumberMode() {
//		return indicateRedundantNumberMode;
//	}
//
//	/**
//	 * @param indicateRedundantNumberMode The indicateRedundantNumberMode to set.
//	 */
//	public void setIndicateRedundantNumberMode(boolean indicateRedundantNumberMode) {
//		this.indicateRedundantNumberMode = indicateRedundantNumberMode;
//	}

	/**
	 * @return the hideSoleNumberMode
	 */
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
	 * �����̑���Ɏg�p���錻�݂̕����W�����擾����BString�ŕԂ��B
	 * @return the letter
	 */
	String getLetters() {
		return new String(letters);
	}

	/**
	 * �����̑���Ɏg�p���镶���W����ݒ肷��BString�Őݒ肷��B
	 * @param letters the letter to set
	 */
	void setLetters(String string) {
		this.letters = string.toCharArray();
	}

	public void drawBoard(Graphics2D g) {
		drawCells(g);
		drawGrid(g);
		drawBoardBorder(g);
	}

	private void drawCells(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int state = board.getState(p);
			drawState(g, p, state);
			int number = board.getNumber(p);
			if (number > 0) 
				drawNumber1(g, p, number);
			else if (number == Board.UNDECIDED_NUMBER) 
				placeBoldCircle(g, p);
		}
	}

	private void drawState(Graphics2D g, Address p, int state) {
		if (state == Board.BLACK) {
			g.setColor(getPaintColor());
			if (isIndicateErrorMode()) {
				if (board.getChain(p) == -1)
					g.setColor(getErrorColor());
				if (board.isBlock(p))
					g.setColor(getErrorColor());
			}
			paintCell(g, p);
		} else if (state == Board.WHITE) {
			g.setColor(getCircleColor());
			placeMark(g, p);
		}
	}

	private void drawNumber1(Graphics2D g, Address p, int number) {
		g.setColor(getNumberColor());
		if (isHideSoleNumberMode()) {
			if (board.isSingle(p)) {
				g.setColor(soleNumberColor);
			}
		}
		if (isIndicateErrorMode()) {
			if (!board.isBlack(p) && board.isRedundantNumber(p)) {
				g.setColor(getErrorColor());
			}
		}
		if (number <= letters.length)
			placeLetter(g, p, letters[number-1]);
		else
			placeNumber(g, p, number);
	}

}
