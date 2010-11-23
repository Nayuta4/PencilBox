package pencilbox.shikaku;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「四角に切れ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color smallSizeColor = new Color(0xFFFF99); // 面積小さい
	private Square draggingSquare; // ドラッグして今まさに描こうとしている四角

	/**
	 * パネルを生成する
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setAreaBorderColor(new Color(0x000099));
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}
	
	public void drawBoard(Graphics2D g) {
		paintAreas(g);
		drawNumbers(g);
		drawGrid(g);
		drawAreaBorders(g);
		drawBoardBorder(g);
		drawEdges(g);
	}

	private void paintAreas(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			Square square = board.getSquare(p);
			if (square == null)
				continue;
			g.setColor(areaPaintColor);
			if (isIndicateErrorMode()) {
				int number = square.getNumber();
				if (number == Square.MULTIPLE_NUMBER) {
					g.setColor(getErrorColor());
				} else if (number == Square.NO_NUMBER) {
					g.setColor(smallSizeColor);
				} else if (number == Board.UNDECIDED_NUMBER) {
					g.setColor(areaPaintColor);
				} else if (number < square.getSquareSize()) {
					g.setColor(getErrorColor());
				} else if (number == square.getSquareSize()) {
					g.setColor(areaPaintColor);
				} else if (number > square.getSquareSize()) {
					g.setColor(smallSizeColor);
				}
			} else if (isSeparateAreaColorMode()) {
				g.setColor(Colors.getBrightColor(board.getSquare(p).getId()));
			}
			paintCell(g, p);
		}
		Square square = getDraggingSquare();
		if (square != null) {
			for (Address p : square.cellSet()) {
				g.setColor(draggingAreaColor);
				paintCell(g, p);
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		int number;
		for (Address p : board.cellAddrs()) {
			number = board.getNumber(p);
			if (number > 0 || number == Board.UNDECIDED_NUMBER) {
				g.setColor(getNumberColor());
				super.placeFilledCircle(g, p, (int)(getCellSize()*0.85) );
			}
			if (number > 0) {
				g.setColor(getBackgroundColor());
				super.placeNumber(g, p, number);
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
		for (Square square : board.getSquareList()) {
			placeSquare(g, square);
		}
		Square square = getDraggingSquare();
		if (square != null) {
			placeSquare(g, square);
		}
	}

	private void drawEdges(Graphics2D g) {
		g.setColor(borderColor);
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getEdge(p);
			if (state == Board.LINE) {
				placeSideLine(g, p);
			}
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		this.draggingSquare = draggingSquare;
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return draggingSquare;
	}

}
