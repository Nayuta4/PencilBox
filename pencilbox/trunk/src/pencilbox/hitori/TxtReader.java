package pencilbox.hitori;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import pencilbox.common.core.BoardBase;
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
		int row = Integer.parseInt(line);
		line = reader.readLine();
		int col = Integer.parseInt(line);
		//  		board = new Board(row, col);
		Board board = new Board();
		board.setSize(new Size(row, col));
		for (int i = 0; i < row; i++) {
			j = 0;
			line = reader.readLine();
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				board.setNumber(i,j,Integer.parseInt(t.nextToken()));
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
				if (str.equals("."))
					board.setState(i,j,Board.UNKNOWN);
				else if (str.equals("+"))
					board.setState(i,j,Board.WHITE);
				else if (str.equals("#"))
					board.setState(i,j,Board.BLACK);
				else
					board.setState(i,j,Board.UNKNOWN);
				j++;
			}
		}
		return board;
	}



}
