/**
 * 
 */
package pencilbox.shikaku;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.ProblemCopierBase;
import pencilbox.common.core.Rotator;

/**
 * 
 */
public class ProblemCopier extends ProblemCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator =  new Rotator(src.rows(), src.cols(), n);
		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
		for (int i=0; i<s.getSquareList().size(); i++ ) {
			Square srcSquare = (Square) s.getSquareList().get(i);
			Address pos0 = rotator.rotateAddress(new Address(srcSquare.r0(), srcSquare.c0()));
			Address pos1 = rotator.rotateAddress(new Address(srcSquare.r1(), srcSquare.c1()));
			if (d.isOn(pos0) && d.isOn(pos1)) {
				Square dstSquare = new Square(pos0.r(), pos0.c(), pos1.r(), pos1.c());
				d.addSquare(dstSquare);
			}
		}
	}
}
