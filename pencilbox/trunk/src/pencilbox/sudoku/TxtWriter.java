package pencilbox.sudoku;


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
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				if (board.isStable(r,c)) {
					int n = board.getNumber(r,c);
					if (n == Board.UNDETERMINED)
						n = 0;
					out.print(board.getNumber(r,c));
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
					out.print(board.getNumber(r,c));
				} else {
					out.print('.');
				}
				out.print(' ');
			}
			out.println();
		}
		
	}
}
