package pencilbox.kakuro;

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
	protected void start() {
		if (node == "wall") {
			int r = Integer.parseInt(attributes.getValue("r"))-1;
			int c = Integer.parseInt(attributes.getValue("c"))-1;
			int a = Integer.parseInt(attributes.getValue("a"));
			int b = Integer.parseInt(attributes.getValue("b"));
			board.setWall(r,c,a,b);
		}
	}
	protected void setBN(int r, int c, int n) {
		board.setNumber(r, c, n);
	}
	protected void setAN(int r, int c, int n) {
		board.setNumber(r, c, n);
	}
}
