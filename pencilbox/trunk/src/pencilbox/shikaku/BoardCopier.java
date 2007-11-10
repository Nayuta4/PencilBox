package pencilbox.shikaku;

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
		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
		for (Square srcSquare : s.getSquareList()) {
			Address pos0 = rotator.rotateAddress(new Address(srcSquare.r0(), srcSquare.c0()));
			Address pos1 = rotator.rotateAddress(new Address(srcSquare.r1(), srcSquare.c1()));
			if (d.isOn(pos0) && d.isOn(pos1)) {
				Square dstSquare = new Square(pos0.r(), pos0.c(), pos1.r(), pos1.c());
				d.addSquare(dstSquare);
			}
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase dstBoardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) dstBoardBase;
		Square srcSquare = null;
		Square dstSquare = null;
		Address d = new Address();
		Address d0 = new Address();
		Address d1 = new Address();
		Rotator2 rotator = new Rotator2(to, rotation);
		for (Address s : region) {
			d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
			d.set(rotator.rotateAddress(d));
			if (dstBoardBase.isOn(d)) {
				board.setNumber(d, srcBoard.getNumber(s));
			}
			srcSquare = srcBoard.getSquare(s);
			if (srcSquare != null) {
				if (s.equals(srcSquare.r0(), srcSquare.c0())) {
					if (region.containsAll(srcSquare.getCorners())) {
						d0.set(srcSquare.r0(), srcSquare.c0());
						d0.set(d0.r() + to.r() - from.r(), d0.c() + to.c() - from.c());
						d0.set(rotator.rotateAddress(d0));
						d1.set(srcSquare.r1(), srcSquare.c1());
						d1.set(d1.r() + to.r() - from.r(), d1.c() + to.c() - from.c());
						d1.set(rotator.rotateAddress(d1));
						dstSquare = new Square(srcSquare);
						dstSquare.set(d0.r(), d0.c(), d1.r(), d1.c());
						dstSquare.setNumber(srcSquare.getNumber());
						if (dstBoardBase.isOnAll(dstSquare.getCorners())) {
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
			board.setNumber(s, 0);
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
