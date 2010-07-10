package pencilbox.sudoku;


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
	/**
	 * ñ‚ëËÉfÅ[É^ÇÃÇ›ÇÃì«Ç›çûÇ›
	 */
	public BoardBase readProblem(Reader in)
		throws IOException {
			String str;
			int j;
			StringTokenizer t;
			BufferedReader reader = new BufferedReader(in);
			String line = reader.readLine();
			int size = Integer.parseInt(line);
			Board board = new Board();
			board.setSize(new Size(size, size));
			for (int i = 0; i < size; i++) {
				j = 0;
				line = reader.readLine();
				if (line == null)
					break;
				t = new StringTokenizer(line);
				while (t.hasMoreTokens()) {
					str = t.nextToken();
					if (str.equals(".")) {
						 board.setNumber(i, j, Board.BLANK);
					} else {
						int n = Integer.parseInt(str);
						if (n == 0){
							board.setNumber(i, j, Board.UNDETERMINED);
						} else {
							board.setNumber(i, j, n);
						}
					}
					j++;
				}
			}
			for (int i = 0; i < size; i++) {
				j = 0;
				line = reader.readLine();
				if (line == null)
					break;
				t = new StringTokenizer(line);
				while (t.hasMoreTokens()) {
					str = t.nextToken();
					if (str.equals(".")) {
					}
					else {
						board.setState(i,j,Integer.parseInt(str));
					}
					j++;
				}
			}
		return board;
	}

}
