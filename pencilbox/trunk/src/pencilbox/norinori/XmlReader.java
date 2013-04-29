package pencilbox.norinori;

import java.util.Arrays;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.XmlReaderBase;


/**
 * 
 */
public class XmlReader extends XmlReaderBase {
	private Board board;
	private Area[] areas;

	protected BoardBase makeBoard() {
		board = new Board();
		return board;
	}
	protected void start() {
		if (node=="areas") {
			int nArea = Integer.parseInt(attributes.getValue("N"));
			areas = new Area[nArea];
			for (int i=0; i<nArea; i++) areas[i] = new Area();
		}
	}
	protected void setBN(int r, int c, int n) {
		areas[n].add(r, c);
		board.setArea(r,c,areas[n]);
	}
	protected void onBoardEnd() {
		super.onBoardEnd();
		board.getAreaList().addAll(Arrays.asList(areas));
	}
	protected void setAType(int r, int c, String t) {
		if (t == WALL)
			board.setState(r, c, Board.BLACK);
		else if (t == SPACE)
			board.setState(r, c, Board.WHITE);
	}
}
