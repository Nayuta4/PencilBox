package pencilbox.slitherlink;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「スリザーリンク」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean warnBranchedLink = false;
	private boolean colorForEachLink = false;

	private Color lineColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color errorColor = Color.RED;
	
	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
	}
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the crossColor.
	 */
	public Color getCrossColor() {
		return crossColor;
	}
	/**
	 * @param crossColor The crossColor to set.
	 */
	public void setCrossColor(Color crossColor) {
		this.crossColor = crossColor;
	}
	/**
	 * @return Returns the lineColor.
	 */
	public Color getLineColor() {
		return lineColor;
	}
	/**
	 * @param lineColor The lineColor to set.
	 */
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	/**
	 * @return the colorForEachLink
	 */
	public boolean isColorForEachLink() {
		return colorForEachLink;
	}
	/**
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}
	/**
	 * @return the warnBranchedLink
	 */
	public boolean isWarnBranchedLink() {
		return warnBranchedLink;
	}
	/**
	 * @param warnBranchedLink The warnBranchedLink to set.
	 */
	public void setWarnBranchedLink(boolean warnBranchedLink) {
		this.warnBranchedLink = warnBranchedLink;
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
	 * 罫線の変わりにマスの中心に点を打つ
	 */
	public void drawGrid(Graphics g) {
		g.setColor(getGridColor());
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				g.fillRect(	toX(c) + getHalfCellSize() - 1,
					toY(r) + getHalfCellSize() - 1,
					3,
					3);
			}
		}
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g) {
		int number;
		int nline;
		g.setFont(getNumberFont());
		g.setColor(getNumberColor());
		for (int r = 0; r < board.rows() - 1; r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				number = board.getNumber(r, c);
				if (number >= 0 && number <= 4) {
					if (isWarnBranchedLink()) {
						nline = board.lineAround(r,c);
						if (nline > number) g.setColor(errorColor);
//						else if ( nline < state) g.setColor(errorColor);
						else g.setColor(getNumberColor());
					}
					placeNumber2(g, r, c, number);
				} else if (number == Board.UNDECIDED_NUMBER) {
					placeCircle2(g, r, c);
				}
			}
		}
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					number = board.getState(d, r, c);
					if (number == Board.LINE) {
						g.setColor(lineColor);
						if (isColorForEachLink())
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
						if (warnBranchedLink && board.isBranchedLink(d,r,c))
							g.setColor(errorColor);
						placeTraversalLine(g, d, r, c);
					} else if (number == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	/**
	 * スリザーリンク用数字配置メソッド
	 * マスではなく頂点に数字を配置する
	 * @param g
	 * @param r
	 * @param c
	 * @param num
	 */
	void placeNumber2(Graphics g, int r, int c, int num) {
		FontMetrics metrics = g.getFontMetrics();
		String numS = Integer.toString(num);
		g.drawString(
			numS,
			(toX(c)
				+ (getCellSize() - 1 - metrics.stringWidth(numS)) / 2
				+ 1
				+ getHalfCellSize()),
			(toY(r)
				+ (getCellSize() - 1 - metrics.getHeight()) / 2
				+ metrics.getAscent())
				+ 1
				+ getHalfCellSize());
	}
	/**
	 * スリザーリンク用数字配置メソッド
	 * マスではなく頂点に○を配置する
	 * @param g
	 * @param r
	 * @param c
	 */
	protected void placeCircle2(Graphics g, int r, int c) {
		int x = toX(c) + (getCellSize() - getCircleSize()) / 2 + getHalfCellSize();
		int y = toY(r) + (getCellSize() - getCircleSize()) / 2 + getHalfCellSize();
		g.drawOval(x, y, getCircleSize(), getCircleSize());
		g.drawOval(x + 1, y + 1, getCircleSize() - 2, getCircleSize() - 2);
	}
	/**
	 * スリザーリンク問題入力用カーソルを描く
	 * 数字を囲むように描く
	 * @param g
	 */
	public void drawCursor(Graphics g) {
		if (isProblemEditMode()) {
			g.setColor(getCursorColor());
			g.drawRect(
				toX(getCellCursor().c()) + getHalfCellSize(),
				toY(getCellCursor().r()) + getHalfCellSize(),
				getCellSize(),
				getCellSize());
			g.drawRect(
				toX(getCellCursor().c()) + getHalfCellSize() + 1,
				toY(getCellCursor().r()) + getHalfCellSize() + 1,
				getCellSize() - 2,
				getCellSize() - 2);
		}
	}
}
