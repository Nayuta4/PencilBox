package pencilbox.hitori;

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
		if (t == WALL)
			board.setState(r, c, Board.BLACK);
		else if (t == SPACE)
			board.setState(r, c, Board.WHITE);
	}
}
