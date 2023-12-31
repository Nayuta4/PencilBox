package pencilbox.shakashaka;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				int st = srcBoard.getState(s);
				if (Board.isTriangle(st)) {
					st = rotateDirection(st, rotation);
				}
				board.changeNumber(d, srcBoard.getNumber(s));
				board.changeState(d, st);
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.changeState(s, Board.UNKNOWN);
			board.changeNumber(s, Board.BLANK);
		}
	}

	/**
	 * Î‚ß‚S•ûŒü‚ğ‰ñ“]‚·‚é
	 * @param direction •ûŒü
	 * @param rotation ‰ñ“]”Ô†
	 * @return ‰ñ“]Œã‚Ì”Ô†
	 */
	public static int rotateDirection(int direction, int rotation) {
		switch (rotation) {
			case 0 :
			case 1 :
			case 2 :
			case 3 :
				direction = (direction + rotation) % 4 + 4; // ”Ô†‚ğ1‚Â‚¸‚ç‚·
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				direction = (direction + rotation) % 4 + 4;  // ”Ô†‚ğ1‚Â‚¸‚ç‚µ‚Ä
				if ((direction&1)==1)                        // 5(LD)‚Æ7(RU)‚ğ“ü‚ê‘Ö‚¦‚é
					direction = (direction^2);
				break;
		}
		return direction;
	}

}