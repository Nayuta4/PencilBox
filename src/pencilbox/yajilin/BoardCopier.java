package pencilbox.yajilin;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.AreaBase;
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
		for (Address pos0 : src.cellAddrs()) {
			Address pos = rotator.rotateAddress(pos0);
			if (!d.isOn(pos))
				continue;
			if (s.getNumber(pos0) >= 0) {
				d.setArrowNumber(pos, s.getArrowNumber(pos0));
				d.setArrowDirection(pos, rotator.rotateDirection(s.getArrowDirection(pos0)));
			} else {
				d.setNumber(pos, s.getNumber(pos0));
			}
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				int number = srcBoard.getNumber(s);
				if (number >= 0 || number == Board.UNDECIDED_NUMBER || number == Board.BLACK) {
					board.eraseLinesAround(d);
				}
				if (number >= 0) {
					int dir = Rotator2.rotateDirection(srcBoard.getArrowDirection(s), rotation);
					number = Board.getNumberValue(number, dir);
				}
				board.changeNumber(d, number);
			}
		}
		ArrayList<SideAddress> list = region.innerBorders(); {
			for (SideAddress s : list) {
				SideAddress d = Rotator2.translateAndRotateSideAddress(s, from, to, rotation);
				if (board.isSideOn(d))
					board.changeState(d, srcBoard.getState(s));
			}
		}
	}

	public void eraseRegion(BoardBase srcBoardBase, AreaBase region) {
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
