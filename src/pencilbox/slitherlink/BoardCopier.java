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
			Address dn = Address.nextCell(s, Direction.DN);
			Address rt = Address.nextCell(s, Direction.RT);
			Address dnrt = Address.nextCell(dn, Direction.RT);
			if (region.containsAll(dn, rt, dnrt)) {
				int grid = srcBoard.getNumber(s);
				Address dd = null;
				if (rotation == 0 || rotation == 4)
					dd = d;
				else if (rotation == 1 || rotation == 7)
					dd = Address.address(d.r()-1, d.c());
				else if (rotation == 2 || rotation == 6)
					dd = Address.address(d.r()-1, d.c()-1);
				else if (rotation == 3 || rotation == 5)
					dd = Address.address(d.r(), d.c()-1);
				if (board.isNumberOn(dd))
					board.setNumber(dd, grid);
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address dn = Address.nextCell(s, Direction.DN);
			Address rt = Address.nextCell(s, Direction.RT);
			Address dnrt = Address.nextCell(dn, Direction.RT);
			for (int n : Direction.DN_RT) {
				if (region.contains(Address.nextCell(s, n))) {
					board.setStateJ(s, n, Board.UNKNOWN);
				}
			}
			if (region.containsAll(dn, rt, dnrt)) {
				board.setNumber(s, Board.NONUMBER);
			}
		}
	}
}
