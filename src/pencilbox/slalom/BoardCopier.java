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
		Address dn = Address.address();
		Address rt = Address.address();
		int joint;
		int dir;
		for (Address s : region) {
			Address d = rotateAddress(s, from, to, rotation);
			dn.set(s.r()+1, s.c());
			rt.set(s.r(), s.c()+1);
			if (region.contains(dn)) {
				joint = srcBoard.getStateJ(s, Direction.DN);
				dir = Rotator2.rotateDirection(Direction.DN, rotation);
				if (board.isSideOn(d, dir))
					board.setStateJ(d, dir, joint);
			}
			if (region.contains(rt)) {
				joint = srcBoard.getStateJ(s, Direction.RT);
				dir = Rotator2.rotateDirection(Direction.RT, rotation);
				if (board.isSideOn(d, dir))
					board.setStateJ(d, dir, joint);
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
		Address dn = Address.address();
		Address rt = Address.address();
		for (Address s : region) {
			dn.set(s.r()+1, s.c());
			rt.set(s.r(), s.c()+1);
			if (region.contains(dn)) {
				board.setStateJ(s, Direction.DN, Board.UNKNOWN);
			}
			if (region.contains(rt)) {
				board.setStateJ(s, Direction.RT, Board.UNKNOWN);
			}
			board.setNumber(s, Board.BLANK);
		}
	}
}
