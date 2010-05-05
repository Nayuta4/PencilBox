package pencilbox.goishi;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelBase;


/**
 * �u��΂Ђ낢�v�p�l���N���X
 */
public class Panel extends PanelBase {

	private Board board;

	private Color inputColor = new Color(0x000099);
	private Color paintColor = new Color(0xFFFFFF);

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
		setGridStyle(1);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/**
	 * @return Returns the inputColor.
	 */
	public Color getInputColor() {
		return inputColor;
	}

	/**
	 * @param inputColor The inputColor to set.
	 */
	public void setInputColor(Color inputColor) {
		this.inputColor = inputColor;
	}

	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}

	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawNumbers(g);
//		drawBoardBorder(g);
	}

	public void drawGrid(Graphics2D g) {
		g.setColor(getGridColor());
		int style = getGridStyle();
		if (style == 0) {
			return;
		} else {
			for (int r = 0; r < rows(); r++) {
				g.drawLine(
					toX(0) + getHalfCellSize(),
					toY(r) + getHalfCellSize(),
					toX(cols()) - getHalfCellSize(),
					toY(r) + getHalfCellSize());
			}
			for (int c = 0; c < cols(); c++) {
				g.drawLine(
					toX(c) + getHalfCellSize(),
					toY(0) + getHalfCellSize(),
					toX(c) + getHalfCellSize(),
					toY(rows()) - getHalfCellSize());
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				if (board.getState(r, c) == Board.STONE) {
					g.setColor(getPaintColor());
					placeFilledCircle(g, r, c);
					int n = board.getNumber(r, c);
					if (n > 0) {
						g.setColor(getInputColor());
//						placeCircle(g, r, c);
						placeNumber(g, r, c, n);
					} else {
						g.setColor(getNumberColor());
						placeBoldCircle(g, r, c);
					}
				}
			}
		}
	}
}