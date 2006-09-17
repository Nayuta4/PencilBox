package pencilbox.masyu;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.XmlReaderBase;

/**
 * 
 */
public class XmlReader extends XmlReaderBase {
	
	private Board board;

	protected BoardBase makeBoard() {
		board = new Board();
		return board;
	}
	protected void setBN(int r, int c, int n) {
		board.setNumber(r, c, n);
	}
	protected void setAType(int r, int c, String t) {
		if (t == VERT)
			board.setState(Board.VERT, r, c, Board.LINE);
		else if (t == HORIZ)
			board.setState(Board.HORIZ, r, c, Board.LINE);
		else if (t == HORIZ_VERT) {
			board.setState(Board.VERT, r, c, Board.LINE);
			board.setState(Board.HORIZ, r, c, Board.LINE);
		}
	}
	protected void setAN(int r, int c, int n) {
		if ((n & 1) != 0)
			board.setState(Board.HORIZ, r, c, Board.LINE);
		if ((n & 2) != 0)
			board.setState(Board.VERT, r, c, Board.LINE);
		if ((n & 4) != 0)
			board.setState(Board.HORIZ, r, c, Board.NOLINE);
		if ((n & 8) != 0)
			board.setState(Board.VERT, r, c, Board.NOLINE);
	}
}
