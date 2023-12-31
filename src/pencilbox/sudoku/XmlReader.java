package pencilbox.sudoku;

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
		if (n == 0)
			n = Board.UNDETERMINED;
		board.setNumber(r, c, n);
	}
	protected void setAN(int r, int c, int n) {
		// 初期化で0になっている、STABLEの所は０になっている
		if (n != 0)
			board.setState(r, c, n);
	}

}