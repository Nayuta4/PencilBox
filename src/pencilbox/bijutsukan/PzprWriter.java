package pencilbox.bijutsukan;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprWriterBase;


/**
 * ŽQlFpzprv3 lightup.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "lightup";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		encode4Cell();
	}

	protected int QnC(int i) {
		int n = bd.getState(i2a(i));
		if (n == Board.NONUMBER_WALL)
			return -2;
		else if (n >= 0 && n <= 4)
			return n;
		else
			return -1;
	}

}
