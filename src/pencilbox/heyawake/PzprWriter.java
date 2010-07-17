package pencilbox.heyawake;

import java.util.ArrayList;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.io.PzprWriterBase;


/**
 * 参考：pzprv3 heyawake.js
 */
public class PzprWriter extends PzprWriterBase {
	
	private Board bd;

	protected String getPzprName() {
		return "heyawake";
	}

	protected void pzlexport(){
		this.bd = (Board)boardBase;
		outSize(bd.rows(), bd.cols());
		makeRoomNumberData();
		makeBorderData();
		this.encodeBorder();
		this.encodeRoomNumber16();
	}

	/**
	 * 部屋を整列し，部屋番号データを作成する。
	 * フィールド roomNumbers は親クラスにある。
	 */
	private void makeRoomNumberData() {
		int roomMax = 0;
		int[] roomId = new int[bd.rows()*bd.cols()];
		ArrayList<Square> rooms = new ArrayList<Square>();
		for(int cc=0;cc<bd.rows()*bd.cols();cc++){
			if (roomId[cc] == 0) {
				Square sq = bd.getSquare(i2a(cc));
				if (sq != null) {
					rooms.add(sq);
					roomMax ++;
					for (Address p : sq.cellSet()) {
						roomId[a2i(p)] = roomMax;
					}
				}
			}
		}
		roomNumbers = new int[rooms.size()];
		for (int i = 0; i < rooms.size(); i++) {
			roomNumbers[i] = rooms.get(i).getNumber();
		}
	}

	protected int getBorder(SideAddress p) {
		Address c1 = SideAddress.nextCellFromBorder(p, 0);
		Address c2 = SideAddress.nextCellFromBorder(p, 1);
		if (bd.getSquare(c1) != bd.getSquare(c2))
			return 1;
		else
			return 0;
	}

}
