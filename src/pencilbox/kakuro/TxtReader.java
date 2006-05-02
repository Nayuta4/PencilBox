package pencilbox.kakuro;


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
		public BoardBase readProblem(Reader in) throws IOException{
			BufferedReader reader = new BufferedReader(in);
			StringTokenizer t;
			int t1, t2, t3, t4;
			String line;
			String str;
			int j = 0;
			int rows = Integer.parseInt(reader.readLine());
			int cols = Integer.parseInt(reader.readLine());
			Board board = new Board();
			board.setSize(new Size(rows, cols));
			while( (line = reader.readLine()) != null) {
				if(line.equals("")) break;
				t = new StringTokenizer(line);
				t1 = Integer.parseInt(t.nextToken());
				t2 = Integer.parseInt(t.nextToken());
				t3 = Integer.parseInt(t.nextToken());
				t4 = Integer.parseInt(t.nextToken());
				board.setWall(t1,t2,t3,t4);
			}
			for (int i = 0; i < rows; i++) {
				line = reader.readLine();
				j = 0;
				if (line == null)
					break;
				t = new StringTokenizer(line);
	//			while (j < cols) {
				while (t.hasMoreTokens()) {
					str = t.nextToken();
					if (!board.isWall(i,j)) {
						if (str.equals(".")) {
							 board.setNumber(i,j,0);
						} 
						else {
							board.setNumber(i,j,Integer.parseInt(str));
						}
					}
					j++;
				}
			}
			return board;
		}

}
