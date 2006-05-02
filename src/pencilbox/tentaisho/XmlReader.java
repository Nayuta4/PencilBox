package pencilbox.tentaisho;

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
	protected void setBN(int r, int c, int n) {
		board.setStar(r, c, n);
	}
	protected void start() {
		if (node=="areas") {
			int nArea = Integer.parseInt(attributes.getValue("N"));
			areas = new Area[nArea]; 
			for (int i=0; i<nArea; i++) areas[i] = new Area();
		}
	}
	protected void setAN(int r, int c, int n) {
		areas[n].add(r, c);
		board.setArea(r,c,areas[n]);
	}
	protected void onAnswerEnd() {
		super.onAnswerEnd();
		for (int i = 0; i < areas.length; i++) {
			board.getAreaList().add(areas[i]);
		}
	}
}
