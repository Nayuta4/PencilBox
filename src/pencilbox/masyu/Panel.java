package pencilbox.masyu;

import java.awt.Color;
import java.awt.Graphics2D;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelBase;
import pencilbox.util.Colors;


/**
 * 「ましゅ」パネルクラス
 */
public class Panel extends PanelBase {

	private Board board;

	private Color grayPearlColor = Color.GRAY;

	/**
	 * 
	 */
	public Panel() {
		setGridColor(Color.GRAY);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	public void drawBoard(Graphics2D g) {
		drawNumbers(g);
		drawGrid(g);
		drawLinks(g);
		drawBoardBorder(g);
	}

	private void drawNumbers(Graphics2D g) {
		for (Address p : board.cellAddrs()) {
			int number = board.getNumber(p);
			if (number == Board.WHITE_PEARL) {
				placeWhitePearl(g, p);
			} else if (number == Board.BLACK_PEARL) {
				placeBlackPearl(g, p);
			} else if (number == Board.GRAY_PEARL) {
				placeGrayPearl(g, p);
			}
		}
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
	
	private void placeBlackPearl(Graphics2D g, Address p) {
		g.setColor(getNumberColor());
		if (isIndicateErrorMode()) {
			int n = board.checkBlackPearl(p);
			if (n==-1)
				g.setColor(getErrorColor()); 
			else if (n==0)
				g.setColor(getErrorColor()); 
//			else if (n==1)
//				g.setColor(getErrorColor()); 
//			else if (n==2)
//				g.setColor(getNumberColor());
		}
		placeFilledCircle(g, p);
	}
	
	private void placeWhitePearl(Graphics2D g, Address p) {
		g.setColor(getNumberColor());
		if (isIndicateErrorMode()) {
			int n = board.checkWhitePearl(p);
			if (n==-1)
				g.setColor(getErrorColor()); 
			else if (n==0)
				g.setColor(getErrorColor()); 
//			else if (n==1)
//				g.setColor(getErrorColor()); 
//			else if (n==2)
//				g.setColor(getNumberColor());
		}
		placeBoldCircle(g, p);
	}
	
	private void placeGrayPearl(Graphics2D g, Address p) {
		g.setColor(grayPearlColor);
		placeFilledCircle(g, p);
	}

}
