package pencilbox.slitherlink;

import pencilbox.common.io.PzprWriterBase;


/**
 * ŽQlFpzprv3 slither.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "slither";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows()-1, bd.cols()-1);
		encode4Cell();
	}

	protected int QnC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n == Board.UNDECIDED_NUMBER)
			return -2;
		else if (n >= 0 && n <= 4)
			return n;
		else
			return -1;
	}

}
