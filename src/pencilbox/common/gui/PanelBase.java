package pencilbox.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

import javax.swing.JPanel;

import pencilbox.common.core.Address;
import pencilbox.common.core.Area;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.core.Size;


/**
 * ペンシルパズルフレームワークのパネルクラス
 * 個別パズルのパネルクラスのスーパークラスとなる
 * 複数のパズルで共通で利用されるメソッドを記述している
 * 各パズルで固有の操作はサブクラスで記述する
 */

public class PanelBase extends JPanel implements Printable {

	public static int ANSWER_INPUT_MODE = 0;
	public static int PROBLEM_INPUT_MODE = 1;
	public static int REGION_EDIT_MODE = 3;

	private Size size;

	private int cellSize = 26;
	private int circleSize = 18;
	private int smallCrossSize = 3; // 片側サイズ
	private int linkWidth = 3;

	private int offsetx = 10;
	private int offsety = 10;

	private Color backgroundColor = Color.WHITE;
	private Color boardBorderColor = Color.BLACK;
	private Color gridColor = Color.BLACK;
	private Color numberColor = Color.BLACK;
	private Color indexColor = Color.BLACK;
	private Color errorColor = Color.RED;

	private Color cursorColor = new Color(0xFF0000);
	private Color answerCursorColor = new Color(0x0000FF);

	private Font indexFont = new Font("SansSerif", Font.ITALIC, 13);
	private Font numberFont = new Font("SansSerif", Font.PLAIN, 20);

	private int gridStyle = 1;   // 0:非表示　１：表示
	private int markStyle = 1;
	private boolean indexMode = false;
	private boolean cursorMode = false;
	private CellCursor cellCursor;

	private Area copyRegion = new Area();
	private Area pasteRegion = new Area();
	private Color copyRegionColor = new Color(0xFF0000);
	private Color pasteRegionColor = new Color(0xFFAAAA);
	
	/**
	 * 編集モード	 
	 */
	private int editMode = 0;

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
	 * 表示サイズを変更する
	 * @param cellSize マスのサイズ
	 */
	public void setDisplaySize(int cellSize) {
		this.cellSize = cellSize;
		if (indexMode) {
			offsetx = cellSize;
			offsety = cellSize;
		}
		circleSize = (int) (cellSize * 0.7);
		smallCrossSize = (int) (cellSize * 0.15);
		numberFont = new Font("SansSerif", Font.PLAIN, cellSize * 4 / 5);
		indexFont = new Font("SansSerif", Font.ITALIC, cellSize / 2);
		updatePreferredSize();
		repaint();
	}
	/**
	 * 罫線表示スタイル取得
	 * @return 現在の番号
	 */
	protected int getGridStyle() {
		return gridStyle;
	}
	/**
	 * 罫線表示スタイル設定
	 * @param i 設定する番号
	 */
	protected void setGridStyle(int i) {
		gridStyle = i;
	}
	/**
	 * @return the markStyle
	 */
	public int getMarkStyle() {
		return markStyle;
	}
	/**
	 * @param markStyle the markStyle to set
	 */
	public void setMarkStyle(int markStyle) {
		this.markStyle = markStyle;
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
	 * @return Returns the problemEditMode.
	 */
	public boolean isProblemEditMode() {
		return editMode == PROBLEM_INPUT_MODE;
	}
	/**
	 * @return the editMode
	 */
	public int getEditMode() {
		return editMode;
	}
	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(int editMode) {
		this.editMode = editMode;
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
		drawPanel((Graphics2D) g);
	}
	/**
	 * パネルを描画する。
	 * 画面表示用，印刷用，画像作成用で共通に使用する。
	 * @param g
	 */
	public void drawPanel(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		paintBackground(g);
		drawBoard(g);
		if (editMode == REGION_EDIT_MODE)
			drawCopyPasteRegion(g);
		else 
			drawCursor(g);
		drawIndex(g);
	}
	
	/**
	 * パネルに盤面を描画する
	 * 個々のサブクラスで実装する。
	 * @param g
	 */
	public void drawBoard(Graphics2D g) {
	}

	/**
	 * Panel上の列座標をピクセルx座標に変換する
	 * @param c Panel上の列座標
	 * @return 変換後のピクセル座標
	 */
	public final int toX(int c) {
		return getOffsetx() + getCellSize() * c;
	}
	/**
	 * Panel上の列座標をピクセルy座標に変換する
	 * @param r Panel上の行座標
	 * @return 変換後のピクセル座標
	 */
	public final int toY(int r) {
		return getOffsety() + getCellSize() * r;
	}

	/**
	 * Panel上のx方向ピクセル座標をマス座標に変換する
	 * @param x Panel上のピクセル座標のx
	 * @return 列座標に変換した数値
	 */
	private final int toC(int x) {
		return (x + getCellSize() - getOffsetx()) / getCellSize() - 1;
	}
	/**
	 * Panel上のｙ向ピクセル座標を行方向マス座標に変換する
	 * @param y Panel上のピクセル座標のy
	 * @return マス座標に変換した数値
	 */
	private final int toR(int y) {
		return (y + getCellSize() - getOffsety()) / getCellSize() - 1;
	}

	/**
	 * 引数のピクセル座標の位置のマス座標を取得する。
	 * @param x
	 * @param y
	 * @return
	 */
	public Address pointToAddress(int x, int y) {
		int r = toR(y);
		int c = toC(x);
		return Address.address(r, c);
	}

	/**
	 * 引数のピクセル座標に最も近い辺座標を取得する。
	 *                [H, r-1, c]
	 *              ┌　─　┐
	 *                ＼　／　
	 * [V, r, c-1]  ｜　・　｜ [V, r, c] 
	 *                ／　＼　
	 *              └　─　┘
	 *                [H, r, c]
	 * @param x
	 * @param y
	 * @return
	 */
	public SideAddress pointToSideAddress(int x, int y) {
		int r = toR(y);
		int c = toC(x);
		int resx = x - getOffsetx() - getCellSize() * c;
		int resy = y - getOffsety() - getCellSize() * r;
		if (resx + resy < getCellSize()) {
			if (resx < resy)
				return SideAddress.sideAddress(Direction.VERT, r, c-1);
			else
				return SideAddress.sideAddress(Direction.HORIZ, r-1, c);
		} else {
			if (resy < resx)
				return SideAddress.sideAddress(Direction.VERT, r, c);
			else
				return SideAddress.sideAddress(Direction.HORIZ, r, c);
		}
	}

	/*
	 * 盤面一部描画用メソッド群
	 */
	/**
	 * 盤面の背景を backgraoundColor で塗りつぶす
	 * @param g
	 */
	public void paintBackground(Graphics2D g) {
		g.setColor(backgroundColor);
		g.fillRect(offsetx, offsety, cellSize * cols(), cellSize * rows());
	}
	/**
	 * 盤面の外枠を描く
	 * @param g
	 */
	public void drawBoardBorder(Graphics2D g) {
		g.setColor(boardBorderColor);
		for (int i=0; i<=1; i++)
			g.drawRect(offsetx - i, offsety - i, cellSize * cols() + i + i,	cellSize * rows() + i + i);
	}
	/**
	 * 罫線を描く
	 * @param g
	 */
	public void drawGrid(Graphics2D g) {
		if (getGridStyle() == 0)
			return;
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
	public void drawIndex(Graphics2D g) {
		int firstIndex = 1;
		g.setFont(indexFont);
		g.setColor(indexColor);
		if (isIndexMode() == false)
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
	public void drawCursor(Graphics2D g) {
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (cursorMode) {
			g.setColor(answerCursorColor);
		} else {
			return;
		}
		for (int i = 0; i <= 2; i++)
			g.drawRect(toX(cellCursor.c())+i, toY(cellCursor.r())+i, cellSize-i-i, cellSize-i-i);
	}
	/*
	 * 特殊図形描画のためのメソッド群
	 */
	/**
	 * 引数の座標を左または上の端点として，セルの１辺の長さと同じ長さの横または縦の線を描く。
	 * @param g
	 * @param x 端点のx座標
	 * @param y 端点のy座標
	 * @param direction 縦辺 なら 縦の線， 横辺 なら 横の線 を引く
	 * @param w 線幅
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction, int w) {
		if (w == 1) {
			if (direction == Direction.HORIZ)
				g.fillRect(x - w/2, y - w/2, cellSize + w, w);
			else if (direction == Direction.VERT)
				g.fillRect(x - w/2, y - w/2, w, cellSize + w);
		} else if (w > 1) { // 角１ピクセルだけ落とす
			if (direction == Direction.HORIZ)
				g.fillRect(x - (w-2)/2, y - w/2, cellSize + w-2, w);
			else if (direction == Direction.VERT)
				g.fillRect(x - w/2, y - (w-2)/2, w, cellSize + w-2);
		}
	}
	/**
	 * 引数の座標を左または上の端点として，セルの１辺の長さの横または縦の線を描く
	 * @param g
	 * @param x 中心のx座標
	 * @param y 中心のy座標
	 * @param direction 縦辺 なら 縦の線， 横辺 なら 横の線 を引く
	 */
	public void drawLineSegment(Graphics g, int x, int y, int direction) {
		drawLineSegment(g, x, y, direction, 3);
	}
	/**
	 * 引数の点を中心に，引数の大きさの四角を描く （大きさ　halfSize*2+1）
	 * @param g
	 * @param x   中心のx座標
	 * @param y   中心のy座標
	 * @param halfSize  大きさ（片側）
	 */
	public void fillSquare(Graphics g, int x, int y, int halfSize) {
		g.fillRect(x - halfSize, y - halfSize, halfSize + halfSize + 1, halfSize + halfSize + 1);
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
	 * 中心の座標と半径を与えて，円を描く
	 * @param g
	 * @param x 中心のx座標
	 * @param y 中心のy座標
	 * @param radius 半径
	 */
	public void drawCircle(Graphics g, int x, int y, int radius) {
		g.drawOval(x-radius, y-radius, radius+radius, radius+radius);
	}
	/**
	 * 中心の座標と半径を与えて，塗りつぶした円を描く。
	 * @param g
	 * @param x 中心のx座標
	 * @param y 中心のy座標
	 * @param radius 半径
	 */
	public void fillCircle(Graphics g, int x, int y, int radius) {
		g.fillOval(x-radius, y-radius, radius+radius+1, radius+radius+1);		
	}
	/**
	 * 引数の座標を中心として文字列を描く
	 * @param g
	 * @param x 中心のx座標
	 * @param y 中心のy座標
	 * @param str 描く文字列
	 */
	public void drawString(Graphics g, int x, int y, String str) {
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(
				str,
				x - metrics.stringWidth(str) / 2,
				y - metrics.getHeight() / 2 + metrics.getAscent());
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
	 * マスの中央に文字列を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param string 描く文字列
	 */
	public void placeString(Graphics2D g, int r, int c, String string) {
		drawString(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), string);
	}
	/**
	 * マスに文字を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param letter 描く文字
	 */
	public void placeLetter(Graphics2D g, int r, int c, char letter) {
		placeString(g, r, c, Character.toString(letter));
	}
	/**
	 * マスに数字を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param number 描く数字
	 */
	public void placeNumber(Graphics2D g, int r, int c, int number) {
		placeString(g, r, c, Integer.toString(number));
	}
	/**
	 * マスに数字を配置する
	 * @param g
	 * @param r 盤面上の行座標
	 * @param c 盤面上の列座標
	 * @param number 描く数字
	 */
	public void placeIndexNumber(Graphics2D g, int r, int c, int number) {
		placeString(g, r, c, Integer.toString(number));
	}
	/**
	 * マスを塗りつぶす
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void paintCell(Graphics2D g, int r, int c) {
		g.fillRect(toX(c), toY(r), getCellSize(), getCellSize());
	}

	/**
	 * マスに○印を配置する
	 * 大きさはクラスで定める標準値
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeCircle(Graphics2D g, int r, int c) {
		placeCircle(g, r, c, getCircleSize());
	}
	/**
	 * マスに○印を配置する
	 * 大きさを引数で指定する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param circleSize 配置する○印の直径
	 */
	public void placeCircle(Graphics2D g, int r, int c, int circleSize) {
		drawCircle(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(),
				circleSize / 2);
	}
	/**
	 * マスに線幅2の○印を配置する
	 * 大きさはクラスで定める標準値
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeBoldCircle(Graphics2D g, int r, int c) {
		placeBoldCircle(g, r, c, getCircleSize());
	}
	/**
	 * マスに線幅2の○印を配置する
	 * 大きさを引数で指定する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param circleSize 配置する○印の直径
	 */
	public void placeBoldCircle(Graphics2D g, int r, int c, int circleSize) {
		int x = toX(c) + getHalfCellSize();
		int y = toY(r) + getHalfCellSize();
		drawCircle(g, x, y, circleSize / 2);
		drawCircle(g, x, y, circleSize / 2 - 1);
	}
	/**
	 * マスに塗りつぶした●印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeFilledCircle(Graphics2D g, int r, int c) {
		placeFilledCircle(g, r, c, getCircleSize());
	}
	/**
	 * マスに塗りつぶした●印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 * @param circleSize 配置する●印の直径
	 */
	public void placeFilledCircle(Graphics2D g, int r, int c, int circleSize) {
		fillCircle(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(),
				circleSize / 2);
	}
	/**
	 * マスの中央に■を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeFilledSquare(Graphics2D g, int r, int c) {
		fillSquare(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), getSmallCrossSize());
	}
	/**
	 * マスに×印を配置する
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeCross(Graphics2D g, int r, int c) {
		drawCross(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(),
				getSmallCrossSize());
	}
	/**
	 * 辺上に線を配置する
	 * @param g
	 * @param d 辺座標
	 * @param r 辺座標
	 * @param c 辺座標
	 * @param w 線幅
	 */
	public void placeSideLine(Graphics2D g, int d, int r, int c, int w) {
		if (d == Direction.VERT)
			drawLineSegment(g, toX(c + 1), toY(r), d, w);
		else if (d == Direction.HORIZ)
			drawLineSegment(g, toX(c), toY(r + 1), d, w);
	}

	public void placeSideLine(Graphics2D g, int d, int r, int c) {
		placeSideLine(g, d, r, c, 3);
	}
	public void placeSideLine(Graphics2D g, SideAddress p, int w) {
		placeSideLine(g, p.d(), p.r(), p.c(), w);
	}
	/**
	 * 辺上に線を配置する。マスとそのマスから見た辺の向きで指定する。
	 * @param g
	 * @param pos マス
	 * @param dir マスのどの向きの辺か
	 */
	public void placeSideLineJ(Graphics2D g, Address pos, int dir, int w) {
		if (dir == Direction.UP)
			placeSideLine(g, Direction.HORIZ, pos.r()-1, pos.c(), w);		
		else if (dir == Direction.LT)
			placeSideLine(g, Direction.VERT, pos.r(), pos.c()-1, w);		
		else if (dir == Direction.DN)
			placeSideLine(g, Direction.HORIZ, pos.r(), pos.c(), w);		
		else if (dir == Direction.RT)
			placeSideLine(g, Direction.VERT, pos.r(), pos.c(), w);		
	}
	
	/**
	 * 辺と交差する線を配置する
	 * @param g
	 * @param d 辺座標
	 * @param r 辺座標
	 * @param c 辺座標
	 */
	public void placeLink(Graphics2D g, int d, int r, int c) {
		drawLineSegment(g, toX(c) + getHalfCellSize(), toY(r) + getHalfCellSize(), d ^ 1, getLinkWidth());
	}
	/**
	 * 辺上に×印を配置する
	 * @param g
	 * @param d 辺座標
	 * @param r 辺座標
	 * @param c 辺座標
	 */
	public void placeSideCross(Graphics2D g, int d, int r, int c) {
		if (d == Direction.VERT)
			drawCross(g, toX(c + 1), toY(r) + getHalfCellSize(), smallCrossSize);
		else if (d == Direction.HORIZ)
			drawCross(g, toX(c) + getHalfCellSize(), toY(r + 1), smallCrossSize);
	}
	/**
	 * マスの中心に横または縦の線を配置する
	 * @param g
	 * @param r
	 * @param c
	 * @param dir
	 */
	public void placeCenterLine(Graphics2D g, int r, int c, int dir) {
		if (dir == Direction.HORIZ)
			drawLineSegment(g, toX(c), toY(r) + getHalfCellSize(), dir, 1);
		else if (dir == Direction.VERT)
			drawLineSegment(g, toX(c) + getHalfCellSize(), toY(r), dir, 1);
	}
	/**
	 * 四角を配置する 
	 * @param g 
	 * @param r0 盤面行座標
	 * @param c0 盤面列座標
	 * @param r1 盤面行座標
	 * @param c1 盤面列座標
	 */
	public void placeSquare(Graphics2D g, int r0, int c0, int r1, int c1) {
		for (int i = 0; i <= 1; i++)
			g.drawRect(
				toX((c0 < c1) ? c0 : c1) + i,
				toY((r0 < r1) ? r0 : r1) + i,
				getCellSize() * ((c0 < c1) ? c1-c0+1 : c0-c1+1) - i*2,
				getCellSize() * ((r0 < r1) ? r1-r0+1 : r0-r1+1) - i*2);
	}

	/**
	 * マスの縁取り 
	 * @param g 
	 * @param r0 盤面行座標
	 * @param c0 盤面列座標
	 */
	public void edgeCell(Graphics2D g, int r0, int c0) {
		g.drawRect(toX(c0), toY(r0), getCellSize(), getCellSize());
	}

	/**
	 * マスの縁取り 
	 * @param g
	 * @param r0 盤面行座標
	 * @param c0 盤面列座標
	 * @param w　線幅
	 */
	public void edgeCell(Graphics2D g, int r0, int c0, int w) {
		for (int i = 0; i < w; i++) {
			g.drawRect(toX(c0)+i, toY(r0)+i, getCellSize()-i-i, getCellSize()-i-i);
		}
	}

	/**
	 * マスに白マス確定（など）記号を配置する 大きさはクラスで定める標準値
	 * @param g
	 * @param r 盤面行座標
	 * @param c 盤面列座標
	 */
	public void placeMark(Graphics2D g, int r, int c) {
		switch (getMarkStyle()) {
		case 1:
			placeCircle(g, r, c);
			break;
		case 2:
			placeFilledCircle(g, r, c);
			break;
		case 3:
			placeFilledSquare(g, r, c);
			break;
		case 4:
			placeCross(g, r, c);
			break;
		case 5:
			paintCell(g, r, c);
			break;
		}
	}

	/* 
	 * 盤面印刷用メソッド
	 * 今は使用しない
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics g, PageFormat pf, int page)
		throws PrinterException {
		if (page >= 1)
			return Printable.NO_SUCH_PAGE;
		Graphics2D g2 = (Graphics2D) g;
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
	 * @return Returns the halfCellSize.
	 */
	public int getHalfCellSize() {
		return cellSize / 2;
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

	public void setLinkWidth(int linkWidth) {
		this.linkWidth = linkWidth;
	}

	public int getLinkWidth() {
		return linkWidth;
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
	 * @param boardBorderColor The boardBorderColor to set.
	 */
	public void setBoardBorderColor(Color boardBorderColor) {
		this.boardBorderColor = boardBorderColor;
	}
	/**
	 * @return Returns the boardBorderColor.
	 */
	public Color getBoardBorderColor() {
		return boardBorderColor;
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
	 * @return Returns the errorColor.
	 */
	public Color getErrorColor() {
		return errorColor;
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
	 * @param cursorMode The cursorMode to set.
	 */
	public void setCursorMode(boolean cursorMode) {
		this.cursorMode = cursorMode;
	}
	/**
	 * @return Returns the cursorMode.
	 */
	public boolean isCursorMode() {
		return cursorMode;
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
	 * @return the indexMode
	 */
	public boolean isIndexMode() {
		return indexMode;
	}
	/**
	 * @param indexMode the indexMode to set
	 */
	public void setIndexMode(boolean indexMode) {
		this.indexMode = indexMode;
	}

	/**
	 * @param b the indexMode to set
	 */
	public void changeIndexMode(boolean b) {
		this.indexMode = b;
		if (b == true) {
			setOffsetx(this.getCellSize());
			setOffsety(this.getCellSize());
		} else {
			setOffsetx(10);
			setOffsety(10);
		}
		updatePreferredSize();
	}
	
	/**
	 * @return the copyRegion
	 */
	Area getCopyRegion() {
		return copyRegion;
	}
	/**
	 * @return the pasteRegion
	 */
	Area getPasteRegion() {
		return pasteRegion;
	}
	
	/**
	 * 「領域編集モード」での編集領域を表示する。
	 * @param g
	 */
	protected void drawCopyPasteRegion(Graphics2D g) {
		g.setColor(copyRegionColor);
		edgeArea(g, copyRegion);
		g.setColor(pasteRegionColor);
		edgeArea(g, pasteRegion);
	}

	/**
	 * 領域の縁取り 
	 * 領域の外周を太線で，領域内部のマス教会を細線で描画する
	 * @param g
	 * @param area 領域
	 */
	public void edgeArea(Graphics2D g, Area area) {
		for (Address pos : area) {
			for (int dir = 0; dir < 4; dir++) {
				Address neighbor = Address.nextCell(pos, dir);
				if (area.contains(neighbor)) {
					if (dir >=2)
						placeSideLineJ(g, pos, dir, 1);
				} else {
					placeSideLineJ(g, pos, dir, 3);
				}
			}
		}
	}
}

