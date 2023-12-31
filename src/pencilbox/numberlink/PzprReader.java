package pencilbox.numberlink;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * ŽQlFpzprv3 numlin.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decodeNumber16();
	}

	protected void sQnC(int id, int num) {
		if (num > 0)
			bd.setNumber(i2a(id), num);
		else if (num == -2)
			bd.setNumber(i2a(id), Board.UNDECIDED_NUMBER);
	}

}
