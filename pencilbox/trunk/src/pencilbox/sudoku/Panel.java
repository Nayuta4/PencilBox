package pencilbox.sudoku;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.HintDot;
import pencilbox.common.gui.PanelBase;


/**
 * 「数独」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color highlight2Color = new Color(0xFFFF80);

	private HintDot hintDot = new HintDot();
	private int unit;

	/**
	 * 
	 */
	public Panel() {
		setCursorMode(true);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
		unit = board.getUnit();
		// ドットヒント表示可能なのは，サイズ3,4,5のみ
		if (unit>=3 && unit<=5)
			hintDot.setDot(this, unit, getCellSize());
	}

	public void setDisplaySize(int cellSize) {
		super.setDisplaySize(cellSize);
		hintDot.setDotSize(cellSize);
	}

	public void drawBoard(Graphics2D g) {
		drawNumbers(g);
		drawGrid(g);
		drawAreaBorders(g);
	}

	/**
	 * ブロックの太枠線を描画する。
	 */
	public void drawAreaBorders(Graphics2D g) {
		g.setColor(getAreaBorderColor());
		for (int r = 0; r <= rows(); r++) {
			if (r % board.getUnit() == 0) {
				g.drawLine(toX(0), toY(r) - 1, toX(cols()), toY(r) - 1);
				g.drawLine(toX(0), toY(r), toX(cols()), toY(r));
				g.drawLine(toX(0), toY(r) + 1, toX(cols()), toY(r) + 1);
			}
		}
		for (int c = 0; c <= cols(); c++) {
			if (c % board.getUnit() == 0) {
				g.drawLine(toX(c) - 1, toY(0), toX(c) - 1, toY(rows()));
				g.drawLine(toX(c), toY(0), toX(c), toY(rows()));
				g.drawLine(toX(c) + 1, toY(0), toX(c) + 1, toY(rows()));
			}
		}
	}

	protected void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			paintCell1(g, p, board.getNumberOrState(p));
			int n = board.getNumber(p);
			if (n > 0) {
				g.setColor(getNumberColor());
				placeNumber(g, p, n);
			} else if (n == Board.UNDETERMINED) {
				if (isDotHintMode() && unit >= 3 && unit <=5) {
					placeHintDot(g, p);
				}
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			} else if (n == Board.BLANK) {
				int s = board.getState(p);
				if (s > 0) {
					g.setColor(getInputColor());
					if (isIndicateErrorMode()) {
						if (board.isMultipleNumber(p)) {
							g.setColor(getErrorColor());
						}
					}
					placeNumber(g, p, s);
				} else {
					if (isDotHintMode() && unit >= 3 && unit <=5) {
						placeHintDot(g, p);
					}
				}
			}
		}
	}
	// 選択数字と同じ行，列，ボックスを色塗り 
	private void paintCell1(Graphics2D g, Address p, int num) {
		if (isHighlightSelectionMode() && getSelectedNumber() > 0) {
			if (getSelectedNumber() == num) {
				g.setColor(highlightColor);
				paintCell(g, p);
			} else if (board.canPlace(p, getSelectedNumber())) {
				g.setColor(highlight2Color);
				paintCell(g, p);
			}
		}
	}

	private void placeHintDot(Graphics2D g, Address p) {
		int pat = board.getPattern(p);
		if (pat == 0) {
			hintDot.placeHintCross(g, p);
		} else {
			hintDot.placeHintDot(g, p, pat);
		}
	}

}
