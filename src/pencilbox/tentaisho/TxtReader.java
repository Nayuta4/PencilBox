package pencilbox.tentaisho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
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
		int j, k;
		line = reader.readLine();
		int row = Integer.parseInt(line);
		line = reader.readLine();
		int col = Integer.parseInt(line);
		Board board = new Board();
		board.setSize(new Size(row,col));

		for (int i = 0; i < row * 2 - 1; i++) {
			line = reader.readLine();
			if (line == null)
				break;
			k = 0;
			j = 0;
			while (k < line.length() && j < col * 2 - 1) {
				while (line.charAt(k) == ' '
					|| line.charAt(k) == ','
					|| line.charAt(k) == '\t')
					k++;
				if (line.charAt(k) == '.')
					board.setStar(i,j,Board.NOSTAR);
				else if (line.charAt(k) == '1')
					board.setStar(i,j,Board.WHITESTAR);
				else if (line.charAt(k) == '2')
					board.setStar(i,j,Board.BLACKSTAR);
				else
					board.setStar(i,j,-9);
				k++;
				j++;
			}
		}
		line = reader.readLine();
		if (line == null)	return board;

		int nArea = Integer.parseInt(line);
		Area[] areas = new Area[nArea];
		for (int n=0; n<nArea; n++) areas[n] = new Area();
		StringTokenizer t;
		String str;

		for (int i = 0; i < row; i++) {
			j = 0;
			line = reader.readLine();
			t = new StringTokenizer(line);
			while (t.hasMoreTokens()) {
				str = t.nextToken();
				if (str.equals(".")) ;
				else {
					int n = Integer.parseInt(str);
					areas[n].add(i, j);
					board.setArea(i,j,areas[n]);
				}
				j++;
			}
		}
		for (int i = 0; i < areas.length; i++) {
			board.getAreaList().add(areas[i]);
		}

		// 辺データの読み込み，なければここで終わり
		line = reader.readLine();
		if (line != null) {
			t = new StringTokenizer(line);
			for (SideAddress b : board.borderAddrs()) {
				if (t.hasMoreTokens()) {
					str = t.nextToken();
					board.setEdge(b, Integer.parseInt(str));
				}
			}
		}
		return board;
	}
}
