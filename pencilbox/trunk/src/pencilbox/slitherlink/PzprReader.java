package pencilbox.slitherlink;

import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * �Q�l�Fpzprv3 slither.js
 */
public class PzprReader extends PzprReaderBase {

	private Board bd;

	protected BoardBase makeBoard() {
		bd = new Board();
		bd.setSize(rows+1, cols+1);		// �f�[�^���� 1 �傫���T�C�Y�̔Ֆʂ��쐬����B
		return bd;
	}

	protected void pzlimport(){
		decode4Cell();
	}

	protected void sQnC(int id, int num) {
		if (num == -2)
			bd.setNumber(i2a(id), Board.UNDECIDED_NUMBER);
		else if (num >= 0 && num <= 4)
			bd.setNumber(i2a(id), num);
	}
}
