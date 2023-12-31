package pencilbox.masyu;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 mashu.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decodeCircle41_42();
	}

	protected void sQuC(int id, int num) {
		if (num == 41)
			bd.setNumber(i2a(id), Board.WHITE_PEARL);
		else if (num == 42)
			bd.setNumber(i2a(id), Board.BLACK_PEARL);
		else
			;
	}

}
