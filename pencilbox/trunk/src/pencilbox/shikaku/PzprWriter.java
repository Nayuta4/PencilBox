package pencilbox.shikaku;

import pencilbox.common.io.PzprWriterBase;


/**
 * ŽQlFpzprv3 shikaku.js
 */
public class PzprWriter extends PzprWriterBase {
	
	private Board bd;
	
	protected String getPzprName() {
		return "shikaku";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encodeNumber16();
	}

	protected int QnC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n > 0)
			return n;
		else if (n == Board.UNDECIDED_NUMBER)
			return -2;
		else
			return -1;
	}

}
