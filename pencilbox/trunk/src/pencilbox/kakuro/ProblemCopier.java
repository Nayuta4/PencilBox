/**
 * 
 */
package pencilbox.kakuro;

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
		if (rotator.isTransposed()) {
			rotator.rotateArrayInt2(s.getSumH(), d.getSumV());
			rotator.rotateArrayInt2(s.getSumV(), d.getSumH());
		} else {
			rotator.rotateArrayInt2(s.getSumH(), d.getSumH());
			rotator.rotateArrayInt2(s.getSumV(), d.getSumV());
		}
	}

}
