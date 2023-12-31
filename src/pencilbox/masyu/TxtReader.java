package pencilbox.masyu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.core.Size;
import pencilbox.common.io.TxtReaderBase;


/**
 * 
 */
public class TxtReader extends TxtReaderBase {
	/*
	 * 問題データのみの読み込み
	 */
	public BoardBase readProblem(Reader in)
		throws IOException {
		BufferedReader reader = new BufferedReader(in);
		String line;
		String str;
		StringTokenizer t;
		int j;
		line = reader.readLine();
		int row = Integer.parseInt(line);
		line = reader.readLine();
		int col = Integer.parseInt(line);
		Board board = new Board();
		board.setSize(new Size(row, col));
		for (int i = 0; i < row; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				if (str.equals("."))
					board.setNumber(i, j, Board.BLANK);
				else if (str.equals("1"))
					board.setNumber(i, j, Board.WHITE_PEARL);
				else if (str.equals("2"))
					board.setNumber(i, j, Board.BLACK_PEARL);
				else if (str.equals("3"))
					board.setNumber(i, j, Board.GRAY_PEARL);
				j++;
			}
		}
		for (int i = 0; i < row; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				board.setState(Direction.VERT, i, j, Integer.parseInt(str));
				j++;
			}
		}
		for (int i = 0; i < row - 1; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				board.setState(Direction.HORIZ, i, j, Integer.parseInt(str));
				j++;
			}
		}

		return board;
	}

}
