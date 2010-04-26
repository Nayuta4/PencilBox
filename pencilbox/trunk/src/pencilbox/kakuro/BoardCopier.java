package pencilbox.kakuro;

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
		Rotator rotator2 = new Rotator(src.rows()-1, src.cols()-1, n);
		int sum = 0;
		for (int r = 1; r < src.rows(); r++) {
			for (int c = 1; c < src.cols(); c++) {
				Address p = Address.address(r-1, c-1);
				p = rotator2.rotateAddress(p);
				p = Address.address(p.r()+1, p.c()+1);
				if (d.isOn(p)) {
					sum = s.getNumber(r, c);
					d.setNumber(p.r(), p.c(), sum);
				}
			}
		}
		for (int r = 1; r < src.rows(); r++) {
			for (int c = 1; c < src.cols(); c++) {
				Address p = Address.address(r-1, c-1);
				p = rotator2.rotateAddress(p);
				p = Address.address(p.r()+1, p.c()+1);
				if (d.isOn(p)) {
					if (!s.isWall(r, c) && s.isWall(r-1, c)) {
						sum = s.getSumV(r-1, c);
							setSum(d, p.r(), p.c(), sum, Direction.VERT, n);
					}
					if (!s.isWall(r, c) && s.isWall(r, c-1)) {
						sum = s.getSumH(r, c-1);
							setSum(d, p.r(), p.c(), sum, Direction.HORIZ, n);
					}
				}
			}
		}
	}

	private void setSum(Board d, int r, int c, int sum, int dir, int rotation) {
		if ((dir == Direction.VERT) ^ Rotator.isTransposed(rotation)) {
			int rr = r;
			while (!d.isWall(rr, c))
				rr--;
			d.setSumV(rr, c, sum);
		} else {
			int cc = c;
			while (!d.isWall(r, cc))
				cc--;
			d.setSumH(r, cc, sum);
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				if (d.r() > 0 && d.c() > 0) {
					board.setNumber(d, srcBoard.getNumber(s));
				}
				if (board.isWall(d)) {
					if (rotation == 0 || rotation == 5)
						board.setSumV(d.r(), d.c(), srcBoard.getSumV(s.r(), s.c()));
					else if (rotation == 1 || rotation == 4)
						board.setSumH(d.r(), d.c(), srcBoard.getSumV(s.r(), s.c()));
					if (rotation == 0 || rotation == 7)
						board.setSumH(d.r(), d.c(), srcBoard.getSumH(s.r(), s.c()));
					else if (rotation == 3 || rotation == 4)
						board.setSumV(d.r(), d.c(), srcBoard.getSumH(s.r(), s.c()));
				}
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			int r = s.r();
			int c = s.c();
			if (r == 0 | c == 0)
				board.setWall(r, c, 0, 0);
			else
				board.removeWall(r, c);
		}
	}
}
