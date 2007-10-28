package pencilbox.yajilin;

import pencilbox.common.core.Address;
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
		rotator.rotateArrayInt3(s.getState(), d.getState());
		Address pos0 = new Address();
		Address pos;
		for (int r = 0; r < s.rows(); r++) {
			for (int c = 0; c < s.cols(); c++) {
				pos0.set(r, c);
				pos = rotator.rotateAddress(pos0);
				if (!d.isOn(pos))
					continue;
				if (s.getNumber(r,c) >= 0) {
					d.setArrowNumber(pos.r(), pos.c(), s.getArrowNumber(r, c));
					d.setArrowDirection(pos.r(), pos.c(), rotator.rotateDirection(s.getArrowDirection(r, c)));
				} else {
					d.setNumber(pos.r(), pos.c(), s.getNumber(r, c));
				}
			}
		}
	}

}
