package pencilbox.numberlink;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
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
				if (n>0 || n == Board.UNDECIDED_NUMBER)
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
				int type = 0;
				if (board.getState(Direction.HORIZ, r, c) == Board.LINE)
					type += 1;
				if (board.getState(Direction.VERT, r, c) == Board.LINE)
					type += 2;
				if(board.getState(Direction.HORIZ, r, c) == Board.NOLINE)
					type += 4;
				if(board.getState(Direction.VERT, r, c) == Board.NOLINE)
					type += 8;

				switch (type) {
					case 0 :
						out(SPACE);
						break;
					case 1 :
						out(HORIZ);
						break;
					case 2 :
						out(VERT);
						break;
					case 3 :
						out(HORIZ_VERT);
						break;
					default:
						outN(type);
				}
			}
			endArow();
		}
	}
}
