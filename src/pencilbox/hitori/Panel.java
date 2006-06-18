package pencilbox.hitori;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * 「ひとりにしてくれ」パネルクラス
 */
public class Panel extends PanelEventHandler {

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
		setMaxInputNumber(board.getMaxNumber()); 
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
		if (getCellCursor() != null) {
			drawCursor(g);
		}
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

	/**
	 * 色を設定する
	 * @param target 設定する色を指定するフィールド名
	 * @param color 設定する色
	 */
	public void setColor(String target, Color color) {
		if (target.equals("paintColor"))
			paintColor = color;
		else if (target.equals("circleColor"))
			circleColor = color;
	}
	/**
	 * 色を取得する
	 * @param target 取得する色を指定するフィールド名
	 * @return 取得した色
	 */
	public Color getColor(String target) {
		if (target.equals("paintColor"))
			return paintColor;
		else if (target.equals("circleColor"))
			return circleColor;
		else
			return null;
	}

	/*
	 * 「ひとりにしてくれ」マウス操作
	 * 
	 * 左プレス：未定⇔黒マス
	 * 右プレス：未定⇔白マス
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.BLACK);
	}
	protected void rightPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.WHITE);
		currentState = board.getState(pos.r(), pos.c());
	}

	protected void leftDragged(Address pos) {
		// 何もしない
	}

	protected void rightDragged(Address pos) {
		int st = board.getState(pos.r(), pos.c());
		if (st == currentState)
			return;
		board.changeStateA(pos.r(), pos.c(), currentState);
	}

//	protected void mouseMovedTo(Address pos) {
//		System.out.println(pos.toString());
//		int n = board.getNumber(pos.r, pos.c);
//		System.out.println(n);
//		if (n>0)
//			selectedNumber = n;
//	}

	/*
	 * 「ひとりにしてくれ」キー操作
	 * 問題入力モードのときのみ，数字入力を受け付ける
	 */

	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.setNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}

//	protected void starEntered(Address pos) {
//		if (problemEditMode)
//			board.setNumber(pos.r, pos.c, Board.UNDEFINED_NUMBER);
//	}

}
