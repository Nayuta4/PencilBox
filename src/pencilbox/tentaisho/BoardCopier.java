package pencilbox.tentaisho;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator =  new Rotator(src.getSize(), n);
		for (Area srcArea : s.getAreaList()) {
			Area a = new Area();
			rotator.rotateArea(srcArea, a);
			if (d.isAreaOn(a)) {
				d.addArea(a);
			}
		}
		Rotator rotator2 = new Rotator(s.rows()*2-1, s.cols()*2-1, n);
		rotator2.rotateArrayInt2(s.getStar(), d.getStar());
	}

}
