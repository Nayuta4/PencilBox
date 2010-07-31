package pencilbox.bijutsukan;

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
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.changeState(d, srcBoard.getState(s));
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.changeState(s, Board.UNKNOWN);
		}
	}

}
