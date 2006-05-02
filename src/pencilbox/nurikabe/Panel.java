package pencilbox.nurikabe;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * 「ぬりかべ」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
	private Color errorColor = Color.RED;

	private int currentState = Board.UNKNOWN;

	private Font countFont = new Font("SansSerif", Font.ITALIC, 13);

	private boolean colorForEachWall = false;
	private boolean showWrongWall = false;
	private boolean showShimaSize = false;

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}
	/**
	 * @param showShimaSize The showShimaSize to set.
	 */
	public void setShowShimaSize(boolean showShimaSize) {
		this.showShimaSize = showShimaSize;
	}
	/**
	 * @param showWrongWall The showWrongWall to set.
	 */
	public void setShowWrongWall(boolean showWrongWall) {
		this.showWrongWall = showWrongWall;
	}
	/**
	 * @param colorForEachWall The colorForEachWall to set.
	 */
	public void setColorForEachWall(boolean colorForEachWall) {
		this.colorForEachWall = colorForEachWall;
	}
	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}
	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}
	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}
	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}

	protected void setDisplaySize(int size) {
		super.setDisplaySize(size);
		countFont = new Font("SansSerif", Font.ITALIC, getCellSize() / 2);
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
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int st = board.getState(r, c);
				if (st == Board.WALL) {
					paintWall(g, r, c);
				} else if (st == Board.SPACE) {
					paintSpace(g, r, c);
				} else if (st > 0) {
					g.setFont(getNumberFont());
					g.setColor(getNumberColor());
					placeNumber(g, r, c, st);
				} else if (st == Board.UNDECIDED_NUMBER) {
					g.setColor(getNumberColor());
					placeBoldCircle(g, r, c);
				}
			}
		}
	}

	void paintSpace(Graphics g, int r, int c) {
		g.setFont(countFont);
		Area area = board.getArea(r,c);
		int number = area.getNumber();
		if (showShimaSize) {
			if (number == 0 || (number > 0 && area.size() < number) || number == Board.UNDECIDED_NUMBER ) {
				g.setColor(circleColor);
				placeCircle(g, r, c);
				placeNumber(g, r, c, area.size());
			} else if (
				number == Area.MULTIPLE_NUMBER	|| (number > 0 && area.size() > number)) {
				g.setColor(Color.RED);
				placeBoldCircle(g, r, c);
				placeNumber(g, r, c, area.size());
			} else {
				g.setColor(circleColor);
				placeFilledCircle(g, r, c);
			}
			return;
		}
		g.setColor(circleColor);
		placeFilledCircle(g, r, c);
	}

	void paintWall(Graphics g, int r, int c) {
		g.setColor(paintColor);
		if (colorForEachWall) {
			g.setColor(Colors.get(board.getArea(r,c).getID()));
		}
		if (showWrongWall) {
			if (board.is2x2Block(r, c) ) {
				g.setColor(errorColor);
			}
		}
		paintCell(g, r, c);
	}
	/**
	 * 「ぬりかべ」キーリスナー
	 * 
	 * 問題入力モードのときのみ数字入力を許可
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.changeState(pos.r, pos.c, num);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.changeState(pos.r, pos.c, Board.UNKNOWN);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.changeState(pos.r, pos.c, Board.UNDECIDED_NUMBER);
	}

	/**
	 * 「ぬりかべ」マウスリスナー
	 * 
	 * 左プレス：未定⇔黒マス
	 * 右プレス：未定⇔白マス
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode())
			return;
		board.toggleState(pos.r, pos.c, Board.WALL);
		if (board.isNumber(pos.r, pos.c))
			currentState = Board.UNKNOWN;
		else
			currentState = board.getState(pos.r, pos.c);
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode())
			return;
		board.toggleState(pos.r, pos.c, Board.SPACE);
		if (board.isNumber(pos.r, pos.c))
			currentState = Board.SPACE;
		else
			currentState = board.getState(pos.r, pos.c);
	}

	protected void leftDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			getCellCursor().setPosition(pos);
			if (isOn(dragStart)) {
				int number = board.getState(dragStart.r, dragStart.c);
				if (number > 0 || number == Board.UNDECIDED_NUMBER){
					board.changeState(dragStart.r, dragStart.c, Board.UNKNOWN);
					board.changeState(pos.r, pos.c, number);
				}
			}
		}
		else {
			int st = board.getState(pos.r, pos.c);
			if (st > 0 || st == Board.UNDECIDED_NUMBER)
				return;
			if (st == currentState)
				return;
			board.changeStateA(pos.r, pos.c, currentState);
		}
	}
	protected void rightDragged(Address dragStart, Address pos) {
		if (isProblemEditMode()) {
			return;
		} else {
			int st = board.getState(pos.r, pos.c);
			if (st > 0 || st == Board.UNDECIDED_NUMBER)
				return;
			if (st == currentState)
				return;
			board.changeStateA(pos.r, pos.c, currentState);
		}
	}

}
