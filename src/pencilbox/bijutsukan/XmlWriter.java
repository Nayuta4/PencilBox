package pencilbox.bijutsukan;

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
				int n = board.getState(r, c);
				if (Board.isWall(n))
					outNumber(r,c,n);
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
				if (st == Board.BULB) {
					out(WALL);
				} else if (st == Board.NOBULB) {
					out(SPACE);
				} else {
					out(UNKNOWN);
				}
			}
			endArow();
		}
	}
}
