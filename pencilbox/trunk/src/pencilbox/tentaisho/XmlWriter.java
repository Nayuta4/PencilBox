package pencilbox.tentaisho;

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
		for (int r = 0; r < rows*2-1; r++) {
			for (int c = 0; c < cols*2-1; c++) {
				if (board.getStar(r,c) > 0)
					outNumber(r,c,board.getStar(r,c));
			}
		}
	}
	protected void setAnswerData(BoardBase theboard){
		Board board = (Board)theboard;
		outAreas(board.getAreaList().size());
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			startArow(r);
			for (int c = 0; c < cols; c++) {
				if (board.getArea(r,c) == null)
					out(UNKNOWN);
				else 
					outN(board.getAreaList().indexOf(board.getArea(r,c)));
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
