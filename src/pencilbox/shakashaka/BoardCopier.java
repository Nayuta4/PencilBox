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
	 * �΂߂S��������]����
	 * @param direction ����
	 * @param rotation ��]�ԍ�
	 * @return ��]��̔ԍ�
	 */
	public static int rotateDirection(int direction, int rotation) {
		switch (rotation) {
			case 0 :
			case 1 :
			case 2 :
			case 3 :
				direction = (direction + rotation) % 4 + 4; // �ԍ���1���炷
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				direction = (direction + rotation) % 4 + 4;  // �ԍ���1���炵��
				if ((direction&1)==1)                        // 5(LD)��7(RU)�����ւ���
					direction = (direction^2);
				break;
		}
		return direction;
	}

}