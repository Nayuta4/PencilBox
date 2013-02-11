package pencilbox.shakashaka;

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
		if (t == UNKNOWN)
			board.setState(r, c, Board.UNKNOWN);
		else if (t == SPACE)
			board.setState(r, c, Board.WHITE);
	}
	protected void setAN(int r, int c, int t) {
		if (Board.isTriangle(t))
			board.setState(r, c, t);
//		if (t == 2)
//			board.setState(r, c, Board.LTDN);
//		else if (t == 3)
//			board.setState(r, c, Board.RTDN);
//		else if (t == 4)
//			board.setState(r, c, Board.RTUP);
//		else if (t == 5)
//			board.setState(r, c, Board.LTUP);
	}
}
