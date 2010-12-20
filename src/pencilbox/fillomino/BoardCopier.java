package pencilbox.fillomino;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator2;
import pencilbox.common.core.SideAddress;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

//	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
//		Board s = (Board) src;
//		Board d = (Board) dst;
//		Rotator rotator = new Rotator(src.getSize(), n);
//		rotator.rotateArrayInt2(s.getState(), d.getState());
//		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
//	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				if (srcBoard.isStable(s)) {
					board.changeFixedNumber(d, srcBoard.getNumber(s));
				} else {
					board.changeAnswerNumber(d, srcBoard.getState(s));
				}
			}
		}
		ArrayList<SideAddress> innerBorders = region.innerBorders();
		for (SideAddress s : innerBorders) {
			SideAddress d = Rotator2.translateAndRotateSideAddress(s, from, to, rotation);
			if (boardBase.isSideOn(d)) {
				board.changeEdge(d, srcBoard.getEdge(s));
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			if (board.isStable(s)) {
				board.changeFixedNumber(s, Board.BLANK);
			} else {
				board.changeAnswerNumber(s, Board.BLANK);
			}
		}
		ArrayList<SideAddress> innerBorders = region.innerBorders();
		for (SideAddress s : innerBorders) {
			board.changeEdge(s, Board.NOLINE);
		}
	}

}
