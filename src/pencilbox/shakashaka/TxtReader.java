package pencilbox.shakashaka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Size;
import pencilbox.common.io.TxtReaderBase;


/**
 * 「シャカシャカ」txt形式ファイル読み込みクラス
 */
public class TxtReader extends TxtReaderBase {

	public BoardBase readProblem(Reader in) throws IOException{
		BufferedReader reader = new BufferedReader(in);
		String line;
		String str;
//		StringTokenizer t;
		String[] t;
		int j;
		line = reader.readLine();
		if (line.equals("pzprv3"))
			line = reader.readLine();
		if (line.equals("shakashaka"))
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
//			t = new StringTokenizer(line);
			t = line.split("[ \t]");

//			while (t.hasMoreTokens()) {
//			str = t.nextToken();
			for (int s = 0; s < t.length; s++) {
			str = t[s];
				if (str.equals("."))      board.setNumber(i,j,Board.BLANK);
				else if (str.equals("0")) board.setNumber(i,j,0);
				else if (str.equals("1")) board.setNumber(i,j,1);
				else if (str.equals("2")) board.setNumber(i,j,2);
				else if (str.equals("3")) board.setNumber(i,j,3);
				else if (str.equals("4")) board.setNumber(i,j,4);
				else if (str.equals("5")) board.setNumber(i,j,Board.NONUMBER_WALL);
				else ;
				j++;
			}
		}
		for (int i = 0; i < rows; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = line.split("[ \t]");
			for (int s = 0; s < t.length; s++) {
				str = t[s];
				if (str.equals("."))      board.setState(i,j,Board.UNKNOWN);
				else if (str.equals("+")) board.setState(i,j,Board.WHITE);
				else if (str.equals("2")) board.setState(i,j,Board.LTDN);
				else if (str.equals("3")) board.setState(i,j,Board.RTDN);
				else if (str.equals("4")) board.setState(i,j,Board.RTUP);
				else if (str.equals("5")) board.setState(i,j,Board.LTUP);
				j++;
			}
		}
		return board;
	}

}
