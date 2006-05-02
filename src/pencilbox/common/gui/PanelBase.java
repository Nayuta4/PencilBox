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

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Rotation;
import pencilbox.common.core.SideAddress;
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

	private Rotation rotator = new Rotation();
	private int displayStyle = 0;
	private boolean cursorOn = false;
	private CellCursor cellCursor;

	/**
	 * true で問題入力モード，false で解答入力モード	 
	 */
	private boolean problemEditMode = false;

	private Address pos0 = new Address();
	private Address pos1 = new Address();
	private SideAddress sidePos = new SideAddress();

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
		this.size = board.getSize();
		rotator.setSize(size);
		updatePreferredSize();
		setBoard(board);
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
	 * Panel表示の回転状態を取得する
	 * @return 現在の回転状態を表す値
	 */
	protected int getRotation() {
		return rotator.getRotation();
	}
	/**
	 * Panel表示の回転状態を設定する
	 * @param rotation 新しく回転状態に設定する値
	 */
	protected void setRotation(int rotation) {
		rotator.setRotation(rotation);
		updatePreferredSize();
		repaint();
	}
	/**
	 * 表示サイズを変更する
	 * @param cellSize マスのサイズ
	 */
	protected void setDisplaySize(int cellSize) {

		this.cellSize = cellSize;
		offsetx = cellSize;
		offsety = cellSize;
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
		setPreferredSize(
			new Dimension(
				offsetx * 2 + cellSize * cols(),
				offsety * 2 + cellSize * rows()));
		revalidate();
	}
	/**
	 * 問題編集可能モードの設定を行う
	 * 都合により，問題編集モードでは回転を標準に戻す
	 * （スリザーリンクと天体ショーの問題入力が回転状態に対応していないため）
	 * フレームの大きさを帰られないのは難点
	 * @param problemEditMode The problemEditMode to set.
	 */
	public void setProblemEditMode(boolean problemEditMode) {
		this.problemEditMode = problemEditMode;
		if (problemEditMode == true) {
			setRotation(0);
//			switch (getRotation()) {
//			case 2:
//			case 5:
//			case 7:
//				setRotation(0);
//				break;
//			case 1:
//			case 3:
//			case 6:
//				setRotation(4);
//				break;
//			}
		}
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
		return isTransposed() ? size.getCols() : size.getRows();
	}
	/**
	 * 現在の回転状態に応じた列数を取得する
	 * @return パネル上の盤面の列数
	 */
	public int cols() {
		return isTransposed() ? size.getRows() : size.getCols();
	}
	/**
	 * パネル上か
	 * @param r パネル上の整数値行座標
	 * @param c パネル上の整数値列座標
	 * @return 引数に与えられた座標が盤上なら true 盤外なら false
	 */
	protected boolean isOn(int r, int c) {
		return r >= 0 && r < rows() && c >= 0 && c < cols();
	}
	protected boolean isOn(Address address) {
		return isOn(address.r, address.c);
	}
	protected boolean isSideOn(SideAddress address) {
		if (address.d == Direction.VERT)
			return address.r >= 0
				&& address.r < rows()
				&& address.c >= 0
				&& address.c < cols() - 1;
		else if (address.d == Direction.HORIZ)
			return address.r >= 0
				&& address.r < rows() - 1
				&& address.c >= 0
				&& address.c < cols();
		return false;
	}
	/**
	 * パネル上か
	 * @param r
	 * @param c
	 * @param adjustRow 行数修正
	 * @param adjustCol 列数修正
	 * @return パネル上なら true
	 */
	protected boolean isOn(int r, int c, int adjustRow, int adjustCol) {
		return r >= 0
			&& r < rows() + adjustRow
			&& c >= 0
			&& c < cols() + adjustCol;
	}
	protected boolean isOn(Address address, int adjustRow, int adjustCol) {
		return isOn(address.r, address.c, adjustRow, adjustCol);
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
	 * Rotationクラスに委託しているメソッド群
	 */
	/**
	 * パネルの縦横がもとの縦横に対して転置されているかどうか
	 * @return 転置されていれば true
	 */
	public boolean isTransposed() {
		return rotator.isTransposed();
	}
	/**
	 * パネル上の整数値座標を盤上の座標に変換する
	 * @param pos
	 */
	public void p2b(Address pos) {
		rotator.p2b(pos);
	}
	/**
	 * パネル上の整数値座標を現在の回転表示状態に応じた盤面の座標に変換する
	 * その際に，盤面サイズは rows()+adjustRow, cols()+adjustCol であるとみなす
	 * @param pos 座標
	 * @param adjustRow 盤面行サイズに対する補正値
	 * @param adjustCol 盤面列サイズに対する補正値
	 */
	public void p2b(Address pos, int adjustRow, int adjustCol) {
		rotator.p2b(pos, adjustRow, adjustCol);
	}
	/**
	 * パネル上の整数値辺座標を盤上の辺座標に変換する
	 * @param pos
	 */
	public void p2bSide(SideAddress pos) {
		rotator.p2bSide(pos);
	}
	/**
	 * 盤面上の整数値座標を現在の回転表示状態に応じたパネル上の座標に変換する
	 * その際に，盤面サイズは rows()+adjustRow, cols()+adjustCol であるとみなす
	 * @param pos 座標
	 * @param adjustRow 盤面行サイズに対する補正値
	 * @param adjustCol 盤面列サイズに対する補正値
	 */
	public void b2p(Address pos, int adjustRow, int adjustCol) {
		rotator.b2p(pos, adjustRow, adjustCol);
	}
	/**
	 * 盤上の座標をパネル上の整数値座標に変換する
	 * @param pos
	 */
	public void b2p(Address pos) {
		rotator.b2p(pos);
	}
	/**
	 * 盤上の辺座標をパネル上の整数値辺座標に変換する
	 * @param pos
	 */
	public void b2pSide(SideAddress pos) {
		rotator.b2pSide(pos);
	}
	/**
	 * 盤上の方向をパネル上の方向に変換する
	 * @param direction 変換元の方向を表す数値
	 * @return 変換後の方向を表す数値
	 */
	public int rotateDirection(int direction) {
		return rotator.rotateDirection(direction);
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
		int offset = 1;
		g.setFont(indexFont);
		g.setColor(numberColor);
		for (int r = 0; r < rows(); r++) {
			placeIndexNumber(g, r, -1, r + offset);
		}
		for (int c = 0; c < cols(); c++) {
			placeIndexNumber(g, -1, c, c + offset);
		}
	}
	/**
	 * カーソルを描く
	 * @param g
	 */
	public void drawCursor(Graphics g) {
		if (!isProblemEditMode() && !cursorOn)
			return;
		if (isProblemEditMode()) {
			g.setColor(cursorColor);
		} else if (cursorOn) {
			g.setColor(cursorColor2);
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
	 * マスの座標を与えると，必要に応じて回転して，そのセルの内容を描画する．
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
		pos0.set(r, c);
		b2p(pos0);
		FontMetrics metrics = g.getFontMetrics();
		try {
			String string = Character.toString(letter);
			g.drawString(
				string,
				(toX(pos0.c)
					+ (cellSize - 1 - metrics.stringWidth(string)) / 2
					+ 1),
				(toY(pos0.r)
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
		pos0.set(r, c);
		b2p(pos0);
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(pos0.c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(pos0.r)
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
		pos0.set(r, c);
//		b2p(pos0);
		FontMetrics metrics = g.getFontMetrics();
		try {
			String numS = Integer.toString(number);
			g.drawString(
				numS,
				(toX(pos0.c)
					+ (cellSize - 1 - metrics.stringWidth(numS)) / 2
					+ 1),
				(toY(pos0.r)
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
		pos0.set(r, c);
		b2p(pos0);
		g.fillRect(
			toX(pos0.c) + 1,
			toY(pos0.r) + 1,
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
		pos0.set(r, c);
		b2p(pos0);
		g.drawOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		g.drawOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		int x = toX(pos0.c) + (cellSize - circleSize) / 2;
		int y = toY(pos0.r) + (cellSize - circleSize) / 2;
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
		pos0.set(r, c);
		b2p(pos0);
		int x = toX(pos0.c) + (cellSize - circleSize) / 2;
		int y = toY(pos0.r) + (cellSize - circleSize) / 2;
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
		pos0.set(r, c);
		b2p(pos0);
		g.fillOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		g.fillOval(
			toX(pos0.c) + (cellSize - circleSize) / 2,
			toY(pos0.r) + (cellSize - circleSize) / 2,
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
		pos0.set(r, c);
		b2p(pos0);
		drawCross(
			g,
			toX(pos0.c) + getHalfCellSize(),
			toY(pos0.r) + getHalfCellSize(),
			crossSize);
	}
	/**
	 * 辺上に線を配置する
	 * @param g
	 * @param dir
	 * @param r
	 * @param c
	 */
	public void placeSideLine(Graphics g, int dir, int r, int c) {
		sidePos.set(dir, r, c);
		b2pSide(sidePos);
		drawLineSegment(
			g,
			toX(sidePos.c + (sidePos.d ^ 1)),
			toY(sidePos.r + sidePos.d),
			sidePos.d);
	}
	/**
	 * 辺と交差する線を配置する
	 * @param g
	 * @param dir
	 * @param r
	 * @param c
	 */
	public void placeTraversalLine(Graphics g, int dir, int r, int c) {
		sidePos.set(dir, r, c);
		b2pSide(sidePos);
		drawLineSegment(
			g,
			toX(sidePos.c) + getHalfCellSize(),
			toY(sidePos.r) + getHalfCellSize(),
			sidePos.d ^ 1);
	}
	/**
	 * 辺上に×印を配置する
	 * @param g
	 * @param dir
	 * @param r
	 * @param c
	 */
	public void placeSideCross(Graphics g, int dir, int r, int c) {
		sidePos.set(dir, r, c);
		b2pSide(sidePos);
		if (sidePos.d == Direction.VERT)
			drawCross(
				g,
				toX(sidePos.c + 1),
				toY(sidePos.r) + getHalfCellSize(),
				smallCrossSize);
		else if (sidePos.d == Direction.HORIZ)
			drawCross(
				g,
				toX(sidePos.c) + getHalfCellSize(),
				toY(sidePos.r + 1),
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
		pos0.set(r, c);
		b2p(pos0);
		int direction = (dir == Direction.HORIZ ^ isTransposed()) ? Direction.HORIZ : Direction.VERT;
		drawMidline(
			g,
			toX(pos0.c) + getHalfCellSize(),
			toY(pos0.r) + getHalfCellSize(),
			direction);
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
		pos0.set(r0, c0);
		pos1.set(r1, c1);
		b2p(pos0);
		b2p(pos1);
		g.drawRect(
			toX((pos0.c < pos1.c) ? pos0.c : pos1.c) + 1,
			toY((pos0.r < pos1.r) ? pos0.r : pos1.r) + 1,
			cellSize
				* (((pos0.c < pos1.c) ? pos1.c - pos0.c : pos0.c - pos1.c) + 1)
				- 2,
			cellSize
				* (((pos0.r < pos1.r) ? pos1.r - pos0.r : pos0.r - pos1.r) + 1)
				- 2);
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
//		repaint();
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
}

