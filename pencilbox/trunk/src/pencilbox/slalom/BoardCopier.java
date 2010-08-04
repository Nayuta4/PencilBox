package pencilbox.slalom;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.Area;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator2;
import pencilbox.common.core.SideAddress;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				if (srcBoard.getNumber(s) == Board.GATE_HORIZ) {
					if (Rotator2.isTransposed(rotation)) {
						board.changeNumber(d, Board.GATE_VERT);
					} else {
						board.changeNumber(d, Board.GATE_HORIZ);
					}
				} else if (srcBoard.getNumber(s) == Board.GATE_VERT) {
					if (Rotator2.isTransposed(rotation)) {
						board.changeNumber(d, Board.GATE_HORIZ);
					} else {
						board.changeNumber(d, Board.GATE_VERT);
					}
				} else {
					board.changeNumber(d, srcBoard.getNumber(s));
				}
			}
		}
		ArrayList<SideAddress> list = region.innerBorders();
		for (SideAddress s : list) {
			SideAddress d = Rotator2.translateAndRotateSideAddress(s, from, to, rotation);
			if (board.isSideOn(d))
				board.changeState(d, srcBoard.getState(s));
		}
	}

	public void eraseRegion(BoardBase srcBoardBase, Area region) {
		Board board = (Board) srcBoardBase;
		ArrayList<SideAddress> list = region.innerBorders();
		for (SideAddress s : list) {
			board.changeState(s, Board.UNKNOWN);
		}
		for (Address s : region) {
			board.changeNumber(s, Board.BLANK);
		}
	}
}
