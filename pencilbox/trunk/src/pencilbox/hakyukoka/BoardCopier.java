package pencilbox.hakyukoka;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator = new Rotator(src.getSize(), n);
		rotator.rotateArrayInt2(s.getState(), d.getState());
		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
		for (Area srcArea : s.getAreaList()) {
			Area a = new Area();
			rotator.rotateArea(srcArea, a);
			if (d.isAreaOn(a)) {
				d.addArea(a);
			}
		}
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		ArrayList<Area> srcAreaList = new ArrayList<Area>();
		label:
		for (Address s : region) {
			Area srcArea = srcBoard.getArea(s);
			if (srcArea != null) {
				if (srcAreaList.contains(srcArea)) // ��ƍς�
					continue;
				srcAreaList.add(srcArea); //�@��ƍς݃��X�g�ɋL�^����
				if (! region.containsAll(srcArea)) // �̈�S�̂��܂܂�Ȃ��ꍇ�͂Ƃ΂�
					continue;
				Area dstArea = new Area();
				for (Address ss : srcArea) {
					Address dd = translateAndRotateAddress(ss, from, to, rotation);
					if (! board.isOn(dd)) {
						continue label; // �ړ��悪�ՊO�ɂ͂ݏo��ꍇ�͒��~
					}
				}
				for (Address ss : srcArea) {
					Address dd = translateAndRotateAddress(ss, from, to, rotation);
					if (board.isOn(dd)) {
						Area oldArea = board.getArea(dd);
						if (oldArea != null) { // �ړ���Ɋ����̗̈悪��������C�̈�S�̂���������B
							board.removeWholeArea(oldArea);
						}
						board.addCellToArea(dd, dstArea);
					}
				}
			}
		}
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
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			if (board.isStable(s)) {
				board.changeFixedNumber(s, Board.BLANK);
			} else {
				board.changeAnswerNumber(s, Board.BLANK);
			}
		}
		for (Address s : region) {
			Area srcArea = board.getArea(s);
			if (srcArea != null) {
				if (! region.containsAll(srcArea)) // �u���b�N�S�̂��I�����ꂽ�ꍇ�ɏ�������B
					continue;
				board.removeWholeArea(srcArea); // �̈悲�Ə�������B
			}
		}
	}
}
