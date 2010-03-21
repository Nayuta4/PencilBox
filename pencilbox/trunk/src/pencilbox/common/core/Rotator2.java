/**
 * 
 */
package pencilbox.common.core;


/**
 * 座標回転計算用補助クラス
 * マス中心を設定して回転する
 */
public class Rotator2 {
	
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
	 * 回転・反転番号
	 * 0　回転なし
	 * 1 左90°回転
	 * 2　左180°回転
	 * 3 左270°回転
	 * 4 縦と横を交換
	 * 5 左90°回転し，その後縦と横を交換し
	 * 6 左180°回転し，その後縦と横を交換
	 * 7 左270°回転し，その後縦と横を交換
	 */
	private int rotation = 0;
//	private int rows; // 回転中心
//	private int cols; // 回転中心
	private int rows2; // 回転中心の2倍
	private int cols2; // 回転中心の2倍
	
	/**
	 * コンストラクタ
	 */
	public Rotator2() {
	}
	
	/**
	 * コンストラクタ
	 * @param rows 行数
	 * @param cols 列数
	 * @param rotation 回転・反転番号
	 */
	public Rotator2(int rows, int cols, int rotation) {
//		this.rows = rows;
//		this.cols = cols;
		this.rows2 = rows * 2;
		this.cols2 = cols * 2;
		this.rotation = rotation;
	}
	
	/**
	 * コンストラクタ
	 * @param center 中心座標
	 * @param rotation 回転・反転番号
	 */
	public Rotator2(Address center, int rotation) {
		this(center.r(), center.c(), rotation);
	}
	
	/**
	 * 状態を設定する
	 * @param rows 行数
	 * @param cols 列数
	 * @param rotation 回転・反転番号
	 */
	public void setup(int rows, int cols, int rotation) {
//		this.rows = rows;
//		this.cols = cols;
		this.rows2 = rows * 2;
		this.cols2 = cols * 2;
		this.rotation = rotation;
	}

	/**
	 * @return Returns the rotation.
	 */
	public int getRotation() {
		return rotation;
	}
	
	/**
	 * 盤面サイズの縦横が交換されるかどうか
	 * @return 縦横交換されるならば true
	 */
	public boolean isTransposed() {
		return isTransposed(rotation);
	}

	/**
	 * 変換した座標を返す
	 * @param pos
	 */
	public Address rotateAddress(Address pos) {
		Address dst = Address.address();
		switch (rotation) {
			case 0 :
				dst.set(pos.r(), pos.c());
				break;
			case 1 :
				dst.set((rows2 + cols2)/2 - pos.c(), (cols2 - rows2)/2 + pos.r());
				break;
			case 2 :
				dst.set(rows2 - pos.r(), cols2 - pos.c());
				break;
			case 3 :
				dst.set((rows2 - cols2)/2 + pos.c(), (cols2 + rows2)/2 - pos.r());
				break;
			case 4 :
				dst.set((rows2 - cols2)/2 + pos.c(), (cols2 - rows2)/2 + pos.r());
				break;
			case 5 :
				dst.set(pos.r(), cols2 - pos.c());
				break;
			case 6 :
				dst.set((rows2 + cols2)/2 - pos.c(), (cols2 + rows2)/2 - pos.r());
				break;
			case 7 :
				dst.set(rows2 - pos.r(), pos.c());
				break;
		}
		return dst;
	}

	/**
	 * 盤上の方向をパネル上の方向に変換する
	 * @param direction 変換元の方向を表す数値
	 * @return 変換後の方向を表す数値
	 */
	public int rotateDirection(int direction) {
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
