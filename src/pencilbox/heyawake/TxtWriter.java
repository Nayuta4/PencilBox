package pencilbox.heyawake;

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
			out.println(board.getSquareList().size());
			for(Square sq : board.getSquareList()) {
				out.print(sq.r0());
				out.print(' ');
				out.print(sq.c0());
				out.print(' ');
				out.print(sq.r1());
				out.print(' ');
				out.print(sq.c1());
				out.print(' ');
				if(sq.getNumber()>=0){
					out.print(sq.getNumber());
				}
				out.println();
			}
			if (mode == QUESTION_ONLY)
				return;
			for (int r=0; r<board.rows(); r++) {
				for (int c=0; c<board.cols(); c++) {
					int st = board.getState(r,c);
					if (st==Board.UNKNOWN) out.print('.');
					else if (st==Board.BLACK) out.print('#');
					else if (st==Board.WHITE) out.print('+');
					out.print(' ');
				}
				out.println();
			}
		
	}
}
