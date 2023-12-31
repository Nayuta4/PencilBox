package pencilbox.goishi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board srcBoard = (Board) src;
		Board board = (Board) dst;
		Rotator rotator = new Rotator(src.getSize(), n);
		for (Address s : src.cellAddrs()) {
			Address d = rotator.rotateAddress(s);
			if (board.isOn(d)) {
				board.setState(d, srcBoard.getState(s));
			}
		}
		for (int i = 0; i < srcBoard.pickedList.size(); i++) {
			Address d = rotator.rotateAddress(srcBoard.pickedList.get(i));
			board.pickedList.add(d);
			board.setNumber(d, i+1);
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.changeState(d, srcBoard.getState(s));
			}
		}
		// E‚Á‚½Î‚ðˆÚ“®‚·‚é
		for (int i = 0; i < srcBoard.pickedList.size(); i++) {
			Address s = srcBoard.pickedList.get(i);
			if (region.contains(s)) {
				Address d = translateAndRotateAddress(s, from, to, rotation);
//				System.out.print(s.toString() + " moves to " + d.toString() + ", ");
				if (board.isOn(d)) {
					if (board.canPick(d)) {
						board.pickUp(d);
					}
				}
			}
		}
//		System.out.println();
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.changeState(s, Board.BLANK);
		}
	}

}
