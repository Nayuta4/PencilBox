package pencilbox.lits;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;



/**
 * ÅuÇkÇhÇsÇrÅvÉpÉlÉãÉNÉâÉX
 */
public class Panel extends PanelBase {
	
	private Board board;

	private Color paintColor = new Color(0x0099FF);
	private Color circleColor = new Color(0xFF9999);
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
		for (Address p : board.cellAddrs()) {
			if (board.getArea(p) == null) {
				g.setColor(noAreaColor);
				paintCell(g, p);
			} else if (board.getArea(p) == draggingArea) {
				g.setColor(draggingAreaColor);
				paintCell(g, p);
			}
		}
	}

	/**
	 * É}ÉXÇÃèÛë‘Çï`âÊÇ∑ÇÈ
	 * @param g
	 */
	protected void drawCells(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int st = board.getState(p);
			if (st == Board.BLACK) {
				g.setColor(getPaintColor());
				if (isSeparateTetrominoColorMode()) {
					if (board.getArea(p) != null) {
						int t = board.getArea(p).getTetrominoType();
						g.setColor(getTetrominoColor(t));
					}
				}
				paintCell(g, p);
			} else if (st == Board.WHITE) {
				g.setColor(getCircleColor());
				placeMark(g, p);
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
		g.setColor(areaBorderColor);
		for (Address p : board.cellAddrs()) {
			for (int d : Direction.DN_RT) {
				Address p1 = p.nextCell(d);
				SideAddress b = SideAddress.get(p, d);
				if (board.isSideOn(b)) {
					if (board.getArea(p) != board.getArea(p1)) {
						placeSideLine(g, b);
					}
				}
			}
		}
	}

}
