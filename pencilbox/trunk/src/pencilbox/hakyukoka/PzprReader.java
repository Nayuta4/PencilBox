package pencilbox.hakyukoka;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * �Q�l�Fpzprv3 ripple.js
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
		this.decodeNumber16();
	}

	protected void sQnC(int id, int num) {
		if (num > 0) {
			bd.setNumber(i2a(id),num);
		} else if (num == -2) {
			bd.setNumber(i2a(id), Board.UNDETERMINED);
		}
	}

	/**
	 * ���E���f�[�^����̈�ԍ��f�[�^���쐬����B
	 */
	private void makeAreas() {
		Area[] areaArray = new Area[nArea];
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

}
