package pencilbox.kurodoko;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprWriterBase;


/**
 * 参考：pzprv3 kudodoko.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "kurodoko";
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
