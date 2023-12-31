package pencilbox.shakashaka;

import java.util.Arrays;

import pencilbox.common.core.Address;
import pencilbox.common.core.Direction;



/**
 * �u�V���J�V���J�v�̈�N���X
 */
public class Area extends pencilbox.common.core.AreaBase {

	int[] nAreaBorder = new int[8]; // �̈�̋��E�̕ӂ̐����`�󂲂Ƃɐ�����B

	private int cmin = Integer.MAX_VALUE;
	private int cmax = -1;
	private int rmin = Integer.MAX_VALUE;
	private int rmax = -1;

	void init() {
		clear();
		cmin = Integer.MAX_VALUE;
		cmax = -1;
		rmin = Integer.MAX_VALUE;
		rmax = -1;
		Arrays.fill(nAreaBorder, 0);
	}

	void updateMinMax(Address p) {
		int x = p.c();
		if (cmin > x) cmin = x;
		if (cmax < x) cmax = x;
		int y = p.r();
		if (rmin > y) rmin = y;
		if (rmax < y) rmax = y;
	}

	int countStraightBorder() {
		int ret = 0;
		for (int i = 0; i < 4; i++) {
			ret += nAreaBorder[i];
		}
		return ret;
	}

	int countDiagonalBorder() {
		int ret = 0;
		for (int i = 4; i < 8; i++) {
			ret += nAreaBorder[i];
		}
		return ret;
	}

	int csize() {
		return cmax-cmin+1;
	}

	int rsize() {
		return rmax-rmin+1;
	}

	int checkRectangleArea() {
//		System.out.println("�̈�"+toString()+"�̒����`����");
//		System.out.println("���}�X��" + this.size() + " �c���̋��E " + this.countStraightBorder() + " �΂߂̋��E " + this.countDiagonalBorder() + " �s�͈� " + this.rmin + " ~ " + this.rmax + " ��͈� " + this.cmin + " ~ " + this.cmax);
//		System.out.print(" ���ꂼ��̋��E�̐��� ");
//		for (int i = 0; i < 8; i++ ) System.out.print(i + ":" +this.nAreaBorder[i] + ", ");
//		System.out.println();	
		if (this.countDiagonalBorder() == 0) {
			int rectArea = this.rsize() * this.csize();
			if (this.size() == rectArea) {
//				System.out.println("�̈�͏c���̕ӂ���Ȃ�ʐ� " + this.size() + " �̒����`��");
				return 1;
			} else {
//				System.out.println("�̈�͏c���̕ӂ���Ȃ邪�C�ʐ� " + this.size() + " �Ȃ̂Œ����`�ł͂Ȃ�");
				return -1;
			}
//			System.out.println("�̈�͏c���̕ӂ���Ȃ钷���`��");
//			return 1;
		}
		if (this.countStraightBorder() == 0) {
			int nn = this.nAreaBorder[Direction.RTUP] + this.nAreaBorder[Direction.LTUP];
//			System.out.print(",�㑤�̕ӂ̐��̘a�� " + nn);
			if (nn != this.csize()) {
//				System.out.println(" �S�̂̃T�C�Y�ƍ���Ȃ��̂Œ����`�ł͂Ȃ�");
				return -1;
			}
//			System.out.println();
			nn = this.nAreaBorder[Direction.LTUP] + this.nAreaBorder[Direction.LTDN];
//			System.out.print(",�����̕ӂ̐��̘a�� " + nn);
			if (nn != this.rsize()) {
//				System.out.println(" �S�̂̃T�C�Y�ƍ���Ȃ��̂Œ����`�ł͂Ȃ�");
				return -1;
			}
//			System.out.println();
//			System.out.println("�΂߂̒����`��");
			return 1;
		}
		return -2;
	}
}
