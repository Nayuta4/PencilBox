package pencilbox.goishi;

import pencilbox.common.core.Address;
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
		board.setState(r, c, Board.STONE);
	}
	public void start(){
		if (node == "picked") {
			int n = Integer.parseInt(attributes.getValue("n"))-1;
			int r = Integer.parseInt(attributes.getValue("r"))-1;
			int c = Integer.parseInt(attributes.getValue("c"))-1;
			Address p = Address.address(r, c);
			board.pickedList.add(p);
			board.setNumber(p, board.pickedList.size());
		}
	}

}