package pencilbox.hitori;

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
		Rotator rotator = new Rotator(src.getSize(), n);
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

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.changeState(d, srcBoard.getState(s));
				board.changeNumber(d, srcBoard.getNumber(s));
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.changeState(s, Board.UNKNOWN);
			board.changeNumber(s, 0);
		}
	}

}
