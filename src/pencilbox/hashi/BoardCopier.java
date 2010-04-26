package pencilbox.hashi;

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
		for (int r=0; r<s.rows(); r++) {
			for (int c=0; c<s.cols(); c++) {
				Address pos = Address.address(r, c);
				pos = rotator.rotateAddress(pos);
				if (d.isOn(pos)) {
					d.setNumber(pos.r(), pos.c(), s.getNumber(r,c));
				}
			}
		}
		for (int r=0; r<s.rows(); r++) {
			for (int c=0; c<s.cols(); c++) {
				Address pos = Address.address(r,c);
				pos = rotator.rotateAddress(pos);
				if (d.isOn(pos)) {
					int st = s.getState(r, c);
					if (rotator.isTransposed()) {
						d.setState(pos.r(), pos.c(), ((st & 0x3) << 2) | ((st & 0xC) >> 2));
					} else {
						d.setState(pos.r(), pos.c(), st);
					}
				}
			}
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.setNumber(d, srcBoard.getNumber(s));
			}
		}
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				int st = srcBoard.getState(s);
				if (Rotator2.isTransposed(rotation)) {
					board.setState(d, ((st & 0x3) << 2) | ((st & 0xC) >> 2));
				} else { 
					board.setState(d, st);
				}
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.setNumber(s, 0);
			board.setState(s, 0);
		}
	}
}
