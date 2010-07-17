package pencilbox.shikaku;

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
		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
		for (Square srcSquare : s.getSquareList()) {
			Address pos0 = rotator.rotateAddress(srcSquare.p0());
			Address pos1 = rotator.rotateAddress(srcSquare.p1());
			if (d.isOn(pos0) && d.isOn(pos1)) {
				Square dstSquare = new Square(pos0, pos1);
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
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (dstBoardBase.isOn(d)) {
				board.setNumber(d, srcBoard.getNumber(s));
			}
			srcSquare = srcBoard.getSquare(s);
			if (srcSquare != null) {
				if (s.equals(srcSquare.p0())) {
					if (region.containsAll(srcSquare.getCorners())) {
						Address d0 = translateAndRotateAddress(srcSquare.p0(), from, to, rotation);
						Address d1 = translateAndRotateAddress(srcSquare.p1(), from, to, rotation);
						dstSquare = new Square(d0, d1);
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
				if (s.equals(square.p0())) {
					if (region.containsAll(square.getCorners())) {
						board.removeSquare(square);
					}
				}
			}
		}
	}

}
