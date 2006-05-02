package pencilbox.kurodoko;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandler;


/**
 * 「黒マスはどこだ」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean showNumberHint = false;
	private boolean warnWrongWall = true;

	private Color paintColor = Color.blue;
	private Color circleColor = Color.magenta;
	private Color successColor = new Color(0x00FF00);
	private Color errorColor = new Color(0xFF0000);
	private Color errorColor2 = new Color(0xFFFF00);

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
	 * @return Returns the warnWrongWall.
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
	 * @return Returns the showNumberHint.
	 */
	public boolean isShowNumberHint() {
		return showNumberHint;
	}
	/**
	 * @param showNumberHint The showNumberHint to set.
	 */
	public void setShowNumberHint(boolean showNumberHint) {
		this.showNumberHint = showNumberHint;
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
				int st = board.getState(r, c);
				if (st == Board.BLACK) {
					g.setColor(paintColor);
					paintCell(g, r, c);
				} else if (st == Board.WHITE) {
					g.setColor(circleColor);
					placeCircle(g, r, c);
				} else if (st > 0) {
					g.setColor(getNumberColor());
					placeLargeCircle(g, r, c);
					placeNumber(g, r, c, st);
				} else if (st == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeLargeCircle(g, r, c);
				}
			}
		}
	}
	
	public void paintCell(Graphics g, int r, int c) {
			if (isWarnWrongWall() && board.isBlock(r,c)) {
				g.setColor(errorColor);
			} else if (isWarnWrongWall() && board.getChain(r,c) < 0) {
				g.setColor(errorColor);
			} else {
				g.setColor(paintColor);	
			}
			super.paintCell(g,r,c);
	}

	public void placeNumber(Graphics g, int r, int c, int num) {
			if (!isShowNumberHint())
				super.placeNumber(g,r,c,num);
			else{
				int nSpace = board.getSumSpace(r,c);
				int nWhite = board.getSumWhite(r,c);
				if (nSpace < num) {
					g.setColor(errorColor);
					placeLargeFilledCircle(g, r, c);
					g.setColor(getNumberColor());
					placeLargeCircle(g, r, c);
					super.placeNumber(g, r, c, num);
				} else if (nSpace == num) {
					g.setColor(successColor);
					placeLargeFilledCircle(g, r, c);
					g.setColor(getNumberColor());
					placeLargeCircle(g, r, c);
					super.placeNumber(g, r, c, num);
				} else if (nWhite > num) {
					g.setColor(errorColor2);
					placeLargeFilledCircle(g, r, c);
					g.setColor(getNumberColor());
					placeLargeCircle(g, r, c);
					super.placeNumber(g, r, c, num);
				} else if (nSpace > num) {
					g.setColor(getNumberColor());
					placeLargeCircle(g, r, c);
					super.placeNumber(g, r, c, num);
				}
			}
		}

	/*
	 * 「黒マスはどこだ」マウス操作
	 * 
	 * 左プレス：未定⇔黒マス
	 * 右プレス：未定⇔白マス
	 * 右ドラッグ：はじめにボタンを押したマスときの状態にあわせる
	 */

	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos.r, pos.c, Board.BLACK);
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos.r, pos.c, Board.WHITE);
		int st = board.getState(pos.r, pos.c);
		if (st > 0 || st == Board.UNDECIDED_NUMBER)
			currentState = Board.WHITE;
		else
			currentState = board.getState(pos.r, pos.c);
	}

	protected void leftDragged(Address pos) {
		// 何もしない
	}

	protected void rightDragged(Address pos) {
		int st = board.getState(pos.r, pos.c);
		if (st >0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		board.changeStateA(pos.r, pos.c, currentState);
	}

	/*
	 * 「黒マスはどこだ」キー操作
	 * 
	 * 問題入力モードのときのみ数字入力を許可
	 */

	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.setNumber(pos.r, pos.c, num);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.changeState(pos.r, pos.c, Board.UNKNOWN);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.changeState(pos.r, pos.c, Board.UNDECIDED_NUMBER);
	}
}
