package pencilbox.lits;

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
		rotator.rotateArrayInt2(s.getState(), d.getState());
		for (Area srcArea : s.getAreaList()) {
			Area a = new Area();
			rotator.rotateArea(srcArea, a);
			if (d.isAreaOn(a)) {
				d.addArea(a);
			}
		}
	}
}
