package pencilbox.shikaku;

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
					board.setNumber(i, j, 0);
				else
					board.setNumber(i, j, Integer.parseInt(str));
				j++;
			}
		}
		line = reader.readLine();
		if (line == null || line.equals(""))
			return board;
		
		int nArea = Integer.parseInt(line);
	
		for (int i = 0; i < nArea; i++) {
			line = reader.readLine();
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				Square sq = new Square(
					Integer.parseInt(t.nextToken()),
					Integer.parseInt(t.nextToken()),
					Integer.parseInt(t.nextToken()),
					Integer.parseInt(t.nextToken()));
				board.setSquare(sq, sq);
				board.getSquareList().add(sq);
			}
		}
		return board;
	}

}
