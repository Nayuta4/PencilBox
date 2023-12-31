package pencilbox.shakashaka;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.XmlWriterBase;

/**
 * 
 */
public class XmlWriter extends XmlWriterBase {
	protected void setBoardData(BoardBase theboard){
		Board board = (Board)theboard;
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				int n = board.getNumber(r, c);
				if (n >=0 && n <= 4 || n==Board.NONUMBER_WALL) {
					outNumber(r,c,n);
				}
			}
		}
	}
	protected void setAnswerData(BoardBase theboard){
		Board board = (Board)theboard;
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			startArow(r);
			for (int c = 0; c < cols; c++) {
				int st = board.getState(r, c);
				if (st == Board.UNKNOWN)
					out(UNKNOWN);
				else if (st == Board.WHITE)
					out(SPACE);
				else if (Board.isTriangle(st))
					outN(st);
//				else if (st == Board.LTDN)
//					out(N2);
//				else if (st == Board.RTDN)
//					out(N3);
//				else if (st == Board.RTUP)
//					out(N4);
//				else if (st == Board.LTUP)
//					out(N5);
			}
			endArow();
		}
	}
}
