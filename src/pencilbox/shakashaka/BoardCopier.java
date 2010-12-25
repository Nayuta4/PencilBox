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
//					System.out.print(s.toString() + " : " + st+ " -> ");
					st = rotateDirection(st, rotation);
//					System.out.println(" -> " + st);
				}
				board.changeState(d, st);
				board.changeNumber(d, srcBoard.getNumber(s));
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.changeNumber(s, Board.BLANK);
			board.changeState(s, Board.UNKNOWN);
		}
	}

	public static int rotateDirection(int direction, int rotation) {
		if (Board.isTriangle(direction)) {
			switch (rotation) {
				case 0 :
				case 1 :
				case 2 :
				case 3 :
					direction = (direction + rotation) % 4 + 4;
					break;
				case 4 :
				case 5 :
				case 6 :
				case 7 :
					direction = (direction + rotation) % 4 + 4;
					if ((direction&1)==1)
						direction = (direction^2);
					break;
			}
			return direction;
		} else {
			return direction;
		}
	}
	
}