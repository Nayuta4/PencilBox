package pencilbox.numberlink;

import pencilbox.common.io.PzprWriterBase;


/**
 * 参考：pzprv3 numlin.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "numlin";
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
