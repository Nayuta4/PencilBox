package pencilbox.hakyukoka;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelEventHandler;



/**
 * 「波及効果」パネルクラス
 */
public class Panel extends PanelEventHandler {
	
	private Board board;

	private int maxNumber = 9;  // キー入力可能数字9までとする
	
	private boolean warnWrongNumber = false;
	private boolean highlightSelectedNumber = false;

	private Color inputColor = Color.BLUE;
	private Color areaBorderColor = Color.BLACK;
	private Color errorColor = Color.RED;
	private Color noAreaColor = new Color(0xCCCCCC);

	private int selectedNumber = 0;
	private Color selectedNumberColor = new Color(0xAAFFAA);
	private Color emissionColor = new Color(0x800000);

	private boolean showAllowedNumberDot = false;
	private HintDot hintDot = new HintDot();

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setCursorOn(true);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(maxNumber);   // 暫定的
		hintDot.setDot(this, 3, getCellSize());
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
	 * @param highlightSelectedNumber The highlightSelectedNumber to set.
	 */
	public void setHighlightSelectedNumber(boolean highlightSelectedNumber) {
		this.highlightSelectedNumber = highlightSelectedNumber;
	}

	/**
	 * @param showAllowedNumberDot The showAllowedNumberDot to set.
	 */
	public void setShowAllowedNumberDot(boolean showAllowedNumberDot) {
		this.showAllowedNumberDot = showAllowedNumberDot;
	}

	/**
	 * @param warnWrongNumber The warnWrongNumber to set.
	 */
	public void setWarnWrongNumber(boolean warnWrongNumber) {
		this.warnWrongNumber = warnWrongNumber;
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
//		paintEmission(g);
		paintCells(g);
		drawEmission(g);
		drawNumbers(g);
		if(showAllowedNumberDot)
			drawDots(g);
		drawBorders(g);
	}
	
	private void drawEmission(Graphics g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				if (highlightSelectedNumber && n > 0 && n == selectedNumber) {
					for (int cc = c-n; cc <= c+n; cc++) {
						if (cc==c)
							continue;
						if (board.isOn(r,cc)) {
							g.setColor(emissionColor);
							placeMidline(g, r, cc, Direction.HORIZ);
//							g.setColor(selectedNumberColor2);
//							paintCell(g, r, cc);
						}
					}
					for (int rr = r-n; rr <= r+n; rr++) {
						if (rr==r)
							continue;
						if (board.isOn(rr,c)) {
							g.setColor(emissionColor);
							placeMidline(g, rr, c, Direction.VERT);
//							g.setColor(selectedNumberColor2);
//							paintCell(g, rr, c);
						}
					}
				}
			}
		}
	}
//	private void paintEmission(Graphics g) {
//		for (int r = 0; r < board.rows(); r++) {
//			for (int c = 0; c < board.cols(); c++) {
//				int number = board.getNumber(r, c);
//				if (highlightSelectedNumber && number > 0 && number == selectedNumber) {
//					for (int cc = c-number; cc <= c+number; cc++) {
//						if (cc==c) continue;
//						if (board.isOn(r,cc)) {
//							g.setColor(selectedNumberColor2);
//							paintCell(g, r, cc);
//						}
//					}
//					for (int rr = r-number; rr <= r+number; rr++) {
//						if (rr==r) continue;
//						if (board.isOn(rr,c)) {
//							g.setColor(selectedNumberColor2);
//							paintCell(g, rr, c);
//						}
//					}
//				}
//			}
//		}
//	}
	private void paintCells(Graphics g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r, c) == null) {
					g.setColor(noAreaColor);
					paintCell(g, r, c);
				}
				// 選択数字配置不可のマスを色塗り
//				if (highlightSelectedNumber && selectedNumber > 0) {
//					if (!board.canPlace(r, c, selectedNumber)) {
//						g.setColor(selectedNumberColor2);
//						paintCell(g, r, c);
//					}
//				}
				if (highlightSelectedNumber && selectedNumber > 0 && board.getNumber(r, c) == selectedNumber) {
					g.setColor(selectedNumberColor);
					paintCell(g, r, c);
				}
			}
		}
	}
	
	private void drawNumbers(Graphics g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				if (board.getNumber(r, c) > 0) {
					if (board.isStable(r, c)) {
						g.setColor(getNumberColor());
					} else {
						if (warnWrongNumber && board.isError(r,c))
							g.setColor(errorColor);
						else
							g.setColor(inputColor);
					}
					placeNumber(g, r, c, board.getNumber(r, c));
				}
				else if (n == Board.UNKNOWN) {
					if(board.isStable(r,c)) {
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
				if (board.getArea(r, c) != board.getArea(r, c + 1)) {
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
				if (board.getArea(r, c) != board.getArea(r + 1, c)) {
					placeSideLine(g, Direction.HORIZ, r, c);
				}
			}
			if (board.getArea(0, c) != null)
				placeSideLine(g, Direction.HORIZ, -1, c);
			if (board.getArea(board.rows()-1, c) != null)
				placeSideLine(g, Direction.HORIZ, board.rows()-1, c);
		}
	}

	private void drawDots(Graphics g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int num = board.getNumber(r, c);
				if (num == 0) {
					int pat = board.getPattern(r, c);
					if (pat == 0) {
						hintDot.placeHintCross(g, r, c);
					} else {
						hintDot.placeHintDot(g, r, c, pat);
					}
				}
			}
		}
	}

	/*
	 * マウス操作
	 */
	private Area draggingArea;

	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (draggingArea == null) {
				//	ここの if 文を有効にすれば，既存のAreaを内側から広げることができる
				//	ただし，undo と整合をどうするかが問題				
				//				if (oldArea != null)
				//					draggingArea = oldArea;
				//				else
				draggingArea = new Area();
			}
			if (oldArea != null && oldArea != draggingArea) {
				board.removeArea(oldArea);
			}
			board.setArea(pos.r(), pos.c(), draggingArea);
			draggingArea.add(pos);
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.increaseNumber(pos.r(), pos.c());
			}
			selectedNumber = board.getNumber(pos.r(), pos.c());
		}
	}
	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (oldArea != null) {
				board.removeArea(oldArea);
			}
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.decreaseNumber(pos.r(), pos.c());
			}
			selectedNumber = board.getNumber(pos.r(), pos.c());
		}
	}
	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
			getCellCursor().setPosition(pos);
			leftPressed(pos);
		}
	}
	protected void rightDragged(Address pos) {
		if (isProblemEditMode())
			rightPressed(pos);
	}
	protected void leftDragFixed(Address dragEnd) {
		if (draggingArea == null)
			return;
		board.addArea(draggingArea);
		draggingArea = null;
	}
	protected void rightDragFixed(Address dragStart, Address dragEnd) {
		//			board.removeSquare(dragStart.r, dragStart.c, dragEnd.r, dragEnd.c);
		draggingArea = null;
	}
	protected void dragFailed() {
		if (draggingArea == null)
			return;
		board.addArea(draggingArea);
		draggingArea = null;
	}
	//		protected boolean dragIneffective(Address oldPos, Address newPos) {
	//			if (newPos.isNextTo(oldPos)) return false; // 隣接マス以外のイベントは無視
	//			else return true;
	//		}
	//	}
	/*
	 * 「波及効果」キー操作
	 *  
	 * 問題入力モードのときはどこでも，
	 * 解答入力モードのときは，入力可能位置のみ，
	 * 数字を入力する
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
			board.setNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
		} else {
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), 0);
			}
		}
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
		} else {
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNKNOWN);
			board.setState(pos.r(), pos.c(), Board.STABLE);
		}
	}
}
