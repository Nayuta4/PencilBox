package pencilbox.yajilin;

import pencilbox.common.io.PzprWriterBase;


/**
 * QlFpzprv3 yajilin.js
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "yajirin";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		this.encodeArrowNumber16();
	}

	protected int QnC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n == Board.UNDECIDED_NUMBER)
			return -2;
		if (n >= 0)
			return bd.getArrowNumber(i2a(i));
		return -1;
	}

	protected int DiC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n == Board.UNDECIDED_NUMBER)
			return 0;                    // PencilBox‚Å‚Í–¢’è”š‚Í–îˆó‚È‚µ
		if (n >= 0) {
			int d = bd.getArrowDirection(i2a(i));
			switch (d) {
				case 0: return 1;
				case 1: return 3;
				case 2: return 2;
				case 3: return 4;
			}
		}
		return 0;
	}

}
