package pencilbox.fillomino;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「フィルオミノ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	/**
	 * 
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
		if (isShowAreaBorderMode()) 
			drawAreaBorders(g);
		drawBoardBorder(g);
		drawEdges(g);
	}

	private void paintAreas(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int n = board.getNumberOrState(p);
			if (n > 0) {
				if (isIndicateErrorMode()) {
					int status = board.getArea(p).getStatus();
					if (status == -1) {
						g.setColor(getErrorColor());
						paintCell(g, p);
					} else if (status == 1) {
						g.setColor(successColor); 
						paintCell(g, p);
					}
				} else if (isSeparateAreaColorMode()) {
					g.setColor(Colors.getBrightColor(n));
					paintCell(g, p);
				}
			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int n = board.getNumber(p);
			if (n > 0) {
				g.setColor(getNumberColor());
				placeNumber(g, p, n);
			} else if (n == Board.UNDETERMINED) {
				g.setColor(getNumberColor());
				placeBoldCircle(g, p);
			} else {
				int s = board.getState(p);
				if (s > 0) {
					g.setColor(getInputColor());
					placeNumber(g, p, s);
				}
			}
		}
	}

	private void drawAreaBorders(Graphics2D g) {
		g.setColor(areaBorderColor);
		for (Address p : board.cellAddrs()) {
			for (int d : Direction.DN_RT) {
				Address p1 = p.nextCell(d);
				SideAddress b = SideAddress.get(p, d);
				if (board.isSideOn(b)) {
					if (board.getNumberOrState(p) != board.getNumberOrState(p1)) {
						placeSideLine(g, b);
					}
				}
			}
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
}