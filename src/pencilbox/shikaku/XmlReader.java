package pencilbox.shikaku;

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
	public void start(){
		if (node == "area") {
			int r0 = Integer.parseInt(attributes.getValue("r0"))-1;
			int c0 = Integer.parseInt(attributes.getValue("c0"))-1;
			int r1 = Integer.parseInt(attributes.getValue("r1"))-1;
			int c1 = Integer.parseInt(attributes.getValue("c1"))-1;
			board.addSquare(new Square(r0, c0, r1, c1));
		}
	}

}
