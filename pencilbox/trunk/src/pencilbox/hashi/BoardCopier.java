package pencilbox.hashi;

import pencilbox.common.core.Address;
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

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.changeNumber(d, srcBoard.getNumber(s));
			}
		}
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				if (srcBoard.isPier(s)) {
					Pier pi = srcBoard.getPier(s);
					for (int dir : Direction.DN_RT) {
						Pier pi2 = pi.getNextPier(dir);
						if (pi2 != null) {
							int n = pi.getLine(dir);
							Address s2 = pi.getNextPier(dir).getPos();
							if (region.contains(s2)) {
								Address d2 = translateAndRotateAddress(s2, from, to, rotation);
								int ddir = Rotator2.rotateDirection(dir, rotation);
								if (board.isOn(d2)) {
									if (board.getPier(d).getNextPier(ddir).getPos().equals(d2)) {
										board.changeLine(d, ddir, n);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.changeNumber(s, 0);
		}
	}
}
