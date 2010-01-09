package pencilbox.yajilin;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.io.PzprReaderBase;


/**
 * �Q�l�Fpzprv3 yajilin.js
 * 
 * ���̂Ȃ������͏�������̐����ɕύX�����
 * ���̂��関�萔���͖��Ȃ��ɕύX�����
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
		  // ����������ƂȂ�����󂪏�����ɂȂ�̂ŁA��������������o���Ă��Ƃ���Đݒ肷��B
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
		bd.setNumber(a, 0); // ���̌��������߂邽�߂ɉ��ɐ���0������B
		bd.setArrowDirection(a, d);
	}
}
