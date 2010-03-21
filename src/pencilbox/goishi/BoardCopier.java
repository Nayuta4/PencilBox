package pencilbox.goishi;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator;
import pencilbox.common.core.Rotator2;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board srcBoard = (Board) src;
		Board board = (Board) dst;
		Rotator rotator = new Rotator(src.getSize(), n);
		Address s = Address.address();
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				s.set(r, c);
				Address d = rotator.rotateAddress(s);
				if (board.isOn(d)) {
					board.setState(d, srcBoard.getState(s));
				}
			}
		}
		for (int i = 0; i < srcBoard.pickedList.size(); i++) {
			board.pickedList.add(rotator.rotateAddress(srcBoard.pickedList.get(i)));
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		Address d = Address.address();
		Rotator2 rotator = new Rotator2(to, rotation);
		for (Address s : region) {
			d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
			d = rotator.rotateAddress(d);
			if (board.isOn(d)) {
				board.setState(d, srcBoard.getState(s));
			}
		}
		// Œ³—Ìˆæ‚ÌE‚Á‚½Î‚Í‚¢‚Á‚½‚ñ–ß‚·
		for (int i = board.pickedList.size()-1; i >= 0; i--) {
			Address s = board.pickedList.get(i);
			if (region.contains(s)) {
				board.pickedList.remove(i);
			}
		}
		// ‚»‚Ìó‘Ô‚Å‚¢‚Á‚½‚ñ”Õ–ÊXV
		board.rePickUpAll();
//		System.out.println("intermidiate size is " + board.pickedList.size());
		// E‚Á‚½Î‚ğˆÚ“®‚·‚é
		for (int i = 0; i < srcBoard.pickedList.size(); i++) {
			Address s = srcBoard.pickedList.get(i);
			if (region.contains(s)) {
				d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
				d = rotator.rotateAddress(d);
//				System.out.print(s.toString() + " moves to " + d.toString() + ", ");
				if (board.isOn(d)) {
					board.pickUp(d);
				} 
			}
		}
//		System.out.println();
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.setState(s, Board.BLANK);
		}
	}

}
