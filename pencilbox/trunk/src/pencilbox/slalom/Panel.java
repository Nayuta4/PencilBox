package pencilbox.slalom;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;

/**
 * 「スラローム」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private int gateLineWidth = 3;
//	private Font smallFont = new Font("SansSerif", Font.BOLD, 14);

	/**
	 * 
	 */
	public Panel() {
		super();
		setGridColor(Color.BLACK);
		setNumberColor(Color.WHITE);
		setCircleColor(Color.BLACK);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	public void drawBoard(Graphics2D g) {
		drawGrid(g);
		drawGates(g);
		drawLinks(g);
		drawNumbers(g);
		drawBoardBorder(g);
	}

	private void drawGates(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
			int state = board.getNumber(p);
			if (state >= 0) {
				placeNumber(g, p, board.getNGate());
			} else if (state == Board.GATE_HORIZ) {
				g.setColor(getGateColor());
				placeGateLine(g, p, Direction.HORIZ);
			} else if (state == Board.GATE_VERT) {
				g.setColor(getGateColor());
				placeGateLine(g, p, Direction.VERT);
			}
//			if (state == Board.GATE_HORIZ || state == Board.GATE_VERT) {
//				g.setColor(getCircleColor());
//				if (board.isGate(p)) {
//					int n = board.getGateNumber(p);
//					if (n >= -1) {
//						placeSmallNumber(g, p, board.getGateNumber(p));
//					} else {
//						placeSmallNumber(g, p, n);
//					}
//				}
//			}
		}
	}

	private void drawNumbers(Graphics2D g) {
		g.setFont(getNumberFont());
		for (Address p : board.cellAddrs()) {
//			if (board.getLink(p) != null) {
//				g.setColor(backLineColor);
//				paintCell(g, p);
//			}
			int state = board.getNumber(p);
			if (state >= 0) {
				g.setColor(getWallColor());
				paintCell(g, p);
				if (state > 0) {
					g.setColor(getNumberColor());
					placeNumber(g, p, state);
				}
			} else if (state == Board.GOAL) {
				g.setColor(getBackgroundColor());
				placeFilledCircle(g, p, getCellSize()-2);
				g.setColor(getCircleColor());
//				placeBoldCircle(g, p, getCellSize()-2);
				placeCircle(g, p, getCellSize()-2);
				placeNumber(g, p, board.getNGate());
			}
		}
	}

//	private void placeSmallNumber(Graphics2D g, Address p, int n) {
//		g.setFont(smallFont);
//		g.setColor(Color.RED);
//		placeString(g, p, Integer.toString(n));
//		g.setFont(getNumberFont());
//	}

	private void placeGateLine(Graphics2D g, Address p, int dir) {
//		Stroke s = g.getStroke();
//		g.setStroke(dotStroke);
		if (dir == Direction.HORIZ)
			drawLineSegment(g, toX(p), toY(p) + getHalfCellSize(), dir, gateLineWidth);
		else if (dir == Direction.VERT)
			drawLineSegment(g, toX(p) + getHalfCellSize(), toY(p), dir, gateLineWidth);
//		g.setStroke(s);
	}

	private void drawLinks(Graphics2D g) {
		for (SideAddress p : board.borderAddrs()) {
			int state = board.getState(p);
			if (state == Board.LINE) {
				g.setColor(getLineColor());
				if (isSeparateLinkColorMode())
					g.setColor(Colors.getColor(board.getLink(p).getId()));
				placeLink(g, p);
			} else if (state == Board.NOLINE) {
				g.setColor(getCrossColor());
				placeSideCross(g, p);
			}
		}
	}
}
