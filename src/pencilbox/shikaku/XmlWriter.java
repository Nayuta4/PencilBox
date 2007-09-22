package pencilbox.shikaku;

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
				int n = board.getNumber(r,c);
//				if (n>0)
					outNumber(r,c,n);
			}
		}
	}
	protected void setAnswerData(BoardBase theboard){
		Board board = (Board)theboard;
		for(Square sq : board.getSquareList()) {
			outArea(sq);
		}
	}
	void outArea(Square area) {
		writer.println(2,"<area r0=\"" + (area.r0()+1) + "\" c0=\"" + (area.c0()+1) + "\" r1=\"" + (area.r1()+1) + "\" c1=\"" + (area.c1()+1) + "\"/>");
	}
}
