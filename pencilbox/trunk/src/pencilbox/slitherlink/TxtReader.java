package pencilbox.slitherlink;

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
		int rows = Integer.parseInt(line)+1;
		line = reader.readLine();
		int cols = Integer.parseInt(line)+1;
		Board board = new Board();
		board.setSize(new Size(rows, cols));
		for (int i = 0; i < rows-1; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				if (str.equals(".")) 	  board.setNumber(i,j,Board.NONUMBER);
				else if (str.equals("0")) board.setNumber(i,j,0);
				else if (str.equals("1")) board.setNumber(i,j,1);
				else if (str.equals("2")) board.setNumber(i,j,2);
				else if (str.equals("3")) board.setNumber(i,j,3);
				else if (str.equals("4")) board.setNumber(i,j,4);
				else if (str.equals("5")) board.setNumber(i,j,5);
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
