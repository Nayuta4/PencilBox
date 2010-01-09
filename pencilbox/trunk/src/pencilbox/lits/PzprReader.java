package pencilbox.lits;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * 参考：pzprv3 lits.js
 */
public class PzprReader extends PzprReaderBase {
	
	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows, cols);
		return bd;
	}

	protected void pzlimport(){
		this.decodeBorder();
		makeAreaIDsFromBorders();
		makeAreas();
	}
	
	/**
	 * 境界線データから領域番号データを作成する。
	 */
	private void makeAreas() {
		Area[] areaArray = new pencilbox.lits.Area[nArea];
		for (int k=0; k<nArea; k++) {
			areaArray[k] = new Area();
			bd.addArea(areaArray[k]);
		}
		for (int i = 0; i < bd.rows()*bd.cols(); i++) {
			int k = areaIds[i]-1;
			areaArray[k].add(i2a(i));
			bd.setArea(i2a(i),areaArray[k]);
		}
	}

}
