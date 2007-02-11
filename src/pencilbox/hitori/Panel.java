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
	private Color errorColor = Color.RED;
	private Color singleNumberColor = new Color(0xC0C0C0);

	private boolean hideSingleMode = false;
	private boolean warnMultipleNumber = false;
	private boolean warnWrongWall = false;

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
	 * @return the warnWrongWall
	 */
	public boolean isWarnWrongWall() {
		return warnWrongWall;
	}

	/**
	 * @param warnWrongWall The warnWrongWall to set.
	 */
	public void setWarnWrongWall(boolean warnWrongWall) {
		this.warnWrongWall = warnWrongWall;
	}

	/**
	 * @return the warnMultipleNumber
	 */
	public boolean isWarnMultipleNumber() {
		return warnMultipleNumber;
	}

	/**
	 * @param warnMultilpleNumber The warnMultilpleNumber to set.
	 */
	public void setWarnMultipleNumber(boolean warnMultilpleNumber) {
		this.warnMultipleNumber = warnMultilpleNumber;
	}

	/**
	 * @return the hideSingleMode
	 */
	public boolean isHideSingleMode() {
		return hideSingleMode;
	}

	/**
	 * @param hideSingleMode The hideSingleMode to set.
	 */
	public void setHideSingleMode(boolean hideSingleMode) {
		this.hideSingleMode = hideSingleMode;
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

	public void drawPanel(Graphics2D g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	protected void drawBoard(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int state = board.getState(r, c);
				int number = board.getNumber(r, c);
				drawState(g, r, c, state);
				if (number > 0) 
					drawNumber1(g, r, c, number);
				else if (number == Board.UNDECIDED_NUMBER) 
					placeBoldCircle(g, r, c);
			}
		}
	}

	private void drawState(Graphics2D g, int r, int c, int state) {
		if (state == Board.BLACK) {
			g.setColor(paintColor);
			if (isWarnWrongWall()) {
				if (board.getChain(r,c) == -1)
					g.setColor(errorColor);
				if (board.isBlock(r,c))
					g.setColor(errorColor);
			}
			paintCell(g, r, c);
		} else if (state == Board.WHITE) {
			g.setColor(circleColor);
			placeMark(g, r, c);
		}
	}

	private void drawNumber1(Graphics2D g, int r, int c, int number) {
		g.setColor(getNumberColor());
		if (isHideSingleMode()) {
			if (board.isSingle(r, c)) {
				g.setColor(singleNumberColor);
			}
		}
		if (isWarnMultipleNumber()) {
			if (!board.isBlack(r, c) && board.isMultipleNumber(r, c)) {
				g.setColor(errorColor);
			}
		}
		if (number <= letters.length)
			placeLetter(g, r, c, letters[number-1]);
		else
			placeNumber(g, r, c, number);
	}

}
