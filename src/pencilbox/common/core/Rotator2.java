/**
 * 
 */
package pencilbox.common.core;


/**
 * 座標回転計算用補助クラス
 */
public class Rotator2 {

    private Rotator2() {}

	/**
	 * 引数で与えた回転番号は，盤面サイズの縦横交換に該当するか否かを答える
	 * @param n　回転番号
	 * @return 縦横交換されるなら true, されないなら false
	 */
	public static boolean isTransposed(int n) {
		switch (n) {
		case 0 :
		case 2 :
		case 5 :
		case 7 :
			return false;
		case 1 :
		case 3 :
		case 4 :
		case 6 :
			return true;
		default :
			return false;
		}
	}

	/**
	 * 回転変換の結合
	 * @param a　1つめの回転番号
	 * @param b　2つめの回転番号
	 * @return　回転番号の積
	 */
	public static int combine(int a, int b) {
		int r = a;
		if (a >= 0 && a <= 3) {
			r = (b / 4) * 4  + (a + b) % 4;
		} else if (a >= 4 && a <= 7) {
			r = ((7-b) / 4) * 4  + (a + (8-b)) % 4;
		}
		return r;
	}

	/**
	 * 与えられた座標posに，
	 * 座標fromから座標toへの移動と同じ平行移動したのちに，
	 * toを中心とした回転番号rotationの回転を施した座標を返す
	 * @param pos 変換元座標
	 * @param from 平行移動の起点
	 * @param to 平行移動の終点
	 * @param rotation 回転番号
	 * @return 変換後の座標
	 */
	public static Address translateAndRotateAddress(Address pos, Address from, Address to, int rotation) {
		int a = to.r();
		int b = to.c();
		int r = pos.r() - from.r() + to.r();
		int c = pos.c() - from.c() + to.c();
		int R = r;
		int C = c;
		switch (rotation) {
			case 0 :
				R = +r;
				C = +c;
				break;
			case 1 :
				R = -c +b +a;
				C = +r -a +b;
				break;
			case 2 :
				R = -r +a +a;
				C = -c +b +b;
				break;
			case 3 :
				R = +c -b +a;
				C = -r +a +b;
				break;
			case 4 :
				R = +c -b +a;
				C = +r -a +b;
				break;
			case 5 :
				R = +r      ;
				C = -c +b +b;
				break;
			case 6 :
				R = -c +b +a;
				C = -r +a +b;
				break;
			case 7 :
				R = -r +a +a;
				C = +c      ;
				break;
		}
		return Address.address(R, C);
	}

	/**
	 * 与えられた辺座標posに，
	 * 座標fromから座標toへの移動と同じ平行移動したのちに，
	 * toを中心とした回転番号rotationの回転を施した辺座標を返す
	 * @param pos 変換元の辺座標
	 * @param from 平行移動の起点
	 * @param to 平行移動の終点
	 * @param rotation 回転番号
	 * @return 変換後の辺座標
	 */
	public static SideAddress translateAndRotateSideAddress(SideAddress pos, Address from, Address to, int rotation) {
		int a = to.r();
		int b = to.c();
		int r = pos.r() - from.r() + to.r();
		int c = pos.c() - from.c() + to.c();
		int d = pos.d();
		int R = r;
		int C = c;
		int D = d;
		if (d == Direction.VERT){
			switch (rotation) {
				case 0 :
					D = d;
					R = +r;
					C = +c;
					break;
				case 1 :
					D = d^1;
					R = -c +b +a -1;
					C = +r -a +b;
					break;
				case 2 :
					D = d;
					R = -r +a +a;
					C = -c +b +b -1;
					break;
				case 3 :
					D = d^1;
					R = +c -b +a;
					C = -r +a +b;
					break;
				case 4 :
					D = d^1;
					R = +c -b +a;
					C = +r -a +b;
					break;
				case 5 :
					D = d;
					R = +r      ;
					C = -c +b +b -1;
					break;
				case 6 :
					D = d^1;
					R = -c +b +a -1;
					C = -r +a +b;
					break;
				case 7 :
					D = d;
					R = -r +a +a;
					C = +c      ;
					break;
			}
		} else {
			switch (rotation) {
				case 0 :
					D = d;
					R = +r;
					C = +c;
					break;
				case 1 :
					D = d^1;
					R = -c +b +a;
					C = +r -a +b;
					break;
				case 2 :
					D = d;
					R = -r +a +a -1;
					C = -c +b +b;
					break;
				case 3 :
					D = d^1;
					R = +c -b +a;
					C = -r +a +b -1;
					break;
				case 4 :
					D = d^1;
					R = +c -b +a;
					C = +r -a +b;
					break;
				case 5 :
					D = d;
					R = +r      ;
					C = -c +b +b;
					break;
				case 6 :
					D = d^1;
					R = -c +b +a;
					C = -r +a +b -1;
					break;
				case 7 :
					D = d;
					R = -r +a +a -1;
					C = +c      ;
					break;
			}
		}
		return SideAddress.sideAddress(D, R, C);
	}

	/**
	 * 盤上の方向をパネル上の方向に変換する
	 * @param direction 変換元の方向を表す数値
	 * @return 変換後の方向を表す数値
	 */
	public static int rotateDirection(int direction, int rotation) {
		switch (rotation) {
			case 0 :
			case 1 :
			case 2 :
			case 3 :
				direction = (direction + rotation) % 4;
				break;
			case 4 :
			case 5 :
			case 6 :
			case 7 :
				direction = (direction + rotation) % 4;
				direction = direction ^ 1;
				break;
		}
		return direction;
	}

}
