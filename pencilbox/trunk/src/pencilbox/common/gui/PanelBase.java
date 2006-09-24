package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.JPanel;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Size;


/**
 * ペンシルパズルフレームワークのパネルクラス
 * 個別パズルのパネルクラスのスーパークラスとなる
 * 複数のパズルで共通で利用されるメソッドを記述している
 * 各パズルで固有の操作はサブクラスで記述する
 */

public class PanelBase extends JPanel implements Printable {

	private Size size;

	protected BufferedImage backgroundImage;
    protected BufferedImage originalImage;
    protected AffineTransform backgroundImageTransform = AffineTransform.getRotateInstance(Math.toRadians(0.0));
    protected boolean useBackgroundImage = false;

	private int cellSize = 26;
	private int halfCellSize = cellSize / 2;
	private int circleSize = 18;
	private int crossSize = 8; // 片側サイズ
	private int smallCrossSize = 3; // 片側サイズ
	private int offsetx = 26;
	private int offsety = 26;

	private Color backgroundColor = Color.WHITE;
	private Color borderColor = Color.BLACK;
	private Color gridColor = Color.BLACK;
	private Color numberColor = Color.BLACK;

	private Color cursorColor = new Color(0xFF0000);
	private Color cursorColor2 = new Color(0x0000FF);

	private Font indexFont = new Font("SansSerif", Font.ITALIC, 13);
	private Font numberFont = new Font("SansSerif", Font.PLAIN, 20);

	private int displayStyle = 0;
	private boolean showIndexMode = true;
	private boolean cursorOn = false;
	private CellCursor cellCursor;

	/**
	 * true で問題入力モード，false で解答入力モード	 
	 */
	private boolean problemEditMode = false;

	/**
	 * パネル生成コンストラクタ
	 */
	public PanelBase() {
		setFocusable(true);
	}
	/**
	 * パネルの初期設定を行う
	 * Board と関連付ける
	 * @param board 盤面
	 */
	public void setup(BoardBase board) {
		size = board.getSize();
		updatePreferredSize();
		setBoard(board);
		cellCursor = createCursor();
	}
	/**
	 *  カーソルを生成する
	 * @return 生成したカーソル
	 */
	public CellCursor createCursor() {
		return new CellCursor();
	}
	/**
	 * 個別クラスのパネルに個別クラスの盤面を設定するためのメソッド
	 * 各個別クラスでオーバーライドする
	 * @param board 盤面
	 */
	protected void setBoard(BoardBase board) {
	}
	/**
	 * 背景画像設定
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		this.backgroundImage = image;
		this.originalImage = image;
		repaint();
	}
	/**
	 * 表示サイズを変更する
	 * @param cellSize マスのサイズ
	 */
	protected void setDisplaySize(int cellSize) {

		this.cellSize = cellSize;
		if (showIndexMode) {
			offsetx = cellSize;
			offsety = cellSize;
		}
		setHalfCellSize(cellSize / 2);
		circleSize = (int) (cellSize * 0.7);
		crossSize = (int) (cellSize * 0.3);
		smallCrossSize = (int) (cellSize * 0.15);
		numberFont = new Font("SansSerif", Font.PLAIN, cellSize * 4 / 5);
		indexFont = new Font("SansSerif", Font.ITALIC, cellSize / 2);
		//		if (size==14){
		//		}
		//		else if (size==20) {
		//		}
		//		else if (size==26) {
		//		}
		updatePreferredSize();
		repaint();
	}
	/**
	 * 罫線表示スタイル取得
	 * @return 現在の番号
	 */
	protected int getDisplayStyle() {
		return displayStyle;
	}
	/**
	 * 罫線表示スタイル設定
	 * @param i 設定する番号
	 */
	protected void setDisplayStyle(int i) {
		displayStyle = i;
	}
	/**
	 * 現在の盤面の状態に合わせて，setPreferredSize() を行う
	 */
	protected void updatePreferredSize() {
		setPreferredSize(getBoardRegionSize());
		revalidate();
	}
	/**
	 * Panelの盤面領域部分のサイズを取得する
	 */
	public Dimension getBoardRegionSize() {
		return new Dimension(
				offsetx * 2 + cellSize * cols() + 1,
				offsety * 2 + cellSize * rows() + 1);
	}
	/**
	 * 問題編集可能モードの設定を行う
	 * @param problemEditMode The problemEditMode to set.
	 */
	public void setProblemEditMode(boolean problemEditMode) {
		this.problemEditMode = problemEditMode;
	}
	/**
	 * @return Returns the problemEditMode.
	 */
	public boolean isProblemEditMode() {
		return problemEditMode;
	}
	/**
	 * 現在の回転状態に応じた行数を取得する
	 * @return パネル上の盤面の行数 
	 */
	public int rows() {
		return size.getRows();
	}
	/**
	 * 現在の回転状態に応じた列数を取得する
	 * @return パネル上の盤面の列数
	 */
	public int cols() {
		return size.getCols();
	}

	/*
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//		Graphics2D g2 = (Graphics2D) g;
		//		context = g2.getFontRenderContext();
		drawPanel(g);
	}
	/**
	 * パネル描画メソッドで，画面表示用と印刷用で共用する．
	 * 個々のサブクラスで実装する
	 * @param g
	 */
	public void drawPanel(Graphics g) {
	}
	/**
	 * Panel上の列座標をピクセルx座標に変換する
	 * @param c Panel上の列座標
	 * @return 変換後のピクセル座標
	 */
	public final int toX(int c) {
		return offsetx + cellSize * c;
	}
	/**
	 * Panel上の列座標をピクセルy座標に変換する
	 * @param r Panel上の行座標
	 * @return 変換後のピクセル座標
	 */
	public final int toY(int r) {
		return offsety + cellSize * r;
	}

	/*
	 * 盤面一部描画用メソッド群
	 */
	/**
	 * 盤面の背景を backgraoundColor で塗りつぶす
	 * @param g
	 */
	public void paintBackground(Graphics g) {
		if (useBackgroundImage && backgroundImage != null) {
			Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(backgroundImage, backgroundImageTransform, null);
		} else {
			g.setColor(backgroundColor);
			g.fillRect(offsetx, offsety, cellSize * cols(), cellSize * rows());
		}
	}
	/**
	 * 盤面の外枠を描く
	 * @param g
	 */
	public void drawBorder(Graphics g) {
		g.setColor(borderColor);
		g.drawRect(offsetx - 1, offsety - 1, cellSize * cols() + 2,	cellSize * rows() + 2);
		g.drawRect(offsetx, offsety, cellSize * cols(), cellSize * rows());
	}
	/**
	 * 罫線を描く
	 * @param g
	 */
	public void drawGrid(Graphics g) {
		g.setColor(gridColor);
		for (int r = 1; r < rows(); r++) {
			g.drawLine(toX(0), toY(r), toX(cols()), toY(r));
		}
		for (int c = 1; c < cols(); c++) {
			g.drawLine(toX(c), toY(0), toX(c), toY(rows()));
		}
	}
	/**
	 * 盤面の上と左の端に座標数字を描く
	 * @param g
	 */
	public void drawIndex(Graphics g) {
		int firstIndex = 1;
		g.setFont(indexFont);
		g.setColor(numberColor);
		if (showIndexMode == false)
			return;
		for (int r = 0; r < rows(); r++) {
			placeIndexNumber(g, r, -1, r + firstIndex);
		}
		for (int c = 0; c < cols(); c++) {
			placeIndexNumber(g, -1, c, c + firstIndex);
		}
	}
	/**
	 * カーソルを描く
	 * @param g
	 */
	public void drawCursor(Graphics g) {
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (cursorOn) {
			g.setColor(cursorColor2);
		} else {
			return;
		}
		g.drawRect(toX(cellCursor.c()), toY(cellCursor.r()), cellSize, cellSize);
		g.drawRect(toX(cellCursor.c()) + 1,	toY(cellCursor.r()) + 1, cellSize - 2, cellSize - 2);
		g.drawRect(toX(cellCursor.c()) + 2,	toY(cellCursor.r()) + 2, cellSize - 4, cellSize - 4);
	}
	/*
	 * 特殊図形描画のためのメソッド群
	 */
	/**
	 * 引数の座標を左または上の端点として，セルの１辺の長さと同じ長さの横または縦の線を描く
	 * @param g
	 * @param x    中心のx座標
	 * @param y    中心のy座標
	 * @param direction 縦辺 なら 縦の線， 横辺 なら 横の線 を引く
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction) {
		if (direction == Direction.HORIZ)
			g.fillRect(x, y - 1, cellSize + 1, 3);
		else if (direction == Direction.VERT)
			g.fillRect(x - 1, y, 3, cellSize + 1);
	}
	/**
	 * 引数の点を中心に，引数の大きさのバツ印を描く
	 * @param g
	 * @param x    中心のx座標
	 * @param y    中心のy座標
	 * @param halfSize 大きさ（片側）
	 */
	public void drawCross(Graphics g, int x, int y, int halfSize) {
		g.drawLine(x - halfSize, y - halfSize, x + halfSize, y + halfSize);
		g.drawLine(x - halfSize, y + halfSize, x + halfSize, y - halfSize);
	}
	/**
	 * 引数の座標を中心として，セルの１辺の長さと同じ長さの横または縦の線を描く
	 * @param g
	 * @param x
	 * @param y
	 * @param direction
	 */
	public void drawMidline(Graphics g, int x, int y, int direction) {
		int length = getHalfCellSize();
		if (direction == Direction.HORIZ)
			g.drawLine(x - length, y, x + length, y);
		else if (direction == Direction.VERT)
			g.drawLine(x, y - length, x, y + length);
	}
	/**
	 * 引数の座標を中心として，セルの１辺の長さと同じ長さで太さ3の横または縦の線を描く
	 * @param g
	 * @param x
	 * @param y
	 * @param direction
	 */
	public void drawMidline3(Graphics g, int x, int y, int direction) {
		int length = getHalfCellSize();
		if (direction == Direction.HORIZ) {
			g.drawLine(x - length, y - 1, x + length, y - 1);
			g.drawLine(x - length, y, x + length, y);
			g.drawLine(x - length, y + 1, x + length, y + 1);
		} else if (direction == Direction.VERT) {
			g.drawLine(x - 1, y - length, x - 1, y + length);
			g.drawLine(x, y - length, x, y + length);
			g.drawLine(x + 1, y - length, x + 1, y + length);
		}
	}

	/*
	 * マスの内容を描画するためのメソッド群
	 * マスの座標を与えると，そのセルの内容を描画する．
	 * 以下のメソッドが用意されている
	 * 数字を描く
	 * 塗りつぶす
	 * ○を描く
	 * ●を描く
	 * ×を描く
	 * 横線または縦線を描く
	 */
	/**
	 * マスに文字を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param letter 描く文字
	 */
	public void placeLetter(Graphics g, int r, int c, char letter) {
		FontMetrics metrics = g.getFontMetrics();
		try {
			String string = Character.toString(letter);
			g.drawString(
				string,
				(toX(c)
					+ (cellSize - 1 - metrics.stringWidth(string)) / 2
					+ 1),
				(toY(r)
					+ (cellSize - 1 - metrics.getHeight()) / 2
					+ metrics.getAscent())
					+ 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	/**
	 * マスに数字を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param number 描く数字
	 */
	public void placeNumber(Graphics g, int r, int c, int number) {
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(r)
					+ (cellSize - 1 - metrics.getHeight()) / 2
					+ metrics.getAscent())
					+ 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	/**
	 * マスに数字を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param number 描く数字
	 */
	public void placeIndexNumber(Graphics g, int r, int c, int number) {
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(r)
					+ (cellSize - 1 - metrics.getHeight()) / 2
					+ metrics.getAscent())
					+ 1);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * マスを塗りつぶす
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void paintCell(Graphics g, int r, int c) {
		g.fillRect(
			toX(c) + 1,
			toY(r) + 1,
			cellSize - 1,
			cellSize - 1);
	}

	/**
	 * マスに○印を配置する
	 * 大きさはクラスで定める標準値
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeCircle(Graphics g, int r, int c) {
		g.drawOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize,
			circleSize);
	}
	/**
	 * マスに○印を配置する
	 * 大きさを引数で指定する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param circleSize 配置する○印の直径
	 */
	public void placeCircle(Graphics g, int r, int c, int circleSize) {
		g.drawOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize,
			circleSize);
	}
	/**
	 * マスに○印を配置する
	 * 大きさはセルに内接する大きさとする
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeLargeCircle(Graphics g, int r, int c) {
		placeCircle(g, r, c, cellSize - 2);
	}
	/**
	 * マスに線幅2の○印を配置する
	 * 大きさはクラスで定める標準値
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeBoldCircle(Graphics g, int r, int c) {
		int x = toX(c) + (cellSize - circleSize) / 2;
		int y = toY(r) + (cellSize - circleSize) / 2;
		g.drawOval(x, y, circleSize, circleSize);
		g.drawOval(x + 1, y + 1, circleSize - 2, circleSize - 2);
	}
	/**
	 * マスに線幅2の○印を配置する
	 * 大きさを引数で指定する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param circleSize 配置する○印の直径
	 */
	public void placeBoldCircle(Graphics g, int r, int c, int circleSize) {
		int x = toX(c) + (cellSize - circleSize) / 2;
		int y = toY(r) + (cellSize - circleSize) / 2;
		g.drawOval(x, y, circleSize, circleSize);
		g.drawOval(x + 1, y + 1, circleSize - 2, circleSize - 2);
	}
	/**
	 * マスに塗りつぶした●印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeFilledCircle(Graphics g, int r, int c) {
		g.fillOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize + 1,
			circleSize + 1);
	}
	/**
	 * マスに内接する大きさの塗りつぶした●印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeLargeFilledCircle(Graphics g, int r, int c) {
		placeFilledCircle(g, r, c, cellSize - 2);
	}
	/**
	 * マスに塗りつぶした●印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param circleSize 配置する●印の直径
	 */
	public void placeFilledCircle(Graphics g, int r, int c, int circleSize) {
		g.fillOval(
			toX(c) + (cellSize - circleSize) / 2,
			toY(r) + (cellSize - circleSize) / 2,
			circleSize + 1,
			circleSize + 1);
	}
	/**
	 * マスに×印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeCross(Graphics g, int r, int c) {
		drawCross(
			g,
			toX(c) + getHalfCellSize(),
			toY(r) + getHalfCellSize(),
			crossSize);
	}
	/**
	 * 辺上に線を配置する
	 * @param g
	 * @param d
	 * @param r
	 * @param c
	 */
	public void placeSideLine(Graphics g, int d, int r, int c) {
		drawLineSegment(
			g,
			toX(c + (d ^ 1)),
			toY(r + d),
			d);
	}
	/**
	 * 辺と交差する線を配置する
	 * @param g
	 * @param d
	 * @param r
	 * @param c
	 */
	public void placeTraversalLine(Graphics g, int d, int r, int c) {
		drawLineSegment(
			g,
			toX(c) + getHalfCellSize(),
			toY(r) + getHalfCellSize(),
			d ^ 1);
	}
	/**
	 * 辺上に×印を配置する
	 * @param g
	 * @param d
	 * @param r
	 * @param c
	 */
	public void placeSideCross(Graphics g, int d, int r, int c) {
		if (d == Direction.VERT)
			drawCross(
				g,
				toX(c + 1),
				toY(r) + getHalfCellSize(),
				smallCrossSize);
		else if (d == Direction.HORIZ)
			drawCross(
				g,
				toX(c) + getHalfCellSize(),
				toY(r + 1),
				smallCrossSize);
	}
	/**
	 * マスの中心に横または縦の線を配置する
	 * @param g
	 * @param r
	 * @param c
	 * @param dir
	 */
	public void placeMidline(Graphics g, int r, int c, int dir) {
		drawMidline(
			g,
			toX(c) + getHalfCellSize(),
			toY(r) + getHalfCellSize(),
			dir);
	}
	/**
	 * 四角を配置する 
	 * @param g 
	 * @param r0 盤面行座標
	 * @param c0 盤面列座標
	 * @param r1 盤面行座標
	 * @param c1 盤面列座標
	 */
	public void placeSquare(Graphics g, int r0, int c0, int r1, int c1) {
		g.drawRect(
			toX((c0 < c1) ? c0 : c1) + 1,
			toY((r0 < r1) ? r0 : r1) + 1,
			cellSize * (((c0 < c1) ? c1-c0 : c0-c1) + 1) - 2,
			cellSize * (((r0 < r1) ? r1-r0 : r0-r1) + 1) - 2);
	}

	/* 
	 * 盤面印刷用メソッド
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pf, int page)
		throws PrinterException {
		if (page >= 1)
			return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(pf.getImageableX(), pf.getImageableY());
		// 表示可能領域の左上角を座標原点とする．coreJAVA v.2 p.652
		g2.scale(0.7, 0.7); // 印刷時は縮小 1pixel -> 0.7point
		//		  g2.draw(new Rectangle2D.Double(0, 0, pf.getImageableWidth(), pf.getImageableHeight()));
		drawPanel(g2);
		return Printable.PAGE_EXISTS;
	}
	/**
	 * @param cellSize The cellSize to set.
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}
	/**
	 * @return Returns the cellSize.
	 */
	public int getCellSize() {
		return cellSize;
	}
	/**
	 * @param halfCellSize The halfCellSize to set.
	 */
	public void setHalfCellSize(int halfCellSize) {
		this.halfCellSize = halfCellSize;
	}
	/**
	 * @return Returns the halfCellSize.
	 */
	public int getHalfCellSize() {
		return halfCellSize;
	}
	/**
	 * @param circleSize The circleSize to set.
	 */
	public void setCircleSize(int circleSize) {
		this.circleSize = circleSize;
	}
	/**
	 * @return Returns the circleSize.
	 */
	public int getCircleSize() {
		return circleSize;
	}
	/**
	 * @param crossSize The crossSize to set.
	 */
	public void setCrossSize(int crossSize) {
		this.crossSize = crossSize;
	}
	/**
	 * @return Returns the crossSize.
	 */
	public int getCrossSize() {
		return crossSize;
	}
	/**
	 * @param smallCrossSize The smallCrossSize to set.
	 */
	public void setSmallCrossSize(int smallCrossSize) {
		this.smallCrossSize = smallCrossSize;
	}
	/**
	 * @return Returns the smallCrossSize.
	 */
	public int getSmallCrossSize() {
		return smallCrossSize;
	}
	/**
	 * @param offsetx The offsetx to set.
	 */
	public void setOffsetx(int offsetx) {
		this.offsetx = offsetx;
	}
	/**
	 * @return Returns the offsetx.
	 */
	public int getOffsetx() {
		return offsetx;
	}
	/**
	 * @param offsety The offsety to set.
	 */
	public void setOffsety(int offsety) {
		this.offsety = offsety;
	}
	/**
	 * @return Returns the offsety.
	 */
	public int getOffsety() {
		return offsety;
	}
	/**
	 * @param backgroundColor The backgroundColor to set.
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	/**
	 * @return Returns the backgroundColor.
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	/**
	 * @param borderColor The borderColor to set.
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	/**
	 * @return Returns the borderColor.
	 */
	public Color getBorderColor() {
		return borderColor;
	}
	/**
	 * @param gridColor The gridColor to set.
	 */
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}
	/**
	 * @return Returns the gridColor.
	 */
	public Color getGridColor() {
		return gridColor;
	}
	/**
	 * @param numberColor The numberColor to set.
	 */
	public void setNumberColor(Color numberColor) {
		this.numberColor = numberColor;
	}
	/**
	 * @return Returns the numberColor.
	 */
	public Color getNumberColor() {
		return numberColor;
	}
	/**
	 * @param cursorColor The cursorColor to set.
	 */
	public void setCursorColor(Color cursorColor) {
		this.cursorColor = cursorColor;
	}
	/**
	 * @return Returns the cursorColor.
	 */
	public Color getCursorColor() {
		return cursorColor;
	}
	/**
	 * @param cursorOn The cursorOn to set.
	 */
	public void setCursorOn(boolean cursorOn) {
		this.cursorOn = cursorOn;
	}
	/**
	 * @return Returns the cursorOn.
	 */
	public boolean isCursorOn() {
		return cursorOn;
	}
	/**
	 * @param numberFont The numberFont to set.
	 */
	protected void setNumberFont(Font numberFont) {
		this.numberFont = numberFont;
	}
	/**
	 * @return Returns the numberFont.
	 */
	protected Font getNumberFont() {
		return numberFont;
	}
	/**
	 * @param cellCursor The cellCursor to set.
	 */
	protected void setCellCursor(CellCursor cellCursor) {
		this.cellCursor = cellCursor;
	}
	/**
	 * @return Returns the cellCursor.
	 */
	protected CellCursor getCellCursor() {
		return cellCursor;
	}
	/**
	 * @return the showIndex
	 */
	public boolean isShowIndexMode() {
		return showIndexMode;
	}
	/**
	 * @param showIndex the showIndex to set
	 */
	public void setShowIndexMode(boolean showIndex) {
		this.showIndexMode = showIndex;
	}

	/**
	 * @param b the showIndex to set
	 */
	public void changeShowIndexMode(boolean b) {
		this.showIndexMode = b;
		if (b == true) {
			setOffsetx(this.getCellSize());
			setOffsety(this.getCellSize());
		} else {
			setOffsetx(1);
			setOffsety(1);
		}
		updatePreferredSize();
	}
}

