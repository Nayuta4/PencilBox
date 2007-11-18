package pencilbox.lits;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelBase;



/**
 * ÅuÇkÇhÇsÇrÅvÉpÉlÉãÉNÉâÉX
 */
public class Panel extends PanelBase {
	
	private Board board;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
	private Color areaBorderColor = Color.BLACK;
	private Color noAreaColor = new Color(0xC0C0C0);
	private Color tetrominoLColor = new Color(0xCCCC00);
	private Color tetrominoIColor = new Color(0xCC00CC);
	private Color tetrominoTColor = new Color(0x00CCCC);
	private Color tetrominoSColor = new Color(0x00CC00);
	private Color draggingAreaColor = new Color(0xCCFFFF);
	
	private boolean separateTetrominoColorMode = false;
	
	private Area draggingArea;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.BLACK);
		setMarkStyle(3);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return the draggingArea
	 */
	Area getDraggingArea() {
		return draggingArea;
	}

	/**
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Area draggingArea) {
		this.draggingArea = draggingArea;
	}

	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}
	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}
	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}
	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}

	/**
	 * @param areaBorderColor the areaBorderColor to set
	 */
	public void setAreaBorderColor(Color areaBorderColor) {
		this.areaBorderColor = areaBorderColor;
	}

	/**
	 * @return the areaBorderColor
	 */
	public Color getAreaBorderColor() {
		return areaBorderColor;
	}

	/**
	 * @return the separateTetrominoColorMode
	 */
	public boolean isSeparateTetrominoColorMode() {
		return separateTetrominoColorMode;
	}

	/**
	 * @param separateTetrominoColorMode the separateTetrominoColorMode to set
	 */
	public void setSeparateTetrominoColorMode(boolean separateTetrominoColorMode) {
		this.separateTetrominoColorMode = separateTetrominoColorMode;
	}

	public void drawBoard(Graphics2D g) {
		paintAreas(g);
		drawCells(g);
		drawGrid(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
	}
	
	private void paintAreas(Graphics2D g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r, c) == null) {
					g.setColor(noAreaColor);
					paintCell(g, r, c);
				} else if (board.getArea(r, c) == draggingArea) {
					g.setColor(draggingAreaColor);
					paintCell(g, r, c);
				}
			}
		}
	}

	/**
	 * É}ÉXÇÃèÛë‘Çï`âÊÇ∑ÇÈ
	 * @param g
	 */
	protected void drawCells(Graphics2D g) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int st = board.getState(r, c);
				if (st == Board.BLACK) {
					g.setColor(getPaintColor());
					if (isSeparateTetrominoColorMode()) {
						if (board.getArea(r, c) != null) {
							int t = board.getArea(r, c).getTetrominoType();
							g.setColor(getTetrominoColor(t));
						}
					}
					paintCell(g, r, c);
				} else if (st == Board.WHITE) {
					g.setColor(getCircleColor());
					placeMark(g, r, c);
				}
			}
		}
	}
	
	private Color getTetrominoColor(int type) {
		switch (type) {
		case Tetromino.TYPE_L :
			return tetrominoLColor;
		case Tetromino.TYPE_I :
			return tetrominoIColor;
		case Tetromino.TYPE_T :
			return tetrominoTColor;
		case Tetromino.TYPE_S :
			return tetrominoSColor;
		case Tetromino.TYPE_O :
			return getErrorColor();
		default :
			return getPaintColor();
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				if (board.getArea(r, c) != board.getArea(r, c + 1)) {
					placeSideLine(g, Direction.VERT, r, c);
				}
			}
			if (board.getArea(r,0) != null)
				placeSideLine(g, Direction.VERT, r, -1);
			if (board.getArea(r,board.cols()-1) != null)
				placeSideLine(g, Direction.VERT, r, board.cols()-1);
		}
		for (int c = 0; c < board.cols(); c++) {
			for (int r = 0; r < board.rows() - 1; r++) {
				if (board.getArea(r, c) != board.getArea(r + 1, c)) {
					placeSideLine(g, Direction.HORIZ, r, c);
				}
			}
			if (board.getArea(0, c) != null)
				placeSideLine(g, Direction.HORIZ, -1, c);
			if (board.getArea(board.rows()-1, c) != null)
				placeSideLine(g, Direction.HORIZ, board.rows()-1, c);
		}
	}

}
