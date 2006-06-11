package pencilbox.tentaisho;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandler;


/**
 * 「天体ショー」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean showStar = true;
	private boolean showAreaBorder = true;
	private boolean showAreaHint = false;

	private Color areaBorderColor = Color.BLUE;
	private Color noStarAreaColor = new Color(0xFFFF80);
	private Color whiteAreaColor = new Color(0x80FFFF);
	private Color blackAreaColor = new Color(0xFF80FF);
	private Color starColor = Color.BLACK;
	private Color errorColor = Color.RED;

	private int halfStarSize = 4; // 星の半径

	private Area draggingArea;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setMaxInputNumber(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the areaBorderColor.
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
	}

	/**
	 * @param areaBorderColor The areaBorderColor to set.
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return Returns the blackAreaColor.
	 */
	public Color getBlackAreaColor() {
		return blackAreaColor;
	}

	/**
	 * @param blackAreaColor The blackAreaColor to set.
	 */
	public void setBlackAreaColor(Color blackAreaColor) {
		this.blackAreaColor = blackAreaColor;
	}

	/**
	 * @return Returns the whiteAreaColor.
	 */
	public Color getWhiteAreaColor() {
		return whiteAreaColor;
	}

	/**
	 * @param whiteAreaColor The whiteAreaColor to set.
	 */
	public void setWhiteAreaColor(Color whiteAreaColor) {
		this.whiteAreaColor = whiteAreaColor;
	}

	/**
	 * @param showAreaBorder The showAreaBorder to set.
	 */
	public void setShowAreaBorder(boolean showAreaBorder) {
		this.showAreaBorder = showAreaBorder;
	}

	/**
	 * @param showStar The showStar to set.
	 */
	public void setShowStar(boolean showStar) {
		this.showStar = showStar;
	}

	/**
	 * @param useDifferentColor The useDifferentColor to set.
	 */
	public void setShowAreaHint(boolean useDifferentColor) {
		this.showAreaHint = useDifferentColor;
	}

	public CellCursor createCursor() {
		return new TentaishoCursor(this);
	}
//	/**
//	 * 天体ショー専用カーソル on/off を切り替える
//	 * @param on カーソル on かどうか
//	 */
//	public void setCursorOn(boolean on) {
//		super.setCursorOn(on);
//	}
	protected void setDisplaySize(int size) {
		halfStarSize = size / 7 + 1;
		super.setDisplaySize(size);
	}
	
	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		if (getCellCursor() != null && isProblemEditMode()) {
			drawCursor(g);
		}
	}
	/**
	 * 盤面を描画する
	 * @param g
	 */
	void drawBoard(Graphics g) {
		paintAreas(g);
		if (showStar) {
			drawStars(g);
		}
		if (showAreaBorder) {
			drawAreaBorders(g);
		}
	}

	private void paintAreas(Graphics g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.isCovered(r, c)) {
					if (board.getArea(r,c) == draggingArea)
						continue;
					int starType = board.getArea(r,c).getStarType();
					if (showAreaHint) {
						if (starType == -1) {
							g.setColor(errorColor);
						}
						else if (starType == Board.WHITESTAR) {
							g.setColor(whiteAreaColor);
						}
						else if (starType == Board.BLACKSTAR) {
							g.setColor(blackAreaColor);
						}
						else {
							g.setColor(noStarAreaColor);
						} 
					}
					else {
						g.setColor(whiteAreaColor);
					}
					paintCell(g, r, c);
				}
			}
		}
	}
	private void drawStars(Graphics g) {
		g.setColor(starColor);
		for (int r = 0; r < board.rows() * 2 - 1; r++) {
			for (int c = 0; c < board.cols() * 2 - 1; c++) {
				if (board.hasStar(r, c))
					placeStar(g, r, c, board.getStar(r, c));
			}
		}
	}
	private void drawAreaBorders(Graphics g) {
		g.setColor(areaBorderColor);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				if (board.getArea(r, c) != board.getArea(r, c + 1)) {
					placeSideLine(g, Direction.VERT, r, c);
				}
			}
		}
		for (int r = 0; r < board.rows() - 1; r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r, c) != board.getArea(r + 1, c)) {
					placeSideLine(g, Direction.HORIZ, r, c);
				}
			}
		}
	}

	/**
	 * 星を配置する
	 * @param g
	 * @param r
	 * @param c
	 * @param star
	 */
	public void placeStar(Graphics g, int r, int c, int star) {
		if (star == Board.WHITESTAR)
			g.drawOval(
				getOffsetx() + getHalfCellSize() * (c + 1) - halfStarSize,
				getOffsety() + getHalfCellSize() * (r + 1) - halfStarSize,
				halfStarSize * 2,
				halfStarSize * 2);
		else if (star == Board.BLACKSTAR)
			g.fillOval(
				getOffsetx() + getHalfCellSize() * (c + 1) - halfStarSize,
				getOffsety() + getHalfCellSize() * (r + 1) - halfStarSize,
				halfStarSize * 2,
				halfStarSize * 2);
	}
	/**
	 * 天体ショー専用カーソルを描く
	 * @param g
	 */
	public void drawCursor(Graphics g) {
		g.setColor(getCursorColor());
		g.drawRect(
			getOffsetx() + (getCellSize() * getCellCursor().c() + getHalfCellSize()) / 2,
			getOffsety() + (getCellSize() * getCellCursor().r() + getHalfCellSize()) / 2,
			getHalfCellSize(),
			getHalfCellSize());
	}

	/*
	 * 「天体ショー」マウスリスナー
	 */
	protected void leftPressed(Address pos) {
		Area oldArea = board.getArea(pos.r, pos.c);
		if (draggingArea == null) {
			//  ここの if 文を有効にすれば，既存のAreaを内側から広げることができる
			//  ただし，undo と整合をどうするかが問題				
//			if (oldArea != null)
//				draggingArea = oldArea;
//			else
			draggingArea = new Area();
		}
		if (oldArea != null && oldArea != draggingArea) {
			board.removeAreaA(oldArea);
		}
		board.setArea(pos.r, pos.c, draggingArea);
		draggingArea.add(pos);
	}
	
	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos.r, pos.c);
		if (oldArea != null) {
			board.removeAreaA(oldArea);
		}
	}
	
	protected void leftDragged(Address pos) {
		leftPressed(pos);			
	}
	
	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}
	
	protected void leftDragFixed(Address dragEnd) {
		if (draggingArea == null)
			return;
		board.addAreaA(draggingArea);
		draggingArea = null;
	}
	
	protected void rightDragFixed(Address dragStart, Address dragEnd) {
		//			board.removeSquare(dragStart.r, dragStart.c, dragEnd.r, dragEnd.c);
		draggingArea = null;
	}

	protected void dragFailed() {
		if (draggingArea == null)
			return;
		board.addAreaA(draggingArea);
		draggingArea = null;
	}
	/*
	 * マウスではカーソル移動しない（暫定）
	 */
	protected void moveCursor(Address pos) {
	}

	/*
	 * 「天体ショー」キーリスナー
	 * 
	 * 問題入力モードのときのみ記号を入力する
	 * 0: 星除去
	 * 1: 白星
	 * 2: 黒星
	 */
	protected void numberEntered(Address p, int n) {
		if (isProblemEditMode())
			if (n == Board.BLACKSTAR || n == Board.WHITESTAR)
				board.setStar(p.r, p.c, n);
	}

	protected void spaceEntered(Address p) {
		if (isProblemEditMode())
			board.setStar(p.r, p.c, Board.NOSTAR);
	}
}
