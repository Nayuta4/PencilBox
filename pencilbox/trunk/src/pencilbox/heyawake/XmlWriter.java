package pencilbox.heyawake;

import java.util.Iterator;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.XmlWriterBase;


/**
 * 
 */
public class XmlWriter extends XmlWriterBase {
	protected void setBoardData(BoardBase theboard){
		Board board = (Board)theboard;
		for(Iterator itr = board.getSquareListIterator(); itr.hasNext(); ) {
			Square area = (Square) itr.next();
			outRoom(area);
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
	void outRoom(Square area) {
		writer.println(2,"<area r0=\"" + (area.r0()+1) + "\" c0=\"" + (area.c0()+1) + "\" r1=\"" + (area.r1()+1) + "\" c1=\"" + (area.c1()+1) + "\" n=\"" + area.getNumber() + "\"/>");
	}
}
