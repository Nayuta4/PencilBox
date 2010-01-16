package pencilbox.goishi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.io.XmlWriterBase;

/**
 * 
 */
public class XmlWriter extends XmlWriterBase {

	protected void setBoardData(BoardBase theboard) {
		Board board = (Board) theboard;
		int rows = board.rows();
		int cols = board.cols();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				if (board.getState(r, c) == Board.STONE)
					outNumber(r, c, 1);
			}
		}
	}
	protected void setAnswerData(BoardBase theboard) {
		Board board = (Board) theboard;
		for(int i = 0; i < board.pickedList.size(); i++) {
			outPicked(i, board.pickedList.get(i));
		}
	}

	void outPicked(int n, Address p) {
		writer.println(2,"<picked n=\"" + (n+1) + "\" r=\"" + (p.r()+1) + "\" c=\"" + (p.c()+1) + "\" />");
	}
}
