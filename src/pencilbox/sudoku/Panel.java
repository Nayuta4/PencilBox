package pencilbox.sudoku;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelEventHandler;


/**
 * 「数独」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean highlightSelectedNumber = false;
	private boolean warnWrongNumber = false;
	private boolean showAllowedNumberDot = false;

	private Color inputColor = Color.BLUE;
	private Color errorColor = Color.RED;

	private int selectedNumber = 0;
	private Color selectedNumberColor = Color.GREEN;
	private Color selectedNumberColor2 = new Color(0xFFFF80);

	private HintDot hintDot = new HintDot();
	private int unit;

	/**
	 * 
	 */
	public Panel() {
		setCursorOn(true);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
		unit = board.getUnit();
		setMaxInputNumber(board.rows());
		// ドットヒント表示可能なのは，サイズ3,4,5のみ
		if (unit>=3 && unit<=5)
			hintDot.setDot(this, unit, getCellSize());
	}

	/**
	 * @return Returns the selectedNumberColor.
	 */
	public Color getSelectedNumberColor() {
		return selectedNumberColor;
	}

	/**
	 * @param selectedNumberColor The selectedNumberColor to set.
	 */
	public void setSelectedNumberColor(Color selectedNumberColor) {
		this.selectedNumberColor = selectedNumberColor;
	}

	/**
	 * @param warnWrongNumber The warnWrongNumber to set.
	 */
	public void setWarnWrongNumber(boolean warnWrongNumber) {
		this.warnWrongNumber = warnWrongNumber;
	}

	/**
	 * @param highlightSelectedNumber The highlightSelectedNumber to set.
	 */
	public void setHighlightSelectedNumber(boolean highlightSelectedNumber) {
		this.highlightSelectedNumber = highlightSelectedNumber;
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
	 * @param showAllowedNumberDot The showAllowedNumberDot to set.
	 */
	public void setShowAllowedNumberDot(boolean showAllowedNumberDot) {
		this.showAllowedNumberDot = showAllowedNumberDot;
	}


	protected void setDisplaySize(int cellSize) {
		super.setDisplaySize(cellSize);
		hintDot.setDotSize(cellSize);
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		if (getCellCursor() != null) {
			drawCursor(g);
		}
	}
	/**
	 * 外枠を含め，罫線を引く
	 */
	public void drawGrid(Graphics g) {
		g.setColor(getBorderColor());
		for (int r = 0; r <= rows(); r++) {
			if (r % board.getUnit() == 0) {
				g.drawLine(toX(0), toY(r) - 1, toX(cols()), toY(r) - 1);
				g.drawLine(toX(0), toY(r), toX(cols()), toY(r));
				g.drawLine(toX(0), toY(r) + 1, toX(cols()), toY(r) + 1);
			} else
				g.drawLine(toX(0), toY(r), toX(cols()), toY(r));
		}
		for (int c = 0; c <= cols(); c++) {
			if (c % board.getUnit() == 0) {
				g.drawLine(toX(c) - 1, toY(0), toX(c) - 1, toY(rows()));
				g.drawLine(toX(c), toY(0), toX(c), toY(rows()));
				g.drawLine(toX(c) + 1, toY(0), toX(c) + 1, toY(rows()));
			} else
				g.drawLine(toX(c), toY(0), toX(c), toY(rows()));
		}
	}

	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g) {
		int num;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				num = board.getNumber(r, c);
				paintCell(g, r, c, num);
				if (num > 0) {
					drawNumber(g, r, c, num);
				}
				else if (num == Board.UNKNOWN) {
					if (board.isStable(r,c)) {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					} else if (showAllowedNumberDot && unit >= 3 && unit <=5)
						drawHintDot(g, r, c);
				}
			}
		}
	}
	// 選択数字と同じ行，列，ボックスを色塗り 
	void paintCell(Graphics g, int r, int c, int num) {
		if (highlightSelectedNumber && selectedNumber > 0) {
			if (selectedNumber == num) {
				g.setColor(selectedNumberColor);
				paintCell(g, r, c);
			}
			else if (board.canPlace(r, c, selectedNumber)) {
				g.setColor(selectedNumberColor2);
				paintCell(g, r, c);
			}
		}
	}
	void drawNumber(Graphics g, int r, int c, int num) {
		if (board.isStable(r, c)) {
			g.setColor(getNumberColor());
		} else if (
			warnWrongNumber && board.isMultipleNumber(r, c)) {
			g.setColor(errorColor);
		} else {
			g.setColor(inputColor);
		}
		placeNumber(g, r, c, num);
	}

	void drawHintDot(Graphics g, int r, int c) {
		int pat = board.getPattern(r, c);
		if (pat == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, pat);
		}
	}
	/*
	 * 不使用
	 */
	void drawHintDots(Graphics g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				drawHintDot(g,r,c);
			}
		}
	}

	/*
	 * 「数独」マウスリスナー
	 */
	protected void leftPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isStable(pos.r, pos.c))
				board.increaseNumber(pos.r, pos.c);
		}
		selectedNumber = board.getNumber(pos.r, pos.c);
	}
	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isStable(pos.r, pos.c))
				board.decreaseNumber(pos.r, pos.c);
		}
		selectedNumber = board.getNumber(pos.r, pos.c);
	}
	/*
	 * 「数独」キーリスナー
	 * 
	 * 問題入力モードのときはどこでも，
	 * 解答入力モードのときは，入力可能位置にのみ，
	 * 数字を入力する
	 * 
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos.r, pos.c, num);
				board.setState(pos.r, pos.c, Board.STABLE);
			}
		} else {
			if (num > 0) {
				if (!board.isStable(pos.r, pos.c)) {
					board.enterNumberA(pos.r, pos.c, num);
				}
			}
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r, pos.c, 0);
			board.setState(pos.r, pos.c, Board.UNSTABLE);
		} else {
			if (!board.isStable(pos.r, pos.c)) {
				board.enterNumberA(pos.r, pos.c, 0);
			}
		}
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos.r, pos.c, Board.UNKNOWN);
			board.setState(pos.r, pos.c, Board.STABLE);
		} 
	}
}
