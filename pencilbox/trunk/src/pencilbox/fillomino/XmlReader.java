package pencilbox.fillomino;

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
		if (n > 0)
			board.setNumber(r, c, n);
		else if (n == 0)
			board.setNumber(r, c, Board.UNDETERMINED);
	}
	protected void setAN(int r, int c, int n) {
		if (n != 0)
			board.setState(r, c, n);
	}
}
