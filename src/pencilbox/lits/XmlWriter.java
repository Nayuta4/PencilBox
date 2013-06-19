package pencilbox.lits;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.XmlWriterBase;

/**
 * 
 */
public class XmlWriter extends XmlWriterBase {
	protected void setBoardData(BoardBase theboard){
		Board board = (Board)theboard;
		outAreas(board.getAreaList().size());
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			startBrow(r);
			for (int c = 0; c < cols; c++) {
				if (board.getArea(r,c) == null)
					out(UNKNOWN);
				else
					outN(board.getAreaList().indexOf(board.getArea(r,c)));
			}
			endBrow();
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
				if (st == Board.BLACK) {
					out(WALL);
				} else if (st == Board.WHITE) {
					out(SPACE);
				} else {
					out(UNKNOWN);
				}
			}
			endArow();
		}
	}
	void outAreas(int v) {
		startTag(2, "areas");
		attribute("N", v);
		emptyTag();
	}
}
