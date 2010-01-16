package pencilbox.goishi;

import java.io.PrintWriter;

import pencilbox.common.core.Address;
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
				if (board.getState(r, c) == Board.STONE) {
					out.print(1);
				} else if (board.getState(r, c) == Board.BLANK){
					out.print('.');
				} else {
					out.print('?');
				}
				out.print(' ');
			}
			out.println();
		}
		if (mode == QUESTION_ONLY)
			return;
		for (int i = 0; i < board.pickedList.size(); i++) {
			Address p = board.pickedList.get(i);
			out.print(i+1);
			out.print(" ");
			out.print(p.r());
			out.print(" ");
			out.print(p.c());
			out.println();
		}
	}



}
