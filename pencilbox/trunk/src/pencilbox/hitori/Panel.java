package pencilbox.hitori;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * 「ひとりにしてくれ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
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
		setMarkStyle(1);
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
	 * 数字の代わりに使用する現在の文字集合を取得する。Stringで返す。
	 * @return the letter
	 */
	String getLetters() {
		return new String(letters);
	}

	/**
	 * 数字の代わりに使用する文字集合を設定する。Stringで設定する。
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
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int state = board.getState(r, c);
				drawState(g, r, c, state);
				int number = board.getNumber(r, c);
				if (number > 0) 
					drawNumber1(g, r, c, number);
				else if (number == Board.UNDECIDED_NUMBER) 
					placeBoldCircle(g, r, c);
			}
		}
	}

	private void drawState(Graphics2D g, int r, int c, int state) {
		if (state == Board.BLACK) {
			g.setColor(getPaintColor());
			if (isIndicateErrorMode()) {
				if (board.getChain(r,c) == -1)
					g.setColor(getErrorColor());
				if (board.isBlock(r,c))
					g.setColor(getErrorColor());
			}
			paintCell(g, r, c);
		} else if (state == Board.WHITE) {
			g.setColor(getCircleColor());
			placeMark(g, r, c);
		}
	}

	private void drawNumber1(Graphics2D g, int r, int c, int number) {
		g.setColor(getNumberColor());
		if (isHideSoleNumberMode()) {
			if (board.isSingle(r, c)) {
				g.setColor(soleNumberColor);
			}
		}
		if (isIndicateErrorMode()) {
			if (!board.isBlack(r, c) && board.isRedundantNumber(r, c)) {
				g.setColor(getErrorColor());
			}
		}
		if (number <= letters.length)
			placeLetter(g, r, c, letters[number-1]);
		else
			placeNumber(g, r, c, number);
	}

}
