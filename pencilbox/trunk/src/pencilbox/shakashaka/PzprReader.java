package pencilbox.shakashaka;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * éQçlÅFpzprv3 shakashaka.js
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
		bd.setNumber(i2a(id), num);
	}

}
