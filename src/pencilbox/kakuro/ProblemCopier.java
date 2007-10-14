/**
 * 
 */
package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.ProblemCopierBase;
import pencilbox.common.core.Rotator;

/**
 * 
 */
public class ProblemCopier extends ProblemCopierBase {
	
	private Board d;

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		this.d = (Board) dst;
		Rotator rotator2 =  new Rotator(src.rows()-1, src.cols()-1, n);
		Address p0 = new Address();
		Address p = new Address();
		int sum = 0;
		for (int r = 1; r < src.rows(); r++) {
			for (int c = 1; c < src.cols(); c++) {
				p0.set(r-1, c-1);
				p = rotator2.rotateAddress(p0);
				p.set(p.r()+1, p.c()+1);
				if (d.isOn(p)) {
					sum = s.getNumber(r, c);
					d.setNumber(p.r(), p.c(), sum);
				}
			}
		}
		for (int r = 1; r < src.rows(); r++) {
			for (int c = 1; c < src.cols(); c++) {
				p0.set(r-1, c-1);
				p = rotator2.rotateAddress(p0);
				p.set(p.r()+1, p.c()+1);
				if (d.isOn(p)) {
					if (!s.isWall(r, c) && s.isWall(r-1, c)) {
						sum = s.getSumV(r-1, c);
							setSum(p.r(), p.c(), sum, Direction.VERT, n);
					}
					if (!s.isWall(r, c) && s.isWall(r, c-1)) {
						sum = s.getSumH(r, c-1);
							setSum(p.r(), p.c(), sum, Direction.HORIZ, n);
					}
				}
			}
		}
	}

	private void setSum(int r, int c, int sum, int dir, int rotation) {
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

}
