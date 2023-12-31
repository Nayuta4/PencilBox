package pencilbox.shakashaka;

import java.util.Arrays;

import pencilbox.common.core.Address;
import pencilbox.common.core.Direction;



/**
 * 「シャカシャカ」領域クラス
 */
public class Area extends pencilbox.common.core.AreaBase {

	int[] nAreaBorder = new int[8]; // 領域の境界の辺の数を形状ごとに数える。

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
//		System.out.println("領域"+toString()+"の長方形判定");
//		System.out.println("総マス数" + this.size() + " 縦横の境界 " + this.countStraightBorder() + " 斜めの境界 " + this.countDiagonalBorder() + " 行範囲 " + this.rmin + " ~ " + this.rmax + " 列範囲 " + this.cmin + " ~ " + this.cmax);
//		System.out.print(" それぞれの境界の数は ");
//		for (int i = 0; i < 8; i++ ) System.out.print(i + ":" +this.nAreaBorder[i] + ", ");
//		System.out.println();	
		if (this.countDiagonalBorder() == 0) {
			int rectArea = this.rsize() * this.csize();
			if (this.size() == rectArea) {
//				System.out.println("領域は縦横の辺からなる面積 " + this.size() + " の長方形だ");
				return 1;
			} else {
//				System.out.println("領域は縦横の辺からなるが，面積 " + this.size() + " なので長方形ではない");
				return -1;
			}
//			System.out.println("領域は縦横の辺からなる長方形だ");
//			return 1;
		}
		if (this.countStraightBorder() == 0) {
			int nn = this.nAreaBorder[Direction.RTUP] + this.nAreaBorder[Direction.LTUP];
//			System.out.print(",上側の辺の数の和は " + nn);
			if (nn != this.csize()) {
//				System.out.println(" 全体のサイズと合わないので長方形ではない");
				return -1;
			}
//			System.out.println();
			nn = this.nAreaBorder[Direction.LTUP] + this.nAreaBorder[Direction.LTDN];
//			System.out.print(",左側の辺の数の和は " + nn);
			if (nn != this.rsize()) {
//				System.out.println(" 全体のサイズと合わないので長方形ではない");
				return -1;
			}
//			System.out.println();
//			System.out.println("斜めの長方形だ");
			return 1;
		}
		return -2;
	}
}
