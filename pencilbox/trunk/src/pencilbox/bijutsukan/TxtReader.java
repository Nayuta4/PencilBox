package pencilbox.bijutsukan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Size;
import pencilbox.common.io.TxtReaderBase;


/**
 * 「美術館」txt形式ファイル読み込みクラス
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
				if (str.equals("."))      board.setState(i,j,Board.UNKNOWN);
				else if (str.equals("#")) board.setState(i,j,Board.NONUMBER_WALL);
				else if (str.equals("0")) board.setState(i,j,0);
				else if (str.equals("1")) board.setState(i,j,1);
				else if (str.equals("2")) board.setState(i,j,2);
				else if (str.equals("3")) board.setState(i,j,3);
				else if (str.equals("4")) board.setState(i,j,4);
				else if (str.equals("5")) board.setState(i,j,Board.NONUMBER_WALL);
				else if (str.equals("+")) board.setState(i,j,Board.BULB);
				else if (str.equals("*")) board.setState(i,j,Board.NOBULB);
				else ;
				j++;
			}
		}
		return board;
	}

}
