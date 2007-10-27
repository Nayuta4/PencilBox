/**
 * 
 */
package pencilbox.kurodoko;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Rotator;

/**
 * 
 */
public class BoardCopier extends pencilbox.common.core.BoardCopierBase {
	
	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator =  new Rotator(src.rows(), src.cols(), n);
		rotator.rotateArrayInt2(s.getState(), d.getState());
	}

}
