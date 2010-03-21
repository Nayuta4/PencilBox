package pencilbox.slitherlink;

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
		Rotator rotator2 = new Rotator(src.rows() - 1, src.cols() - 1, n);
		rotator2.rotateArrayInt2(s.getNumber(), d.getNumber());
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase dstBoardBase, Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) dstBoardBase;
		Address d = Address.address();
		Address dn = Address.address();
		Address rt = Address.address();
		Address dnrt = Address.address();
		int joint;
		int grid;
		int dir;
		Rotator2 rotator = new Rotator2(to, rotation);
		for (Address s : region) {
			d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
			d.set(rotator.rotateAddress(d));
			dn.set(s.r()+1, s.c());
			rt.set(s.r(), s.c()+1);
			dnrt.set(s.r()+1, s.c()+1);
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
			if (region.containsAll(dn, rt, dnrt)) {
				grid = srcBoard.getNumber(s);
				if (rotation == 0 || rotation == 4)
					d.set(d.r(), d.c());
				else if (rotation == 1 || rotation == 7)
					d.set(d.r()-1, d.c());
				else if (rotation == 2 || rotation == 6)
					d.set(d.r()-1, d.c()-1);
				else if (rotation == 3 || rotation == 5)
					d.set(d.r(), d.c()-1);
				if (board.isNumberOn(d))
					board.setNumber(d, grid);
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, Area region) {
		Board board = (Board) boardBase;
		Address dn = Address.address();
		Address rt = Address.address();
		Address dnrt = Address.address();
		for (Address s : region) {
			dn.set(s.r()+1, s.c());
			rt.set(s.r(), s.c()+1);
			dnrt.set(s.r()+1, s.c()+1);
			if (region.contains(dn)) {
				board.setStateJ(s, Direction.DN, Board.UNKNOWN);
			}
			if (region.contains(rt)) {
				board.setStateJ(s, Direction.RT, Board.UNKNOWN);
			}
			if (region.containsAll(dn, rt, dnrt)) {
				board.setNumber(s, Board.NONUMBER);
			}
		}
	}
}
