package pencilbox.norinori;

import pencilbox.common.core.Address;
import pencilbox.common.core.SideAddress;
import pencilbox.common.io.PzprWriterBase;


public class PzprWriter extends PzprWriterBase {
	
	private Board bd;
	
	protected String getPzprName() {
		return "norinori";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		makeBorderData();
		this.encodeBorder();
	}

	protected int getBorder(SideAddress b) {
		Address c1 = SideAddress.nextCellFromBorder(b, 0);
		Address c2 = SideAddress.nextCellFromBorder(b, 1);
		if (bd.getArea(c1) != bd.getArea(c2))
			return 1;
		else
			return 0;
	}

}
