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
		if (checkpflag("d")) { // ぱずぷれ旧形式
			decodeLITS_old();
			makeAreaIDsFromBorders();
			makeAreas();
		} else {
			this.decodeBorder();
			makeAreaIDsFromBorders();
			makeAreas();
		}
	}

	/**
	 * 境界線データから領域番号データを作成する。
	 */
	private void makeAreas() {
		Area[] areaArray = new pencilbox.lits.Area[nArea];
		for (int k=0; k<nArea; k++) {
			areaArray[k] = new Area();
			bd.getAreaList().add(areaArray[k]);
		}
		for (int i = 0; i < bd.rows()*bd.cols(); i++) {
			int k = areaIds[i]-1;
			areaArray[k].add(i2a(i));
			bd.setArea(i2a(i),areaArray[k]);
		}
	}

	/**
	 * ぱずぷれ旧形式
	 */
	protected void decodeLITS_old(){
		String bstr = this.outbstr;
		int bdinside = (cols-1)*rows+cols*(rows-1);
		borders = new int[bdinside];
		if (bstr.length() < rows*cols)
			return; // 短いデータは無視
		char[] bstrA = bstr.toCharArray();
		for(int i=0; i<rows*cols; i++){
			int x = i%cols;
			int y = i/cols;
			if (x < cols-1) {
				char c0 = bstrA[i];
				char c1 = bstrA[i+1];
				if (c0 != c1)
					borders[y*(cols-1)+x] = 1;
			}
		}
		for(int i=0; i<rows*cols; i++){
			int x = i%cols;
			int y = i/cols;
			if (y < rows-1) {
				char c0 = bstrA[i];
				char c1 = bstrA[i+cols];
				if (c0 != c1)
					borders[((cols-1)*rows) + y*cols+x] = 1;
			}
		}
		outbstr(bstr, rows*cols);
	}

}
