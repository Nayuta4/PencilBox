package pencilbox.hakyukoka;

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
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				int n = board.getNumber(r,c);
				if (n > 0) {
					out.print(n);
				} else if (n == Board.UNDETERMINED) {
					out.print(0);
				} else {
					out.print('.');
				}
				out.print(' ');
			}
			out.println();
		}
		if (mode == QUESTION_ONLY)
			return;
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				if (!board.isStable(r,c)) {
					out.print(board.getState(r,c));
				} else {
					out.print('.');
				}
				out.print(' ');
			}
			out.println();
		}
	}
}
