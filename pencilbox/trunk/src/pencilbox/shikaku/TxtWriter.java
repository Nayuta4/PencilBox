package pencilbox.shikaku;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.TxtWriterBase;


/**
 * 
 */
public class TxtWriter extends TxtWriterBase {

	public void writeProblem(PrintWriter out, BoardBase puzzleBoard, int mode) {
		Board board = (Board) puzzleBoard;

		int num;
		out.println(board.rows());
		out.println(board.cols());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				num = board.getNumber(r, c);
					if (num == 0)    out.print('.');
					else out.print(num);
					out.print(' ');
			}
			out.println();
		}
		if (mode == QUESTION_ONLY)
			return;
		out.println(board.getSquareList().size());
		for(Square sq : board.getSquareList()) {
			out.print( sq.r0() );
			out.print( ' ' );
			out.print( sq.c0() );
			out.print( ' ' );
			out.print( sq.r1() );
			out.print( ' ' );
			out.print( sq.c1() );
			out.println();
		}
	}
}
