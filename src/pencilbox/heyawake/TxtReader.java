package pencilbox.heyawake;

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
		StringTokenizer t;
		String t1, t2, t3, t4, t5;
		String line;
		Square newRoom;
		int rows = Integer.parseInt(reader.readLine());
		int cols = Integer.parseInt(reader.readLine());
		Board board = new Board();
		board.setSize(new Size(rows, cols));
		int nRoom = Integer.parseInt(reader.readLine());
		for(int n=0; n<nRoom; n++) {
			line = reader.readLine();
			t = new StringTokenizer(line);
			t1 = t.nextToken();
			t2 = t.nextToken();
			t3 = t.nextToken();
			t4 = t.nextToken();
			if (t.hasMoreTokens()) {
				t5 = t.nextToken();
//				board.roomList.add(
				newRoom = new Square(
						Integer.parseInt(t1),
						Integer.parseInt(t2),
						Integer.parseInt(t3),
						Integer.parseInt(t4),
						Integer.parseInt(t5));
			} else {
//				board.roomList.add(
				newRoom = new Square(
						Integer.parseInt(t1),
						Integer.parseInt(t2),
						Integer.parseInt(t3),
						Integer.parseInt(t4));
			}
			board.addSquare(newRoom);
		}
		int[][] field = board.getState();
		for (int i = 0; i < rows; i++) {
			line = reader.readLine();
			if(line==null || line.equals("")) break;
			int k = 0;
			int j = 0;
			while (k < line.length() && j < cols) {
				while (line.charAt(k) == ' '
					|| line.charAt(k) == ','
					|| line.charAt(k) == '\t')
					k++;
				if (line.charAt(k) == '.')
					field[i][j] = Board.UNKNOWN;
				else if (line.charAt(k) == '+')
					field[i][j] = Board.WHITE;
				else if (line.charAt(k) == '#')
					field[i][j] = Board.BLACK;
				else 
					field[i][j] = -9;
				k++;
				j++;
			}
		}
		return board;
	}


}
