package pencilbox.masyu;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.Area;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator;
import pencilbox.common.core.Rotator2;
import pencilbox.common.core.SideAddress;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator = new Rotator(src.getSize(), n);
		rotator.rotateArrayInt3(s.getState(), d.getState());
		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase dstBoardBase, Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) dstBoardBase;
		ArrayList<SideAddress> list = region.innerBorders();
		for (SideAddress s : list) {
			SideAddress d = Rotator2.translateAndRotateSideAddress(s, from, to, rotation);
			if (board.isSideOn(d))
				board.setState(d, srcBoard.getState(s));
		}
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d))
				board.setNumber(d, srcBoard.getNumber(s));
		}
	}

	public void eraseRegion(BoardBase srcBoardBase, Area region) {
		Board board = (Board) srcBoardBase;
		ArrayList<SideAddress> list = region.innerBorders();
		for (SideAddress s : list) {
			board.setState(s, Board.UNKNOWN);
		}
		for (Address s : region) {
			board.setNumber(s, Board.BLANK);
		}
	}
}
