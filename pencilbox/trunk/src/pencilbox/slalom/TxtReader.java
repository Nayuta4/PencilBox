package pencilbox.slalom;

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

	public BoardBase readProblem(Reader in) throws IOException{
		BufferedReader reader = new BufferedReader(in);
		String line;
		String str;
		StringTokenizer t;
		int j;
		line = reader.readLine();
		int rows = Integer.parseInt(line);
		line = reader.readLine();
		int cols = Integer.parseInt(line);
		Board board = new Board();
		board.setSize(new Size(rows, cols));
		for (int i = 0; i < rows; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				if (str.equals("."))
					board.setNumber(i, j, Board.BLANK);
				else if (str.equals("+"))
					board.setNumber(i, j, Board.GOAL);
				else if (str.equals("-"))
					board.setNumber(i, j, Board.GATE_HORIZ);
				else if (str.equals("|"))
					board.setNumber(i, j, Board.GATE_VERT);
				else {
					int number = Integer.parseInt(str);
					board.setNumber(i, j, number);
				}
				j++;
			}
		}
		for (int i = 0; i < rows; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				board.changeState(Direction.VERT, i, j, Integer.parseInt(str));
				j++;
			}
		}
		for (int i = 0; i < rows-1; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				board.changeState(Direction.HORIZ, i, j, Integer.parseInt(str));
				j++;
			}
		}
		
		return board;
	}

}
