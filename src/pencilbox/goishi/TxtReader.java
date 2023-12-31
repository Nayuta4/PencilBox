package pencilbox.goishi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Size;
import pencilbox.common.io.TxtReaderBase;


/**
 * 
 */
public class TxtReader extends TxtReaderBase {
	/*
	 * 問題データの読み込み
	 */
	public BoardBase readProblem(Reader in)
		throws IOException {
		BufferedReader reader = new BufferedReader(in);
		String line;
		String str;
		StringTokenizer t;
		int t1, t2, t3;
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
					board.setState(Address.address(i, j), Board.BLANK);
				} else if (str.equals("1")){
					board.setState(Address.address(i, j), Board.STONE);
				}
				j++;
			}
		}
		while(true) {
			line = reader.readLine();
			if (line == null || line.equals(""))
				break;
			t = new StringTokenizer(line);
			t1 = Integer.parseInt(t.nextToken());
			t2 = Integer.parseInt(t.nextToken());
			t3 = Integer.parseInt(t.nextToken());
			Address p = Address.address(t2, t3);
			board.pickedList.add(p);
			board.setNumber(p, board.pickedList.size());
		}
		return board;
	}
}
