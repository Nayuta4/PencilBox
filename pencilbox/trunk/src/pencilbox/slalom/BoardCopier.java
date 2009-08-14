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
		Address d = new Address();
		Address dn = new Address();
		Address rt = new Address();
		int joint;
		int dir;
		Rotator2 rotator = new Rotator2(to, rotation);
		for (Address s : region) {
			d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
			d.set(rotator.rotateAddress(d));
			dn.set(s.r()+1, s.c());
			rt.set(s.r(), s.c()+1);
			if (region.contains(dn)) {
				joint = srcBoard.getStateJ(s, Direction.DN);
				dir = rotator.rotateDirection(Direction.DN);
				if (board.isSideOn(d, dir))
					board.setStateJ(d, dir, joint);
			}
			if (region.contains(rt)) {
				joint = srcBoard.getStateJ(s, Direction.RT);
				dir = rotator.rotateDirection(Direction.RT);
				if (board.isSideOn(d, dir))
					board.setStateJ(d, dir, joint);
			}
			if (board.isOn(d)) {
				if (srcBoard.getNumber(s) == Board.GATE_HORIZ) {
					if (rotator.isTransposed()) {
						board.setNumber(d, Board.GATE_VERT);
					} else {
						board.setNumber(d, Board.GATE_HORIZ);
					}
				} else if (srcBoard.getNumber(s) == Board.GATE_VERT) {
					if (rotator.isTransposed()) {
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
		Address dn = new Address();
		Address rt = new Address();
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
