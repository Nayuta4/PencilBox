/**
 * 
 */
package pencilbox.hashi;

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
		Address pos0 = new Address();
		Address pos;
		Rotator rotator =  new Rotator(src.rows(), src.cols(), n);
		for (int r=0; r<s.rows(); r++) {
			for (int c=0; c<s.cols(); c++) {
				pos0.set(r,c);
				pos = rotator.rotateAddress(pos0);
				if (d.isOn(pos)) {
					d.setNumber(pos.r(), pos.c(), s.getNumber(r,c));
				}
			}
		}
		for (int r=0; r<s.rows(); r++) {
			for (int c=0; c<s.cols(); c++) {
				pos0.set(r,c);
				pos = rotator.rotateAddress(pos0);
				if (d.isOn(pos)) {
					int st = s.getState(r, c);
					if (rotator.isTransposed()) {
						d.setState(pos.r(), pos.c(), ((st & 0x3) << 2) | ((st & 0xC) >> 2));
					} else {
						d.setState(pos.r(), pos.c(), st);
					}
				}
			}
		}
	}
}
