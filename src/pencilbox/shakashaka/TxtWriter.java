package pencilbox.shakashaka;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.TxtWriterBase;


/**
 * 「シャカシャカ」txt形式ファイル書き出しクラス
 */
public class TxtWriter extends TxtWriterBase {

	public void writeProblem(PrintWriter out, BoardBase puzzleBoard, int mode) {
		Board board = (Board) puzzleBoard;
		int st;
		out.println(board.rows());
		out.println(board.cols());
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				st = board.getNumber(r,c);
				if (st == Board.BLANK)
					out.print('.');
				else if (st >= 0 && st <= 4)
					out.print(st);
				else if (st == Board.NONUMBER_WALL)
					out.print(5);
				else 
					out.print('?');
				out.print(' ');
			}
			out.println();
		}
		if (mode == QUESTION_ONLY)
			return;
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				st = board.getState(r,c);
				if (st == Board.UNKNOWN)
					out.print('.');
				else if (st == Board.WHITE)
					out.print('+');
				else if (st == Board.LTDN)
					out.print('2');
				else if (st == Board.RTDN)
					out.print('3');
				else if (st == Board.RTUP)
					out.print('4');
				else if (st == Board.LTUP)
					out.print('5');
				out.print(' ');
			}
			out.println();
		}
	}
}
