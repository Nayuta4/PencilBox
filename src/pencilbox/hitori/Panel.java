package pencilbox.hitori;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ひとりにしてくれ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color paintColor = Color.blue;
	private Color circleColor = Color.magenta;
	private Color errorColor = Color.red;
	private Color singleNumberColor = new Color(0xCCCCCC);

	private boolean hideSingleMode = false;
	private boolean warnMultipleNumber = false;
	private boolean warnWrongWall = false;

//	private int selectedNumber = 0;

	private char[] letter = {
	};

	/**
	 * 
	 */
	public Panel() {
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
	 * @param warnWrongWall The warnWrongWall to set.
	 */
	public void setWarnWrongWall(boolean warnWrongWall) {
		this.warnWrongWall = warnWrongWall;
	}

	/**
	 * @param warnMultilpleNumber The warnMultilpleNumber to set.
	 */
	public void setWarnMultipleNumber(boolean warnMultilpleNumber) {
		this.warnMultipleNumber = warnMultilpleNumber;
	}

	/**
	 * 数字の代わりに使用する文字集合を設定する
	 * @param option 設定する文字集合タイプの番号
	 * @see pencilbox.hitori.Letters#getLetterSeries(int)
	 */
	public void setLetter(int option) {
		letter = Letters.getLetterSeries(option);
	}

	/**
	 * @param hideSingleMode The hideSingleMode to set.
	 */
	public void setHideSingleMode(boolean hideSingleMode) {
		this.hideSingleMode = hideSingleMode;
	}

	public void drawPanel(Graphics g) {
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
	void drawBoard(Graphics g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int state = board.getState(r, c);
				int number = board.getNumber(r, c);
				drawState(g, r, c, state);
				if (number > 0) 
					drawNumber(g, r, c, number);
				else if (number == Board.UNDECIDED_NUMBER) 
					placeBoldCircle(g, r, c);
			}
		}
	}
	private void drawState(Graphics g, int r, int c, int state) {
//		if (state != Board.BLACK) {
//			if (board.number[r][c] == selectedNumber) {
//				g.setColor(Color.CYAN);
//				paintCell(g,r,c);
//			}
//		}
		if (state == Board.BLACK) {
			g.setColor(paintColor);
			if (warnWrongWall) {
				if (board.getChain(r,c) == -1)
					g.setColor(Colors.getError());
				else // if (board.chain[r][c] == 1)
					g.setColor(paintColor);
//				else
//					g.setColor(Colors.get(board.chain[r][c]));
				if (board.isBlock(r,c))
					g.setColor(Colors.getError());
			}
			paintCell(g, r, c);
		} else if (state == Board.WHITE) {
			g.setColor(circleColor);
			placeCircle(g, r, c);
		}
	}
	private void drawNumber(Graphics g, int r, int c, int number) {
//			if (!hideSingleMode || !board.isSingle(r, c)) {
				if (hideSingleMode && board.isSingle(r, c)) {
					g.setColor(singleNumberColor);
				} else if (warnMultipleNumber
					&& !board.isBlack(r, c)
					&& board.isMultipleNumber(r, c)) {
					g.setColor(errorColor);
//				} else if (colorfulMode) {
//					g.setColor(Colors.getColor(number));
				} else {
					g.setColor(getNumberColor());
				}
				if (number <= letter.length)
					placeLetter(g, r, c, letter[number-1]);
				else
					placeNumber(g, r, c, number);
//			}
	}

}
