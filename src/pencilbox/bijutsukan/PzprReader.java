package pencilbox.bijutsukan;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 lightup.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decode4Cell();
	}

	protected void sQnC(int id, int num) {
		if (num == -2)
			bd.setState(i2a(id), Board.NONUMBER_WALL);
		else if (num >= 0 && num <= 4)
			bd.setState(i2a(id), num);
	}

}
