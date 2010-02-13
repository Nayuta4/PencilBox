package pencilbox.lits;

import java.util.ArrayList;

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
		Board s = (Board) src;
		Board d = (Board) dst;
		Rotator rotator = new Rotator(src.getSize(), n);
		rotator.rotateArrayInt2(s.getState(), d.getState());
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
		Area srcArea = null;
		Area dstArea = null;
		Address d = new Address();
		Rotator2 rotator = new Rotator2(to, rotation);
		for (Address s : region) {
			d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
			d.set(rotator.rotateAddress(d));
			if (board.isOn(d)) {
				board.setState(d, srcBoard.getState(s));
				srcArea = srcBoard.getArea(s);
				if (srcArea != null) {
					if (srcAreaList.contains(srcArea))
						continue;
					if (! region.containsAll(srcArea)) // �u���b�N�S�̂��I�����ꂽ�ꍇ�Ɉړ�����B
						continue;
					srcAreaList.add(srcArea);
					dstArea = new Area();
					for (Address ss : srcArea) {
						if (region.contains(ss)) {
							Address dd = new Address(ss.r() + to.r() - from.r(), ss.c() + to.c() - from.c());
							dd.set(rotator.rotateAddress(dd));
							if (board.isOn(dd))
								dstArea.add(dd);
						}
					}
					if (dstArea.size() < srcArea.size()) // �u���b�N�S�̈ړ��̂Ƃ��C�ړ���Ńu���b�N���ՊO�ɂ͂ݏo��ꍇ�̓u���b�N����������
						continue;
					for (Address dd : dstArea) {
						if (board.getArea(dd) != null) {
							board.removeArea(board.getArea(dd));
//							board.removeCellFromArea(dd, board.getArea(dd));
						}
					}
					board.addArea(dstArea);
				}
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.setState(s, Board.UNKNOWN);
			Area srcArea = board.getArea(s);
			if (srcArea != null) {
				if (! region.containsAll(srcArea)) // �u���b�N�S�̂��I�����ꂽ�ꍇ�ɏ�������B
					continue;
			board.removeCellFromArea(s, srcArea);
			}
		}
	}
}