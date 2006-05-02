package pencilbox.bijutsukan;

import java.io.PrintWriter;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.TxtWriterBase;


/**
 * �u���p�فvtxt�`���t�@�C�������o���N���X
 */
public class TxtWriter extends TxtWriterBase {

	public void writeProblem(PrintWriter out, BoardBase puzzleBoard) {
		Board board = (Board) puzzleBoard;
		int st;
		out.println(board.rows());
		out.println(board.cols());
		for(int r=0; r<board.rows(); r++){
			for(int c=0; c<board.cols(); c++){
				st = board.getState(r,c);
				if (st == Board.NONUMBER_WALL)    out.print('#');
				else if (st >= 0 && st <= 4)  out.print(st);
				else if (st == Board.UNKNOWN) out.print('.');
				else if (st == Board.ILLUMINATION)    out.print('+');
				else if (st == Board.NOILLUMINATION)    out.print('*');
				out.print(' ');
			}
			out.print('\n');
		}
	}
}
