package pencilbox.kakuro;

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
				if (board.isWall(r, c))
					outWall(r,c,board.getSumH(r,c), board.getSumV(r,c));
			}
		}
	}
	void outWall(int r, int c, int a, int b) {
		writer.println(2,"<wall r=\"" + (r+1) + "\" c=\"" + (c+1) + "\" a=\"" + a + "\" b=\"" + b + "\"/>");
	}
	protected void setAnswerData(BoardBase theboard){
		Board board = (Board)theboard;
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			startArow(r);
			for (int c = 0; c < cols; c++) {
				outN(board.getNumber(r,c));
			}
			endArow();
		}
	}
}
