package pencilbox.tentaisho;

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
		for (int r = 0; r < board.rows() * 2 - 1; r++) {
			for (int c = 0; c < board.cols() * 2 - 1; c++) {
				st = board.getStar(r, c);
				if (st == Board.NOSTAR)
					out.print('.');
				else if (st == Board.BLACKSTAR || st == Board.WHITESTAR)
					out.print(st);
				else
					out.print('?');
				//				writer.print(' ');
			}
			out.print('\n');
		}
		out.println(board.getAreaList().size());
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getArea(r,c) == null)
				out.print('.');
				else 
				out.print(board.getAreaList().indexOf(board.getArea(r,c)));
				out.print(' ');
			}
			out.print('\n');
		}
		
		
		
		
	}
}
