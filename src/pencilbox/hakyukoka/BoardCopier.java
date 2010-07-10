package pencilbox.hakyukoka;

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
		Area srcArea = null;
		Area dstArea = null;
		for (Address s : region) {
			Address d = translateAndRotateAddress(s, from, to, rotation);
			if (board.isOn(d)) {
				board.setState(d, srcBoard.getState(s));
				board.setNumber(d, srcBoard.getNumber(s));
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
	}

	public void eraseRegion(BoardBase boardBase, pencilbox.common.core.Area region) {
		Board board = (Board) boardBase;
		for (Address s : region) {
			board.setState(s, Board.UNKNOWN);
			board.setNumber(s, Board.BLANK);
			Area srcArea = board.getArea(s);
			if (srcArea != null) {
				if (!region.containsAll(srcArea)) // ブロック全体が選択された場合に消去する。
					continue;
				board.removeArea(srcArea);
			}
		}
	}

}
