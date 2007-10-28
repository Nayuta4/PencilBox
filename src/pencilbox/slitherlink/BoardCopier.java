package pencilbox.slitherlink;

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
		Rotator rotator2 = new Rotator(src.rows() - 1, src.cols() - 1, n);
		rotator2.rotateArrayInt2(s.getNumber(), d.getNumber());
	}

}
