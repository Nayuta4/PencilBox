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
	 * 変換した座標を返す
	 * @param pos
	 */
	public static Address translateAndRotateAddress(Address pos0, Address from, Address to, int rotation) {
		Address pos = Address.address(pos0.r() - from.r() + to.r(), pos0.c() - from.c() + to.c());
		int rows2 = to.r()*2;
		int cols2 = to.c()*2;
		switch (rotation) {
			case 0 :
				return Address.address(pos.r(), pos.c());
			case 1 :
				return Address.address((rows2 + cols2)/2 - pos.c(), (cols2 - rows2)/2 + pos.r());
			case 2 :
				return Address.address(rows2 - pos.r(), cols2 - pos.c());
			case 3 :
				return Address.address((rows2 - cols2)/2 + pos.c(), (cols2 + rows2)/2 - pos.r());
			case 4 :
				return Address.address((rows2 - cols2)/2 + pos.c(), (cols2 - rows2)/2 + pos.r());
			case 5 :
				return Address.address(pos.r(), cols2 - pos.c());
			case 6 :
				return Address.address((rows2 + cols2)/2 - pos.c(), (cols2 + rows2)/2 - pos.r());
			case 7 :
				return Address.address(rows2 - pos.r(), pos.c());
			default :
				return Address.address(pos.r(), pos.c());
		}
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
