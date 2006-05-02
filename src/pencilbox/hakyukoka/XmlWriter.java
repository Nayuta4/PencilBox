package pencilbox.hakyukoka;

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
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (board.isStable(r, c))
					outNumber(r,c,board.getNumber(r,c));
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
				if (!board.isStable(r, c))
					outN(board.getNumber(r, c));
				else
					outN(0);
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
