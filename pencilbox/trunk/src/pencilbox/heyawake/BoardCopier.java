package pencilbox.heyawake;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
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
		rotator.rotateArrayInt2(s.getState(), d.getState());
		for (Square srcSquare : s.getSquareList()) {
			Address pos0 = rotator.rotateAddress(Address.address(srcSquare.r0(), srcSquare.c0()));
			Address pos1 = rotator.rotateAddress(Address.address(srcSquare.r1(), srcSquare.c1()));
			if (d.isOn(pos0) && d.isOn(pos1)) {
				Square dstSquare = new Square(pos0.r(), pos0.c(), pos1.r(), pos1.c(), srcSquare.getNumber());
				d.addSquare(dstSquare);
			}
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase dstBoardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) dstBoardBase;
		Square srcSquare = null;
		Square dstSquare = null;
		for (Address s : region) {
			Address d = rotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.setState(d, srcBoard.getState(s));
			}
			srcSquare = srcBoard.getSquare(s);
			if (srcSquare != null) {
				if (s.equals(srcSquare.r0(), srcSquare.c0())) {
					if (region.containsAll(srcSquare.getCorners())) {
						Address d0 = rotateAddress(srcSquare.p0(), from, to, rotation);
						Address d1 = rotateAddress(srcSquare.p1(), from, to, rotation);
						dstSquare = new Square(srcSquare);
						dstSquare.set(d0, d1);
						dstSquare.setNumber(srcSquare.getNumber());
						if (board.isOnAll(dstSquare.getCorners())) {
							board.removeOverlappedSquares(dstSquare, null);
							board.addSquare(dstSquare);
						}
					}
				}
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.setState(s, Board.UNKNOWN);
			Square square = board.getSquare(s);
			if (square != null) {
				if (s.equals(square.r0(), square.c0())) {
					if (region.containsAll(square.getCorners())) {
						board.removeSquare(square);
					}
				}
			}
		}
	}

}
