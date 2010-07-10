package pencilbox.sudoku;

import java.util.StringTokenizer;

import pencilbox.common.io.PclReaderBase;


/**
 * 
 */
public class PclReader extends PclReaderBase {

	protected void readQuestion(String text) {
		Board board = (Board) getBoard();
		StringTokenizer t = new StringTokenizer(text);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				String n = t.nextToken();
				int num = 0;
				if (n.equals("."))
					num = 0;
				else
					num = Integer.parseInt(n);
				board.setNumber(r ,c, num);
			}
		}
	}
	protected void readAnswer(String text) {
		Board board = (Board) getBoard();
		StringTokenizer t = new StringTokenizer(text);
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				String n = t.nextToken();
				int num = 0;
				if (n.equals("."))
					num = 0;
				else
					num = Integer.parseInt(n);
				if (board.getNumber(r,c) == 0) {
					board.setState(r ,c, num);
				}
			}
		}
	}
}