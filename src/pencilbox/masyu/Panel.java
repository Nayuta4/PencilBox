package pencilbox.masyu;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandler;
import pencilbox.util.Colors;


/**
 * 「ましゅ」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;

	private boolean colorForEachLink = false;
	private boolean warnBranchedLink = false;
	private boolean warnWrongPearl = false;

	private Color lineColor = Color.BLUE;
	private Color crossColor = Color.MAGENTA;
	private Color pearlColor = Color.BLACK;
	private Color grayPearlColor = Color.GRAY;
	private Color errorColor = Color.RED;

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
	 * @return Returns the pearlColor.
	 */
	public Color getPearlColor() {
		return pearlColor;
	}
	/**
	 * @param pearlColor The pearlColor to set.
	 */
	public void setPearlColor(Color pearlColor) {
		this.pearlColor = pearlColor;
	}
	/**
	 * @param colorForEachLink The colorForEachLink to set.
	 */
	public void setColorForEachLink(boolean colorForEachLink) {
		this.colorForEachLink = colorForEachLink;
	}
	/**
	 * @param warnBranchedLink The warnBranchedLink to set.
	 */
	public void setWarnBranchedLink(boolean warnBranchedLink) {
		this.warnBranchedLink = warnBranchedLink;
	}
	/**
	 * @param warnWrongPearl The warnWrongPearl to set.
	 */
	public void setWarnWrongPearl(boolean warnWrongPearl) {
		this.warnWrongPearl = warnWrongPearl;
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
		int state;
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				state = board.getPearl(r, c);
				if (state == Board.WHITE_PEARL) {
					placeWhitePearl(g,r,c);
				} else if (state == Board.BLACK_PEARL) {
					placeBlackPearl(g,r,c);
				} else if (state == Board.GRAY_PEARL) {
					placeGrayPearl(g,r,c);
				}
			}
		}
		for (int d = 0; d <= 1; d++) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					state = board.getState(d, r, c);
					if (state == Board.LINE) {
						g.setColor(lineColor);
						if(colorForEachLink)
							g.setColor(Colors.getColor(board.getLink(d,r,c).getID()));
						if(warnBranchedLink && board.isBranchedLink(d,r,c))
							g.setColor(errorColor);
						placeTraversalLine(g, d, r, c);
					} else if (state == Board.NOLINE) {
						g.setColor(crossColor);
						placeSideCross(g, d, r, c);
					}
				}
			}
		}
	}
	
	private void placeBlackPearl(Graphics g, int r, int c) {
		if (warnBranchedLink || warnWrongPearl) {
			int p = board.checkBlackPearl(r,c);
			if (p==-1) g.setColor(errorColor); 
	//		else if (p==1) g.setColor(successColor);
			else g.setColor(pearlColor); 
		}
		placeFilledCircle(g, r, c);
	}
	
	private void placeWhitePearl(Graphics g, int r, int c) {
		if (warnBranchedLink || warnWrongPearl) {
			int p = board.checkWhitePearl(r,c);
			if (p==-1) g.setColor(errorColor);
	//		else if (p==1) g.setColor(successColor);
			else g.setColor(pearlColor);
		}
		placeBoldCircle(g, r, c);
	}
	
	private void placeGrayPearl(Graphics g, int r, int c) {
		g.setColor(grayPearlColor);
		placeFilledCircle(g, r, c);
	}

	/*
	 * ましゅ用マウスリスナー
	 * 盤内の辺に対して操作をする
	 * 左クリック： 線確定⇔未定
	 * 右クリック： 線なし確定⇔未定
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos.d, pos.r, pos.c, Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos.d, pos.r, pos.c, Board.NOLINE);
	}
	/*
	 * ましゅ用マウスモーションリスナー
	 * マスAからマスBへドラッグしたとき，
	 * AとBが同一行または列にあれば，
	 * 左ドラッグ： AからBまで線を引く
	 * 右ドラッグ： AからBまで線を消す
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r == dragEnd.r || dragStart.c == dragEnd.c) {
			board.determineInlineState(dragStart, dragEnd, Board.LINE);
		}
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r == dragEnd.r || dragStart.c == dragEnd.c) {
			board.determineInlineState(dragStart, dragEnd, Board.UNKNOWN);
		}
	}
	/*
	 * 「ましゅ」キーリスナー 問題入力モードのときのみ記号 space: 真珠除去 1: 白真珠 2: 黒真珠 -: 灰色真珠
	 */
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode())
			if(n == 1 || n == 2)
				board.setPearl(pos.r, pos.c, n);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setPearl(pos.r, pos.c, Board.NO_PEARL);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setPearl(pos.r, pos.c, Board.GRAY_PEARL);
	}
}
