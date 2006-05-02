package pencilbox.masyu;

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
				st = board.getPearl(r, c);
				if (st == Board.NO_PEARL)
					out.print('.');
				else if (st == Board.BLACK_PEARL || st == Board.WHITE_PEARL || st == Board.GRAY_PEARL)
					out.print(st);
				else
					out.print('?');
				out.print(' ');
			}
			out.print('\n');
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols() - 1; c++) {
				out.print(board.getState(Board.VERT, r, c));
				out.print(' ');
			}
			out.print('\n');
		}
		for (int r = 0; r < board.rows()-1; r++) {
			for (int c = 0; c < board.cols(); c++) {
				out.print(board.getState(Board.HORIZ, r, c));
				out.print(' ');
			}
			out.print('\n');
		}
		
	}
}
