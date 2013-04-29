package pencilbox.satogaeri;

import pencilbox.common.core.Address;
import pencilbox.common.core.SideAddress;
import pencilbox.common.io.PzprWriterBase;


/**
 * �Q�l�Fpzprv3 
 */
public class PzprWriter extends PzprWriterBase {

	private Board bd;

	protected String getPzprName() {
		return "satogaeri";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		makeBorderData();
		this.encodeBorder();
		this.encodeNumber16();
	}

	protected int getBorder(SideAddress p) {
		Address c1 = SideAddress.nextCellFromBorder(p, 0);
		Address c2 = SideAddress.nextCellFromBorder(p, 1);
		if (bd.getArea(c1) != bd.getArea(c2))
			return 1;
		else
			return 0;
	}

	protected int QnC(int i) {
		int n = bd.getNumber(i2a(i));
		if (n >= 0)
			return n;
		else if (n == Board.UNDETERMINED)
			return -2;
		else
			return -1;
	}

}
