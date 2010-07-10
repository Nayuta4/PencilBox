package pencilbox.hakyukoka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
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
		line = reader.readLine();
		int nArea = Integer.parseInt(line);
		Area[] areaArray = new Area[nArea];
		for (int k=0; k<nArea; k++) {
			areaArray[k] = new Area();
		}
		for (int i = 0; i < row; i++) {
			j = 0;
			line = reader.readLine();
			if (line == null)
				break;
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				int k = Integer.parseInt(str);
				if(k>=0) { 
					areaArray[k].add(i,j);
					board.setArea(i,j,areaArray[k]);
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
					board.setState(i,j,Integer.parseInt(str));
				}
				j++;
			}
		}
		board.getAreaList().addAll(Arrays.asList(areaArray));
		return board;
	}
  


}
