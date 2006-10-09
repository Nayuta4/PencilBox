package pencilbox.kakuro;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import pencilbox.common.core.Address;
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

	private Address wallPos = new Address();
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

	public void drawPanel(Graphics g){
		paintBackground(g);
		drawIndex(g);
		drawGrid(g);
		drawBoard(g);
		drawBorder(g);
		drawCursor(g);
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g){
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
	void drawWall(Graphics g, int r, int c, int a, int b){

		String numS;
		g.setColor(wallColor);
		wallPos.set(r,c);
		int statusA = board.getWordStatus(r,c,Direction.HORIZ);
		int statusB = board.getWordStatus(r,c,Direction.VERT);
//		r = wallPos.r;
//		c = wallPos.c;
		g.fillRect(toX(wallPos.c())+1, toY(wallPos.r())+1, getCellSize()-1, getCellSize()-1);
		g.setColor(separationColor);
		g.drawLine(toX(wallPos.c()),toY(wallPos.r()), toX(wallPos.c()+1), toY(wallPos.r()+1));
		FontMetrics metrics = g.getFontMetrics();
		numS = Integer.toString(b);
		if (b>0) {
			if (isWarnWrongNumber() && statusB == -1) g.setColor(errorColor);
//			else if (statusB == 1 ) g.setColor(successColor);
			else g.setColor(separationColor);
		g.drawString(
			numS,
			(toX(wallPos.c()) + (getHalfCellSize() - metrics.stringWidth(numS)) / 2 + 1),
			(toY(wallPos.r()) + (getHalfCellSize() - metrics.getHeight()) / 2 + metrics.getAscent())  + getHalfCellSize());
		}
		numS = Integer.toString(a);
		if (a>0) {
			if (warnWrongNumber && statusA == -1) g.setColor(errorColor);
//			else if (statusA == 1 ) g.setColor(successColor);
			else g.setColor(separationColor);
		g.drawString(
			numS,
			(toX(wallPos.c()) + (getHalfCellSize() - metrics.stringWidth(numS)) / 2  + getHalfCellSize()),
			(toY(wallPos.r()) + (getHalfCellSize() - metrics.getHeight()) / 2 + metrics.getAscent()) + 1);
		}
		g.setColor(separationColor);
		if (board.isWall(r, c+1)) {
			g.drawLine(toX(wallPos.c()+1),toY(wallPos.r()), toX(wallPos.c()+1), toY(wallPos.r()+1));
		}
		if (board.isWall(r+1, c)) {
			g.drawLine(toX(wallPos.c()),toY(wallPos.r()+1), toX(wallPos.c()+1), toY(wallPos.r()+1));
		}
	}
	/**
	 * カックロ問題入力用カーソルを描く
	 */
	public void drawCursor(Graphics g) {
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
	
	void placeNumberHint(Graphics g, int r, int c) {
		if (board.getRemNo(r,c) == 0) {
			hintDot.placeHintCross(g, r, c);
		} else {
			hintDot.placeHintDot(g, r, c, board.getRemPattern(r,c));
		}
	}

}
