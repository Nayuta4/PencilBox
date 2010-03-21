package pencilbox.yajilin;

import pencilbox.common.core.Address;
import pencilbox.common.core.Area;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Rotator;
import pencilbox.common.core.Rotator2;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator = new Rotator(src.getSize(), n);
		rotator.rotateArrayInt3(s.getState(), d.getState());
		Address pos0 = Address.address();
		Address pos;
		for (int r = 0; r < s.rows(); r++) {
			for (int c = 0; c < s.cols(); c++) {
				pos0.set(r, c);
				pos = rotator.rotateAddress(pos0);
				if (!d.isOn(pos))
					continue;
				if (s.getNumber(pos0) >= 0) {
					d.setArrowNumber(pos, s.getArrowNumber(pos0));
					d.setArrowDirection(pos, rotator.rotateDirection(s.getArrowDirection(pos0)));
				} else {
					d.setNumber(pos, s.getNumber(pos0));
				}
			}
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase dstBoardBase, Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) dstBoardBase;
		Address d = Address.address();
		Address dn = Address.address();
		Address rt = Address.address();
		int joint;
		int number;
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
				board.setStateJ(d, dir, joint);
			}
			if (region.contains(rt)) {
				joint = srcBoard.getStateJ(s, Direction.RT);
				dir = rotator.rotateDirection(Direction.RT);
				board.setStateJ(d, dir, joint);
			}
			if (board.isOn(d)) {
				number = srcBoard.getNumber(s);
				if (number >= 0 || number == Board.BLACK) {
					board.eraseLinesAround(d);
				}
				board.setNumber(d, number);
				if (board.isNumber(d))
					board.setArrowDirection(d, rotator.rotateDirection(srcBoard.getArrowDirection(s)));
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
