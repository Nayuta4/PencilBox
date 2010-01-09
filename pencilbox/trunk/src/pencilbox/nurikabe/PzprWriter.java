package pencilbox.nurikabe;

import pencilbox.common.io.PzprWriterBase;


/**
 * ŽQlFpzprv3 nurikabe.js
 */
public class PzprWriter extends PzprWriterBase {
	
	private Board bd;
	
	protected String getPzprName() {
		return "nurikabe";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encodeNumber16();
	}

	protected int QnC(int i) {
		int n = bd.getState(i2a(i));
		if (n > 0)
			return n;
		else if (n == Board.UNDECIDED_NUMBER)
			return -2;
		else
			return -1;
	}

}
