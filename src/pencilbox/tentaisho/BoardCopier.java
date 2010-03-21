package pencilbox.tentaisho;

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
		for (Area srcArea : s.getAreaList()) {
			Area a = new Area();
			rotator.rotateArea(srcArea, a);
			if (d.isAreaOn(a)) {
				d.addArea(a);
			}
		}
		Rotator rotator2 = new Rotator(s.rows()*2-1, s.cols()*2-1, n);
		rotator2.rotateArrayInt2(s.getStar(), d.getStar());
	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.Area region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		ArrayList<Area> srcAreaList = new ArrayList<Area>();
		Area srcArea = null;
		Area dstArea = null;
		Address d = Address.address();
		Rotator2 rotator = new Rotator2(to, rotation);
		for (Address s : region) {
			d.set(s.r() + to.r() - from.r(), s.c() + to.c() - from.c());
			d.set(rotator.rotateAddress(d));
			if (board.isOn(d)) {
				srcArea = srcBoard.getArea(s);
				if (srcArea != null) {
					if (srcAreaList.contains(srcArea))
						continue;
					if (! region.containsAll(srcArea)) // ブロック全体が選択された場合に移動する。
						continue;
					srcAreaList.add(srcArea);
					dstArea = new Area();
					for (Address ss : srcArea) {
						if (region.contains(ss)) {
							Address dd = Address.address(ss.r() + to.r() - from.r(), ss.c() + to.c() - from.c());
							dd.set(rotator.rotateAddress(dd));
							if (board.isOn(dd))
								dstArea.add(dd);
						}
					}
					if (dstArea.size() < srcArea.size()) // ブロック全体移動のとき，移動先でブロックが盤外にはみ出る場合はブロックを消去する
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
		pencilbox.common.core.Area region2 = makeStarRegion(region);
		Address dd = Address.address();
		Rotator2 rotator2 = new Rotator2(to.r()*2, to.c()*2, rotation);
		for (Address ss : region2) {
			dd.set(ss.r() + to.r()*2 - from.r()*2, ss.c() + to.c()*2 - from.c()*2);
			dd.set(rotator2.rotateAddress(dd));
			if (board.isOnStar(dd.r(), dd.c()))
				board.setStar(dd.r(), dd.c(), srcBoard.getStar(ss.r(), ss.c()));
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			Area srcArea = board.getArea(s);
			if (srcArea != null) {
				if (! region.containsAll(srcArea)) // ブロック全体が選択された場合に消去する。
					continue;
				board.removeCellFromArea(s, srcArea);
			}
		}
		pencilbox.common.core.Area region2 = makeStarRegion(region);
		for (Address ss : region2) {
			board.setStar(ss.r(), ss.c(), Board.NOSTAR);
		}
	}
	
	private pencilbox.common.core.Area makeStarRegion(pencilbox.common.core.Area region) {
		pencilbox.common.core.Area region2 = new pencilbox.common.core.Area();
		Address p = Address.address();
		for (Address s : region) {
			region2.add(Address.address(s.r()*2, s.c()*2));
			p.set(s.r(), s.c()+1);
			if (region.contains(p))
				region2.add(Address.address(s.r()*2, s.c()*2+1));
			p.set(s.r()+1, s.c());
			if (region.contains(p))
				region2.add(Address.address(s.r()*2+1, s.c()*2));
			p.set(s.r()+1, s.c()+1);
			if (region.contains(p))
				region2.add(Address.address(s.r()*2+1, s.c()*2+1));
		}
		return region2;
	}

}
