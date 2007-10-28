package pencilbox.heyawake;

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
		rotator.rotateArrayInt2(s.getState(), d.getState());
		for (Square srcSquare : s.getSquareList()) {
			Address pos0 = rotator.rotateAddress(new Address(srcSquare.r0(), srcSquare.c0()));
			Address pos1 = rotator.rotateAddress(new Address(srcSquare.r1(), srcSquare.c1()));
			if (d.isOn(pos0) && d.isOn(pos1)) {
				Square dstSquare = new Square(pos0.r(), pos0.c(), pos1.r(), pos1.c(), srcSquare.getNumber());
				d.addSquare(dstSquare);
			}
		}
	}

}
