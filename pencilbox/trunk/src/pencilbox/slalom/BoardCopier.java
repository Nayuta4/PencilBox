package pencilbox.slalom;

import pencilbox.common.core.Address;
import pencilbox.common.core.Area;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Rotator2;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyRegion(BoardBase srcBoardBase, BoardBase dstBoardBase, Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) dstBoardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			for (int n : Direction.DN_RT) {
				if (region.contains(Address.nextCell(s, n))) {
					int joint = srcBoard.getStateJ(s, n);
					int dir = Rotator2.rotateDirection(n, rotation);
					if (board.isSideOn(d, dir))
						board.setStateJ(d, dir, joint);
				}
			}
			if (board.isOn(d)) {
				if (srcBoard.getNumber(s) == Board.GATE_HORIZ) {
					if (Rotator2.isTransposed(rotation)) {
						board.setNumber(d, Board.GATE_VERT);
					} else {
						board.setNumber(d, Board.GATE_HORIZ);
					}
				} else if (srcBoard.getNumber(s) == Board.GATE_VERT) {
					if (Rotator2.isTransposed(rotation)) {
						board.setNumber(d, Board.GATE_HORIZ);
					} else {
						board.setNumber(d, Board.GATE_VERT);
					}
				} else {
					board.setNumber(d, srcBoard.getNumber(s));
				}
			}
		}
	}

	public void eraseRegion(BoardBase srcBoardBase, Area region) {
		Board board = (Board) srcBoardBase;
		for (Address s : region) {
			for (int n : Direction.DN_RT){
				Address dn = Address.nextCell(s, n);
				if (region.contains(dn)) {
					board.setStateJ(s, n, Board.UNKNOWN);
				}
			}
			board.setNumber(s, Board.BLANK);
		}
	}
}
