/**
 * 
 */
package pencilbox.hitori;

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
		Rotator rotator =  new Rotator(src.rows(), src.cols(), n);
    	rotator.rotateArrayInt2(s.getState(), d.getState());
    	rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
    	for (int r = 0; r < d.rows(); r++) {
    		for (int c = 0; c < d.cols(); c++) {
    			if (d.getNumber(r, c) > d.getMaxNumber()) {
    				d.setNumber(r, c, 0);
    			}
    		}
    	}
	}

}
