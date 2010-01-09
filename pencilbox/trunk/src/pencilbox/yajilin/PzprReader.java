package pencilbox.yajilin;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 yajilin.js
 * 
 * 矢印のない数字は上向き矢印の数字に変更される
 * 矢印のある未定数字は矢印なしに変更される
 */
public class PzprReader extends PzprReaderBase {
	
	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		decodeArrowNumber16();
	}

	protected void sQnC(int id, int num) {
		Address a = i2a(id);
		if (num == -2) {
			bd.setNumber(a, Board.UNDECIDED_NUMBER);
		} else if (num >= 0) {
		  // 数字を入れるとなぜか矢印が上向きになるので、いったん向きを覚えてあとから再設定する。
			int d = bd.getArrowDirection(a);
			bd.setArrowNumber(a, num);
			bd.setArrowDirection(a, d);
		}
	}

	protected void sDiC(int id, int num) {
		Address a = i2a(id);
		int d=0;
		switch (num) {
			case 1: d=0;
			break;
			case 3: d=1;
			break;
			case 2: d=2;
			break;
			case 4: d=3;
			break;
		}
		bd.setNumber(a, 0); // 矢印の向きを決めるために仮に数字0を入れる。
		bd.setArrowDirection(a, d);
	}
}
