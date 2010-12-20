package pencilbox.tentaisho;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator;
import pencilbox.common.core.Rotator2;
import pencilbox.common.core.SideAddress;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

//	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
//		Board s = (Board) src;
//		Board d = (Board) dst;
//		Rotator rotator = new Rotator(src.getSize(), n);
//		for (Area srcArea : s.getAreaList()) {
//			Area a = new Area();
//			rotator.rotateArea(srcArea, a);
//			if (d.isAreaOn(a)) {
//				d.addArea(a);
//			}
//		}
//		Rotator rotator2 = new Rotator(s.rows()*2-1, s.cols()*2-1, n);
//		rotator2.rotateArrayInt2(s.getStar(), d.getStar());
//	}

	public void copyRegion(BoardBase srcBoardBase, BoardBase boardBase, pencilbox.common.core.AreaBase region, Address from, Address to, int rotation) {
		Board srcBoard = (Board) srcBoardBase;
		Board board = (Board) boardBase;
		ArrayList<Area> srcAreaList = new ArrayList<Area>();
		label:
		for (Address s : region) {
			Area srcArea = srcBoard.getArea(s);
			if (srcArea != null) {
				if (srcAreaList.contains(srcArea)) // 作業済み
					continue;
				srcAreaList.add(srcArea); //　作業済みリストに記録する
				if (! region.containsAll(srcArea)) // 領域全体が含まれない場合はとばす
					continue;
				Area dstArea = new Area();
				for (Address ss : srcArea) {
					Address dd = translateAndRotateAddress(ss, from, to, rotation);
					if (! board.isOn(dd)) {
						continue label; // 移動先が盤外にはみ出る場合は中止
					}
				}
				for (Address ss : srcArea) {
					Address dd = translateAndRotateAddress(ss, from, to, rotation);
					if (board.isOn(dd)) {
						Area oldArea = board.getArea(dd);
						if (oldArea != null) { // 移動先に既存の領域があったら，領域全体を消去する。
							board.removeWholeArea(oldArea);
						}
						board.addCellToArea(dd, dstArea);
					}
				}
			}
		}
		pencilbox.common.core.AreaBase region2 = makeStarRegion(region);
		for (Address ss : region2) {
			Address dd = translateAndRotateStarAddress(ss, from, to, rotation);
			if (board.isOnStar(dd)) {
				board.changeStar(dd, srcBoard.getStar(ss));
			}
		}
		ArrayList<SideAddress> innerBorders = region.innerBorders();
		for (SideAddress s : innerBorders) {
			SideAddress d = Rotator2.translateAndRotateSideAddress(s, from, to, rotation);
			if (boardBase.isSideOn(d)) {
				board.changeEdge(d, srcBoard.getEdge(s));
			}
		}
	}

	private Address translateAndRotateStarAddress(Address ss, Address from, Address to, int rotation) {
		Address from2 = Address.address(from.r()*2, from.c()*2);
		Address to2 = Address.address(to.r()*2, to.c()*2);
		return Rotator2.translateAndRotateAddress(ss, from2, to2, rotation);
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			Area srcArea = board.getArea(s);
			if (srcArea != null) {
				if (! region.containsAll(srcArea)) // ブロック全体が選択された場合に消去する。
					continue;
				board.removeWholeArea(srcArea); // 領域ごと消去する。
			}
		}
		pencilbox.common.core.AreaBase region2 = makeStarRegion(region);
		for (Address ss : region2) {
			board.changeStar(ss, Board.NOSTAR);
		}
		ArrayList<SideAddress> innerBorders = region.innerBorders();
		for (SideAddress s : innerBorders) {
			board.changeEdge(s, Board.NOLINE);
		}
	}
	
	private pencilbox.common.core.AreaBase makeStarRegion(pencilbox.common.core.AreaBase region) {
		pencilbox.common.core.AreaBase region2 = new pencilbox.common.core.AreaBase();
		for (Address s : region) {
			region2.add(Address.address(s.r()*2, s.c()*2));
			if (region.contains(Address.address(s.r(), s.c()+1)))
				region2.add(Address.address(s.r()*2, s.c()*2+1));
			if (region.contains(Address.address(s.r()+1, s.c())))
				region2.add(Address.address(s.r()*2+1, s.c()*2));
			if (region.contains(Address.address(s.r()+1, s.c()+1)))
				region2.add(Address.address(s.r()*2+1, s.c()*2+1));
		}
		return region2;
	}
}
