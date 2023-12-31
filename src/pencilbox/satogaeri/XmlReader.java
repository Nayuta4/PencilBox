package pencilbox.satogaeri;

import java.util.Arrays;

import pencilbox.common.core.Address;
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
	protected void onNumber() {
		int r = Integer.parseInt(attributes.getValue("r"))-1;
		int c = Integer.parseInt(attributes.getValue("c"))-1;
		int n = Integer.parseInt(attributes.getValue("n"));
		if (n == Board.UNDETERMINED) {
			board.setNumber(r,c,Board.UNDETERMINED);
		} else {
			board.setNumber(r,c,n);
		}
	}
	protected void onBoardEnd() {
		super.onBoardEnd();
		board.getAreaList().addAll(Arrays.asList(areas));
	}
	protected void setAN(int r, int c, int t) {
		if (t != Board.NOROUTE) {
			board.setRoute(Address.address(r, c), t);
		}
	}
}
