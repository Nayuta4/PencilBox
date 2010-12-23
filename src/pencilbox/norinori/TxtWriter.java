package pencilbox.norinori;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.TxtWriterBase;


/**
 * 
 */
public class TxtWriter extends TxtWriterBase {

	public void writeProblem(PrintWriter out, BoardBase puzzleBoard, int mode) {
		
		Board board = (Board) puzzleBoard;
		
		out.println(board.rows());
		out.println(board.cols());
		out.println(board.getAreaList().size());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r, c) == null)
					out.print(-1);
				else
					out.print(board.getAreaList().indexOf(board.getArea(r, c)));
				out.print(' ');
			}
			out.println();
		}
		if (mode == QUESTION_ONLY)
			return;
		int s;
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				s = board.getState(i,j);
				if (s > 0) {
					out.print(s);
				} else if (s == Board.UNKNOWN) {
					out.print('.');
				} else if (s == Board.WHITE) {
					out.print('+');
				} else if (s == Board.BLACK) {
					out.print('#');
				} else {
					out.print('?');
				}
				out.print(' ');
			}
			out.println();
		}
	}

}
