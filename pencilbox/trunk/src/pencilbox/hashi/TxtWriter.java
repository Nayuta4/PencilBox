package pencilbox.hashi;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.TxtWriterBase;


/**
 * 
 */
public class TxtWriter extends TxtWriterBase {

	public void writeProblem(PrintWriter out, BoardBase puzzleBoard) {
		Board board = (Board) puzzleBoard;
		int st;
		out.println(board.rows());
		out.println(board.cols());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				st = board.getNumber(r, c);
				if (st == 0)
					out.print('.');
				else
					out.print(st);
				out.print(' ');
			}
			out.print('\n');
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				out.print(board.getState(r,c));
				out.print(' ');
			}
			out.print('\n');
		}
		
		
	}
}
