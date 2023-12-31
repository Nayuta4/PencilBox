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
	 * 斜め４方向を回転する
	 * @param direction 方向
	 * @param rotation 回転番号
	 * @return 回転後の番号
	 */
	public static int rotateDirection(int direction, int rotation) {
		switch (rotation) {
			case 0 :
			case 1 :
			case 2 :
			case 3 :
				direction = (direction + rotation) % 4 + 4; // 番号を1つずらす
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				direction = (direction + rotation) % 4 + 4;  // 番号を1つずらして
				if ((direction&1)==1)                        // 5(LD)と7(RU)を入れ替える
					direction = (direction^2);
				break;
		}
		return direction;
	}

}