package pencilbox.kakuro;


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
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				if (board.isWall(r,c)) {
					out.print(r);
					out.print(' ');
					out.print(c);
					out.print(' ');
					out.print(board.getSumH(r,c));
					out.print(' ');
					out.print(board.getSumV(r,c));
					out.println();
				}
			}
		}
		if (mode == QUESTION_ONLY)
			return;
		out.println();
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				if (board.getNumber(r,c)>0) {
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
