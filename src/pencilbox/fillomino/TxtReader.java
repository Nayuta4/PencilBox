package pencilbox.fillomino;

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
	/*
	 * ñ‚ëËÉfÅ[É^ÇÃì«Ç›çûÇ›
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
		for (int i = 0; i < row; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				if (str.equals(".")) {
				} else {
					board.setState(i, j, Integer.parseInt(str));
				}
				j++;
			}
		}
		return board;
	}
  


}
