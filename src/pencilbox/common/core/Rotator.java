/**
 * 
 */
package pencilbox.common.core;


/**
 * 座標回転計算用補助クラス
 */
public class Rotator {

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
	private int rows; // 全体の行数
	private int cols; // 全体の列数

	/**
	 * コンストラクタ
	 * @param rows 行数
	 * @param cols 列数
	 * @param rotation 回転・反転番号
	 */
	public Rotator(int rows, int cols, int rotation) {
		this.rows = rows;
		this.cols = cols;
		this.rotation = rotation;
	}

	/**
	 * コンストラクタ
	 * @param size 盤面サイズ
	 * @param rotation 回転・反転番号
	 */
	public Rotator(Size size, int rotation) {
		this(size.getRows(), size.getCols(), rotation);
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
		int r = pos.r();
		int c = pos.c();
		int R = r;
		int C = c;
		switch (rotation) {
			case 0 :
				R = r;
				C = c;
				break;
			case 1 :
				R = cols - 1 - c;
				C = r;
				break;
			case 2 :
				R = rows - 1 - r;
				C = cols - 1 - c;
				break;
			case 3 :
				R = c;
				C = rows - 1 - r;
				break;
			case 4 :
				R = c;
				C = r;
				break;
			case 5 :
				R = r;
				C = cols - 1 - c;
				break;
			case 6 :
				R = cols - 1 - c;
				C = rows - 1 - r;
				break;
			case 7 :
				R = rows - 1 - r;
				C = c;
				break;
		}
		return Address.address(R, C);
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

	/**
	 * Areaを回転して複製する
	 * @param src 複製元Area
	 * @param dst 複製先Area
	 * @return dstと同じ
	 */
	public AreaBase rotateArea(AreaBase src, AreaBase dst) {
		for (Address p : src) {
			dst.add(rotateAddress(p));
		}
		return dst;
	}

	/**
	 * ２次元int型配列を回転して複製する
	 * 複製先配列はあらかじめ作っておかないといけない
	 * @param src 複製元２次元int型配列
	 * @param dst 複製先２次元int型配列
	 */
	public void rotateArrayInt2(int[][] src, int[][] dst) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				Address address = rotateAddress(Address.address(r, c));
				if (address.r() < dst.length && address.c() < dst[address.r()].length)
					dst[address.r()][address.c()] = src[r][c];
			}
		}
	}

	/**
	 * 辺座標型の３次元int型配列を回転して複製する
	 * 複製先配列はあらかじめ作っておかないといけない
	 * @param src 複製元３次元int型配列
	 * @param dst 複製先３次元int型配列
	 */
	public void rotateArrayInt3(int[][][] src, int[][][] dst) {
		Rotator rotator2;
		switch (rotation) {
		case 0:
		case 2:
		case 5:
		case 7:
			rotator2 = new Rotator(rows, cols-1, rotation);
			rotator2.rotateArrayInt2(src[0], dst[0]);
			rotator2 = new Rotator(rows-1, cols, rotation);
			rotator2.rotateArrayInt2(src[1], dst[1]);
			break;
		case 1:
		case 3:
		case 4:
		case 6:
			rotator2 = new Rotator(rows, cols-1, rotation);
			rotator2.rotateArrayInt2(src[0], dst[1]);
			rotator2 = new Rotator(rows-1, cols, rotation);
			rotator2.rotateArrayInt2(src[1], dst[0]);
			break;
		}
	}

}
