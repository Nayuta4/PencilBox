package pencilbox.hashi;

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
				if (n>0) outNumber(r,c,n);
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
				outN(board.getState(r,c));
			}
			endArow();
		}
	}
}
