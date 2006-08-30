package pencilbox.heyawake;

import java.io.PrintWriter;
import java.util.Iterator;

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
			out.println(board.getSquareListSize());
			for(Iterator itr = board.getSquareListIterator(); itr.hasNext(); ){
				Square room = (Square) itr.next();
				out.print(room.r0);
				out.print(' ');
				out.print(room.c0);
				out.print(' ');
				out.print(room.r1);
				out.print(' ');
				out.print(room.c1);
				out.print(' ');
				if(room.getNumber()>=0){
					out.print(room.getNumber());
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
