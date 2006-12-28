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

	private boolean warnWrongNumber = false;
	private boolean showAllowedNumberDot = false;

	private Color inputColor = Color.BLUE;
	private Color wallColor = new Color(0xC0C0C0);
	private Color separationColor = Color.BLACK; // 壁マスの斜線
	private Color errorColor = Color.RED;

	private Font smallFont = new Font("SansSerif", Font.PLAIN, 13);

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
	 * @return the showAllowedNumberDot
	 */
	public boolean isShowAllowedNumberDot() {
		return showAllowedNumberDot;
	}
	/**
	 * @param showAllowedNumberDot The showAllowedNumberDot to set.
	 */
	public void setShowAllowedNumberDot(boolean showAllowedNumberDot) {
		this.showAllowedNumberDot = showAllowedNumberDot;
	}
	/**
	 * @return the warnWrongNumber
	 */
	public boolean isWarnWrongNumber() {
		return warnWrongNumber;
	}
	/**
	 * @param warnWrongNumber The warnWrongNumber to set.
	 */
	public void setWarnWrongNumber(boolean warnWrongNumber) {
		this.warnWrongNumber = warnWrongNumber;
	}
	
	public void setDisplaySize(int size) {
		super.setDisplaySize(size);
		smallFont = new Font("SansSerif", Font.PLAIN, getCellSize() / 2);
		hintDot.setDotSize(getCellSize());
	}
	
	public CellCursor createCursor() {
		return new KakuroCursor();
	}

	public void drawPanel(Graphics2D g){
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
	protected void drawBoard(Graphics2D g){
		int state;
		g.setFont(smallFont);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if(board.isWall(r,c)){
					drawWall(g,r,c,board.getSumH(r,c),board.getSumV(r,c));
				}
			}
		}
		g.setFont(getNumberFont());
		g.setColor(inputColor);
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				state = board.getNumber(r,c);
				if (state > 0) {
					if (isWarnWrongNumber() && board.isMultipleNumber(r, c))
						g.setColor(errorColor);
					else
						g.setColor(inputColor);
					placeNumber(g, r, c, state);
				} else if (isShowAllowedNumberDot()) {
					placeNumberHint(g, r, c);
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
		g.setColor(wallColor);
		paintCell(g, r, c);
		g.setColor(separationColor);
		g.drawLine(toX(c), toY(r), toX(c+1), toY(r+1));
		if (b>0) {
			int statusB = board.getWordStatus(r,c,Direction.VERT);
			if (isWarnWrongNumber() && statusB == -1)
				g.setColor(errorColor);
			else
				g.setColor(separationColor);
			drawString(g, toX(c) + getHalfCellSize()/2 + 1, toY(r+1) - getHalfCellSize()/2, Integer.toString(b));
		}
		if (a>0) {
			int statusA = board.getWordStatus(r,c,Direction.HORIZ);
			if (isWarnWrongNumber() && statusA == -1)
				g.setColor(errorColor);
			else
				g.setColor(separationColor);
			drawString(g, toX(c+1) - getHalfCellSize()/2, toY(r) + getHalfCellSize()/2 + 1, Integer.toString(a));
		}
//		g.setColor(separationColor);
//		if (board.isWall(r, c+1)) {
//			g.drawLine(toX(c+1), toY(r), toX(c+1), toY(r+1));
//		}
//		if (board.isWall(r+1, c)) {
//			g.drawLine(toX(c), toY(r+1), toX(c+1), toY(r+1));
//		}
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
	
	void placeNumberHint(Graphics2D g, int r, int c) {
		if (board.getRemNo(r,c) == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, board.getRemPattern(r,c));
		}
	}

}
