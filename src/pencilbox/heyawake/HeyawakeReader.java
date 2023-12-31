package pencilbox.heyawake;

import java.io.IOException;
import java.util.StringTokenizer;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Size;


/**
 * へわやけアプレット URL の読み込み
 * @author nayuta
 */
public class HeyawakeReader {

	public BoardBase readProblem(String line) throws IOException {
		StringTokenizer t = new StringTokenizer(line, "/ \t\n\r\f");
		String s = t.nextToken();
		int x = s.indexOf('x');
		int col = Integer.parseInt(s.substring(0, x));
		int row = Integer.parseInt(s.substring(x+1));
		int in;
		int num;
		Board board = new Board();
		board.setSize(new Size(row, col));
		while(t.hasMoreTokens()) {
			s = t.nextToken();
			in = s.indexOf("in");
			x = s.indexOf('x');
			if (in > 0) {
				num = Integer.parseInt(s.substring(0, in));
			} else {
				num = -1;
				in = -2;
			}
			col = Integer.parseInt(s.substring(in+2, x));
			row = Integer.parseInt(s.substring(x+1));
			addNewRoom(board, row, col, num);
		}
		return board;
	}

	private void addNewRoom(Board board, int row, int col, int num) {
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				if (board.getSquare(r, c) == null) {
					Square square = new Square(r, c, r+row-1, c+col-1, num);
					board.setSquare(square, square);
					board.getSquareList().add(square);
					return;
				}
			}
		}
	}

}
