package pencilbox.kakuro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelBase;


  /**
   * 「カックロ」パネルクラス
   */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;
	private boolean dotHintMode = false;

	private Color inputColor = new Color(0x000099);
	private Color wallColor = new Color(0xC0C0C0);

	private Font smallFont = new Font("SansSerif", Font.PLAIN, 13);

	private HintDot hintDot = new HintDot();

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setCursorMode(true);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
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
	 * @param wallColor the wallColor to set
	 */
	public void setWallColor(Color wallColor) {
		this.wallColor = wallColor;
	}

	/**
	 * @return the wallColor
	 */
	public Color getWallColor() {
		return wallColor;
	}

	/**
	 * @return the dotHintMode
	 */
	public boolean isDotHintMode() {
		return dotHintMode;
	}
	/**
	 * @param dotHintMode The dotHintMode to set.
	 */
	public void setDotHintMode(boolean dotHintMode) {
		this.dotHintMode = dotHintMode;
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
	
	public void setDisplaySize(int size) {
		super.setDisplaySize(size);
		smallFont = new Font("SansSerif", Font.PLAIN, getCellSize() / 2);
		hintDot.setDotSize(getCellSize());
	}
	
	public CellCursor createCursor() {
		return new KakuroCursor();
	}

	public void drawBoard(Graphics2D g){
		drawWalls(g);
		drawNumbers(g);
		drawGrid(g);
		drawBoardBorder(g);
	}

	private void drawWalls(Graphics2D g) {
		g.setFont(smallFont);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isWall(r, c)) {
					drawWall(g, r, c, board.getSumH(r, c), board.getSumV(r, c));
				}
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		int number;
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				number = board.getNumber(r, c);
				if (number > 0) {
					g.setColor(getInputColor());
					if (isIndicateErrorMode()) {
						if (board.isMultipleNumber(r, c))
							g.setColor(getErrorColor());
					}
					placeNumber(g, r, c, number);
				} else if (number == 0) {
					if (isDotHintMode()) {
						placeHintDot(g, r, c);
					}
				}
			}
		}
	}
	/**
	 * 壁マスの描画
	 * @param g
	 * @param r 行座標
	 * @param c 列座標
	 * @param a 斜線右上の数字
	 * @param b 斜線左下の数字
	 */
	void drawWall(Graphics2D g, int r, int c, int a, int b){
		g.setColor(getWallColor());
		paintCell(g, r, c);
		g.setColor(getGridColor());
		edgeCell(g, r, c);
		g.drawLine(toX(c), toY(r), toX(c+1), toY(r+1));
		if (b>0) {
			g.setColor(getNumberColor());
			if (isIndicateErrorMode()) {
				if (board.getWordStatus(r,c,Direction.VERT) == -1) 
					g.setColor(getErrorColor());
			}
			drawString(g, toX(c) + getHalfCellSize()/2 + 1, toY(r+1) - getHalfCellSize()/2, Integer.toString(b));
		}
		if (a>0) {
			g.setColor(getNumberColor());
			if (isIndicateErrorMode()) {
				if (board.getWordStatus(r,c,Direction.HORIZ) == -1)
					g.setColor(getErrorColor());
			}
			drawString(g, toX(c+1) - getHalfCellSize()/2, toY(r) + getHalfCellSize()/2 + 1, Integer.toString(a));
		}
	}
	/**
	 * カックロ問題入力用カーソルを描く
	 */
	public void drawCursor(Graphics2D g) {
		super.drawCursor(g);
		if (isProblemEditMode()) {
			KakuroCursor kcursor = (KakuroCursor) getCellCursor();
			g.drawRect(
				toX(kcursor.c())+kcursor.getStair()*getHalfCellSize(),
				toY(kcursor.r())+(kcursor.getStair()^1)*getHalfCellSize(),
				getHalfCellSize(),
				getHalfCellSize());
		}
	}
	
	void placeHintDot(Graphics2D g, int r, int c) {
		int pattern = board.getPattern(r, c);
		if (pattern == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, pattern);
		}
	}

}
