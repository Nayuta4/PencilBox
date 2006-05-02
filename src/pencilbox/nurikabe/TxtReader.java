package pencilbox.nurikabe;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Size;
import pencilbox.common.io.TxtReaderBase;


/**
 * 
 */
public class TxtReader extends TxtReaderBase {

	public BoardBase readProblem(Reader in) throws IOException {
		BufferedReader reader = new BufferedReader(in);
		String line = reader.readLine();
		int row = Integer.parseInt(line);
		line = reader.readLine();
		int col = Integer.parseInt(line);
		Board board = new Board();
		board.setSize(new Size(row, col));
		for(int i = 0; i < row; i++) {
			line = reader.readLine();
			int k=0;
			int j=0;
			while (k < line.length() && j < col) {
				while (line.charAt(k) == ' ' || line.charAt(k) == ',' || line.charAt(k) == '\t')
					k++;
				if (line.charAt(k) == '.')
					board.setState(i,j,Board.UNKNOWN);
				else if (line.charAt(k) == '+')
					board.setState(i,j,Board.SPACE);
				else if (line.charAt(k) == '#')
					board.setState(i,j,Board.WALL);
				else if (line.charAt(k) == '?')
					board.setState(i,j,Board.UNDECIDED_NUMBER);
				else if (line.charAt(k) > '0' && line.charAt(k) <= '9') {
					if(k == line.length()-1){
						board.setState(i,j,Integer.parseInt(line.substring(k,k+1)));
					}else {
						if(line.charAt(k+1) >= '0' && line.charAt(k+1) <= '9') {
							board.setState(i,j,Integer.parseInt(line.substring(k,k+2)));
							k++;
						} else {
							board.setState(i,j,Integer.parseInt(line.substring(k,k+1)));
						}
					}
				} else {
					board.setState(i,j,-9);
				}
				k++;
				j++;
			}
		}
		return board;
	}

}
