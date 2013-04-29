package pencilbox.slitherlink;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.io.TxtWriterBase;


/**
 * 
 */
public class TxtWriter extends TxtWriterBase {

	public void writeProblem(PrintWriter out, BoardBase puzzleBoard, int mode) {
		Board board = (Board) puzzleBoard;

		int st;
		out.println(board.rows()-1);
		out.println(board.cols()-1);
		for(int r=0; r<board.rows()-1; r++){
			for(int c=0; c<board.cols()-1; c++){
				st = board.getNumber(r,c);
				if (st == Board.NONUMBER)    out.print('.');
				else if (st >= 0 && st <= 5)  out.print(st);
				else out.print('.');
				out.print(' ');
			}
			out.println();
		}
		if (mode == QUESTION_ONLY)
			return;
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols()-1; c++){
				out.print(board.getState(Direction.VERT,r,c));
				out.print(' ');
			}
			out.println();
		}
		for(int r=0; r<board.rows()-1; r++){
			for(int c=0; c<board.cols(); c++){
				out.print(board.getState(Direction.HORIZ,r,c));
				out.print(' ');
			}
			out.println();
		}

	}
}
