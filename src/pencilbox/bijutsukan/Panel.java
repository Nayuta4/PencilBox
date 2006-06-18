package pencilbox.bijutsukan;

import java.awt.Color;
import java.awt.Graphics;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandler;


/**
 * 「美術館」パネルクラス
 */
public class Panel extends PanelEventHandler {

	private Board board;
	
	private boolean warnWrongIllumination = false;

	private Color wallColor = Color.BLACK;
	private Color illuminationColor = Color.BLUE;
	private Color illuminatedColor = new Color(0xAAFFFF);
	private Color noilluminationColor = Color.MAGENTA;
	private Color wallNumberColor = Color.WHITE;
	private Color errorColor = Color.RED;

	/**
	 * Panel を生成する
	 */
	public Panel() {
		setMaxInputNumber(5);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the warnWrongIllumination.
	 */
	public boolean isWarnWrongIllumination() {
		return warnWrongIllumination;
	}

	/**
	 * @param warnWrongIllumination The warnWrongIllumination to set.
	 */
	public void setWarnWrongIllumination(boolean warnWrongIllumination) {
		this.warnWrongIllumination = warnWrongIllumination;
	}

	/**
	 * @return Returns the illuminatedColor.
	 */
	public Color getIlluminatedColor() {
		return illuminatedColor;
	}

	/**
	 * @param illuminatedColor The illuminatedColor to set.
	 */
	public void setIlluminatedColor(Color illuminatedColor) {
		this.illuminatedColor = illuminatedColor;
	}

	/**
	 * @return Returns the illuminationColor.
	 */
	public Color getIlluminationColor() {
		return illuminationColor;
	}

	/**
	 * @param illuminationColor The illuminationColor to set.
	 */
	public void setIlluminationColor(Color illuminationColor) {
		this.illuminationColor = illuminationColor;
	}

	/**
	 * @return Returns the noilluminationColor.
	 */
	public Color getNoilluminationColor() {
		return noilluminationColor;
	}

	/**
	 * @param noilluminationColor The noilluminationColor to set.
	 */
	public void setNoilluminationColor(Color noilluminationColor) {
		this.noilluminationColor = noilluminationColor;
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
		g.setFont(getNumberFont());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int state = board.getState(r, c);
				int l = board.getHorizIlluminated(r,c);
				int m = board.getVertIlluminated(r,c);
				if (l>0 || m>0) {
					g.setColor(illuminatedColor);
					paintCell(g,r,c);
				}
				g.setColor(illuminationColor);
				if (l > 0) {
					if (warnWrongIllumination && l > 1) {
						g.setColor(errorColor);
					}
					placeMidline(g, r, c, Direction.HORIZ);
				}
				g.setColor(illuminationColor);
				if (m > 0) {
					if (warnWrongIllumination && m > 1) {
						g.setColor(errorColor);
					}
					placeMidline(g, r, c, Direction.VERT);
				}
				if (state >= 0 && state <= Board.NONUMBER_WALL) {
					g.setColor(wallColor);
					paintCell(g, r, c);
					if (state >= 0 && state <= 4) {
						g.setColor(wallNumberColor);
//						if (numberHintMode) {
//							int st = board.getWallState(r,c);
//							if (st == -1) g.setColor(errorColor);
//							else if (st == 1) g.setColor(successColor);
//						} else
						if (warnWrongIllumination == true) {
							int st = board.checkAdjacentIllumination(r,c);
							if (st == -1)
								g.setColor(errorColor);
						}
						placeNumber(g, r, c, state);
					}
				} else if (state == Board.ILLUMINATION) {
					g.setColor(illuminationColor);
					if (warnWrongIllumination == true && board.isMultiIlluminated(r,c)) {
						g.setColor(errorColor);
					}
					placeFilledCircle(g, r, c);
				} else if (state == Board.NOILLUMINATION) {
					g.setColor(noilluminationColor);
					placeCross(g, r, c);
				}
			}
		}
	}

	/*
	 * 「美術館」マウス操作
	 * 左プレス：未定⇔照明配置
	 * 右プレス：未定⇔照明なし
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.ILLUMINATION);
	}

	protected void rightPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.NOILLUMINATION);
		if (board.isWall(pos.r(), pos.c()))
			currentState = Board.UNKNOWN;
		else
			currentState = board.getState(pos.r(), pos.c());
	}

	protected void leftDragged(Address pos) {
		// 何もしない
	}

	protected void rightDragged(Address pos) {
		if (board.isWall(pos.r(), pos.c()))
			return;
		if (board.getState(pos.r(), pos.c()) == currentState)
			return;
		board.changeStateA(pos.r(), pos.c(), currentState);
	}

	/*
	 * 「美術館」キー操作
	 * 問題入力モードのときのみ記号
	 * 0-4: その数字つきの壁
	 * 5: 数字なしの壁
	 */
	protected void numberEntered(Address pos, int num) {
		if (!isProblemEditMode())
			return;
		board.changeState(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (!isProblemEditMode())
			return;
		board.changeState(pos.r(), pos.c(), -1);
	}

	protected void minusEntered(Address pos) {
		if (!isProblemEditMode())
			return;
		board.changeState(pos.r(), pos.c(), Board.NONUMBER_WALL);
	}
}
