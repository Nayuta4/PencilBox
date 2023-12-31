package pencilbox.satogaeri;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.BoardCopierBase;
import pencilbox.common.core.Rotator2;

/**
 * 
 */
public class BoardCopier extends BoardCopierBase {

//	public void copyBoardStates(BoardBase src, BoardBase dst, int n) {
//		Board s = (Board) src;
//		Board d = (Board) dst;
//		Rotator rotator = new Rotator(src.getSize(), n);
//		rotator.rotateArrayInt2(s.getNumber(), d.getNumber());
//		for (Area srcArea : s.getAreaList()) {
//			Area a = new Area();
//			rotator.rotateArea(srcArea, a);
//			if (d.isAreaOn(a)) {
//				d.addArea(a);
//			}
//		}
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
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.changeFixedNumber(d, srcBoard.getNumber(s));
			}
		}
		// 線全体が含まれればコピーする
//		System.out.println(" コピー元領域は" + region);
		label:
		for (Address s : region) {
			if (srcBoard.hasNumber(s)) {
				ArrayList<Address> routeArea = srcBoard.getCellsOfWholeRoute(s);
//				System.out.println(s + " を含むルート全体は" + routeArea);
				for (Address ss : routeArea) {
//					System.out.println(ss + " は領域に含まれるか？");
					if (!region.contains(ss)) {
//						System.out.println("　　含まれないので中止");
						continue label; // 移動先が盤外にはみ出る場合は中止
					}
				}
				for (Address ss : routeArea) {
					Address dd = translateAndRotateAddress(ss, from, to, rotation);
					if (! board.isOn(dd)) {
						continue label; // 移動先が盤外にはみ出る場合は中止
					}
				}
				for (Address ss : routeArea) {
					Address dd = translateAndRotateAddress(ss, from, to, rotation);
					int st = srcBoard.getRoute(ss);
					if (st >= 0 && st <= 3) {
						st = Rotator2.rotateDirection(st, rotation);
					}
					board.changeRoute(dd, st);
				}
			}
		}
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.AreaBase region) {
		Board board = (Board) boardBase;
		// 数字が消えたら線を消す
		for (Address s : region) {
			if (board.hasNumber(s)) {
				board.eraseRoute(s);
			}
		}
		for (Address s : region) {
			board.changeFixedNumber(s, Board.BLANK);
		}
		for (Address s : region) {
			Area srcArea = board.getArea(s);
			if (srcArea != null) {
				if (! region.containsAll(srcArea)) // ブロック全体が選択された場合に消去する。
					continue;
				board.removeWholeArea(srcArea); // 領域ごと消去する。
			}
		}
	}
}
