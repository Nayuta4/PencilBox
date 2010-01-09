package pencilbox.masyu;

import pencilbox.common.io.PzprWriterBase;


/**
 * éQçlÅFpzprv3 mashu.js
 */
public class PzprWriter extends PzprWriterBase {
	
	private Board bd;
	
	protected String getPzprName() {
		return "mashu";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encodeCircle41_42();
	}

	 protected int QuC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n == Board.WHITE_PEARL)
			return 41;
		else if (n == Board.BLACK_PEARL)
			return 42;
		else if (n == Board.GRAY_PEARL)
			return -1;
		else
			return -1;
	}

}
