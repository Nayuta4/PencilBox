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
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
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
							Address dd = translateAndRotateAddress(ss, from, to, rotation);
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
		for (Address ss : region2) {
			Address dd = translateAndRotateStarAddress(ss, from, to, rotation);
			if (board.isOnStar(dd))
				board.setStar(dd, srcBoard.getStar(ss));
		}
	}

	private Address translateAndRotateStarAddress(Address ss, Address from, Address to, int rotation) {
		Address from2 = Address.address(from.r()*2, from.c()*2);
		Address to2 = Address.address(to.r()*2, to.c()*2);
		return Rotator2.translateAndRotateAddress(ss, from2, to2, rotation);
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
			board.setStar(ss, Board.NOSTAR);
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
