package pencilbox.fillomino;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * �Q�l�Fpzprv3 fillomino.js
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
		if (num > 0) {
			bd.setState(i2a(id),Board.STABLE);
			bd.setNumber(i2a(id),num);
		} else if (num == -2) {
			bd.setState(i2a(id),Board.STABLE);
			bd.setNumber(i2a(id), 0);
		}
	}

}