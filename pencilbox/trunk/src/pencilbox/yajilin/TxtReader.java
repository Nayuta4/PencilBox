package pencilbox.yajilin;

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
					board.setNumber(i,j,Board.UNKNOWN);
				else if (str.equals("#"))
					board.setNumber(i,j,Board.BLACK);
				else if (str.equals("+"))
					board.setNumber(i,j,Board.WHITE);
				else {
					int arrow = Integer.parseInt(str);
					board.setNumber(i,j,arrow);
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
				board.setState(Direction.VERT, i, j, Integer.parseInt(str));
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
				board.setState(Direction.HORIZ, i, j, Integer.parseInt(str));
				j++;
			}
		}
	
		return board;
	}

}
