package pencilbox.fillomino;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandler;


/**
 * 「フィルオミノ」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean showAreaBorder = true;
	private boolean showAreaHint = false;

	private Color inputColor = Color.BLUE;
	private Color areaBorderColor = Color.BLUE;
	private Color errorColor = Color.RED;
	private Color successColor = new Color(0x80FFFF);

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
	 * @return Returns the showAreaBorder.
	 */
	public boolean isShowAreaBorder() {
		return showAreaBorder;
	}

	/**
	 * @param showAreaBorder The showAreaBorder to set.
	 */
	public void setShowAreaBorder(boolean showAreaBorder) {
		this.showAreaBorder = showAreaBorder;
	}

	/**
	 * @return Returns the showAreaHint.
	 */
	public boolean isShowAreaHint() {
		return showAreaHint;
	}

	/**
	 * @param showAreaHint The showAreaHint to set.
	 */
	public void setShowAreaHint(boolean showAreaHint) {
		this.showAreaHint = showAreaHint;
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
		drawNumbers(g);
		if (showAreaBorder) 
			drawBorders(g);
	}

	private void drawNumbers(Graphics g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int number = board.getNumber(r, c);
				if (number > 0) {
					if (showAreaHint) {
						int status = board.getArea(r,c).getStatus();
						if (status == -1) {
							g.setColor(errorColor);
							paintCell(g, r, c);
						} 
						else if (status == 1) {
							g.setColor(successColor); 
							paintCell(g, r, c);
						}
					}
				}
				if (number > 0) {
					if (board.isStable(r, c)) {
						g.setColor(getNumberColor());
					} else {
						g.setColor(inputColor);
					}
					placeNumber(g, r, c, board.getNumber(r, c));
				} else if (number == Board.UNKNOWN) {
					if (board.isStable(r, c)) {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					}
				}
			}
		}
	}

	private void drawBorders(Graphics g) {
		g.setColor(areaBorderColor);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				if (board.getNumber(r, c) != board.getNumber(r, c + 1)) {
					//				 && board.getNumber(r,c)>0 && board.getNumber(r,c+1)>0) {
					placeSideLine(g, Direction.VERT, r, c);
				}
			}
			if (board.getArea(r,0) != null)
				placeSideLine(g, Direction.VERT, r, -1);
			if (board.getArea(r,board.cols()-1) != null)
				placeSideLine(g, Direction.VERT, r, board.cols()-1);
		}
		for (int c = 0; c < board.cols(); c++) {
			for (int r = 0; r < board.rows() - 1; r++) {
				if (board.getNumber(r, c) != board.getNumber(r + 1, c)) {
					//				&& board.getNumber(r,c)>0 && board.getNumber(r+1,c)>0) {
					placeSideLine(g, Direction.HORIZ, r, c);
				}
			}
			if (board.getArea(0, c) != null)
				placeSideLine(g, Direction.HORIZ, -1, c);
			if (board.getArea(board.rows()-1, c) != null)
				placeSideLine(g, Direction.HORIZ, board.rows()-1, c);
		}
	}

	/*
	 * マウス操作
	 */
	private int state;
	
	protected void leftPressed(Address pos) {
			state = board.getNumber(pos.r(), pos.c());
	}
	protected void rightPressed(Address pos) {
		if (!board.isStable(pos.r(), pos.c()))
			board.enterNumberA(pos.r(), pos.c(), 0);
	}

	protected void leftClicked(Address pos) {
		if (!board.isStable(pos.r(), pos.c()))
			board.increaseNumber(pos.r(), pos.c());
	}

	protected void rightClicked(Address pos) {
		if (!board.isStable(pos.r(), pos.c()))
			board.decreaseNumber(pos.r(), pos.c());
	}

	protected void leftDragged(Address oldPos, Address newPos) {
		if (!board.isStable(newPos.r(), newPos.c()))
			board.enterNumberA(
				newPos.r(),
				newPos.c(),
	//	board.getNumber(oldPos.r, oldPos.c));
				state);
	}
	protected void rightDragged(Address oldPos, Address newPos) {
		if (!board.isStable(newPos.r(), newPos.c()))
			board.enterNumberA(
				newPos.r(),
				newPos.c(),
//				board.getNumber(oldPos.r, oldPos.c));
				0);
	}

	/*
	 *  キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos.r(), pos.c(), num);
				board.setState(pos.r(), pos.c(), Board.STABLE);
			}
		} else {
			if (num > 0) {
				if (!board.isStable(pos.r(), pos.c())) {
					board.enterNumberA(pos.r(), pos.c(), num);
				}
			}
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
		} else {
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r(), pos.c(), Board.UNKNOWN);
			board.setState(pos.r(), pos.c(), Board.STABLE);
		}
	}
}
