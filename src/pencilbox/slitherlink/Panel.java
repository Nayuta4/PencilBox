package pencilbox.slitherlink;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「スリザーリンク」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private boolean indicateErrorMode = false;
	private boolean separateLinkColorMode = false;

	private Color lineColor = new Color(0x000099);
	private Color crossColor = new Color(0xFF0099);
	
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
	 * @return the separateLinkColorMode
	 */
	public boolean isSeparateLinkColorMode() {
		return separateLinkColorMode;
	}
	/**
	 * @param separateLinkColorMode The separateLinkColorMode to set.
	 */
	public void setSeparateLinkColorMode(boolean separateLinkColorMode) {
		this.separateLinkColorMode = separateLinkColorMode;
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

	public void drawBoard(Graphics2D g) {
		drawNumbers(g);
		drawLinks(g);
		drawGrid(g);
		drawBoardBorder(g);
	}
	/**
	 * 罫線の変わりにマスの中心に点を打つ
	 */
	public void drawGrid(Graphics2D g) {
		if (getGridStyle() == 0)
			return;
		g.setColor(getGridColor());
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				fillSquare(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), 1);
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		int number;
		g.setFont(getNumberFont());
		g.setColor(getNumberColor());
		for (int r = 0; r < board.rows() - 1; r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				number = board.getNumber(r, c);
				if (number >= 0 && number <= 4) {
					g.setColor(getNumberColor());
					if (isIndicateErrorMode()) {
						int nline = board.lineAround(r,c);
						if (nline > number)
							g.setColor(getErrorColor());
						else if (nline < number)
							g.setColor(getErrorColor());
					}
					placeNumber2(g, r, c, number);
				} else if (number == Board.UNDECIDED_NUMBER) {
					placeCircle2(g, r, c);
				}
			}
		}
	}
	
	private void drawLinks(Graphics2D g) {
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getState(p);
			if (state == Board.LINE) {
				g.setColor(getLineColor());
				if (isSeparateLinkColorMode())
					g.setColor(Colors.getColor(board.getLink(p).getId()));
				placeLink(g, p);
			} else if (state == Board.NOLINE) {
				g.setColor(getCrossColor());
				placeSideCross(g, p);
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
	protected void placeNumber2(Graphics2D g, int r, int c, int num) {
		drawString(g, toX(c+1), toY(r+1), Integer.toString(num));
	}
	/**
	 * スリザーリンク用数字配置メソッド
	 * マスではなく頂点に○を配置する
	 * @param g
	 * @param r
	 * @param c
	 */
	protected void placeCircle2(Graphics2D g, int r, int c) {
		int x = toX(c) + getCellSize();
		int y = toY(r) + getCellSize();
		drawCircle(g, x, y, getCircleSize()/2);
		drawCircle(g, x, y, getCircleSize()/2 - 1);
	}
	/**
	 * スリザーリンク問題入力用カーソルを描く
	 * 数字を囲むように描く
	 * @param g
	 */
	public void drawCursor(Graphics2D g) {
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
