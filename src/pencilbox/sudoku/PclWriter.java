package pencilbox.sudoku;

import pencilbox.common.io.PclWriterBase;

/**
 *
 */
public class PclWriter extends PclWriterBase {

	protected String makeQuestionText() {
		Board board = (Board) getBoard();
		StringBuffer sb = new StringBuffer();
		sb.append('\n');
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.isStable(r, c) ? board.getNumber(r, c) : 0;
				if (n > 0)
					sb.append(Integer.toString(n));
				else
					sb.append('.');
				sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	protected String makeAnswerText() {
		Board board = (Board) getBoard();
		StringBuffer sb = new StringBuffer();
		sb.append('\n');
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				int n = board.getNumber(r, c);
				if (n > 0)
					sb.append(Integer.toString(n));
				else
					sb.append('.');
				sb.append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}

