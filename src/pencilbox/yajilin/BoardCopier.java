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
		for (int r = 0; r < s.rows(); r++) {
			for (int c = 0; c < s.cols(); c++) {
				Address pos0 = Address.address(r, c);
				Address pos = rotator.rotateAddress(pos0);
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
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			for (int n : Direction.DN_RT) {
				if (region.contains(s.nextCell(n))) {
					int joint = srcBoard.getStateJ(s, n);
					int dir = Rotator2.rotateDirection(n, rotation);
					if (board.isSideOn(d, dir))
						board.setStateJ(d, dir, joint);
				}
			}
			if (board.isOn(d)) {
				int number = srcBoard.getNumber(s);
				if (number >= 0 || number == Board.BLACK) {
					board.eraseLinesAround(d);
				}
				board.setNumber(d, number);
				if (board.isNumber(d))
					board.setArrowDirection(d, Rotator2.rotateDirection(srcBoard.getArrowDirection(s), rotation));
			}
		}
	}

	public void eraseRegion(BoardBase srcBoardBase, Area region) {
		Board board = (Board) srcBoardBase;
		for (Address s : region) {
			for (int n : Direction.DN_RT) {
				if (region.contains(Address.nextCell(s, n))) {
					board.setStateJ(s, n, Board.UNKNOWN);
				}
			}
			board.setNumber(s, Board.BLANK);
		}
	}
}
