package pencilbox.util;

/**
 * 配列操作用補助メソッドを集めたクラス
 */
public class ArrayUtil {

    private ArrayUtil() {}

	/**
	 * ２次元int型配列をコピーする
	 * @param from コピー元２次元int型配列
	 * @param to コピー先２次元int型配列
	 */
	public static void copyArrayInt2(int[][] from, int[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * ２次元int型配列を一律の値で初期化する
	 * @param from 初期化する２次元int型配列
	 * @param value 値
	 */
	public static void initArrayInt2(int[][] from, int value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * ２次元int型配列をコピーする
	 * @param from コピー元２次元boolean型配列
	 * @param to コピー先２次元boolean型配列
	 */
	public static void copyArrayBoolean2(boolean[][] from, boolean[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * ２次元boolean型配列を一律の値で初期化する
	 * @param from 初期化する２次元boolean型配列
	 * @param value 値
	 */
	public static void initArrayBoolean2(boolean[][] from, boolean value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * ２次元Object型配列をコピーする
	 * @param from コピー元２次元Object型配列
	 * @param to コピー先２次元Object型配列
	 */
	public static void copyArrayObject2(Object[][] from, Object[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * ２次元Object型配列を一律の値で初期化する
	 * @param from 初期化する２次元object型配列
	 * @param value 値
	 */
	public static void initArrayObject2(Object[][] from, Object value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * ３次元boolean型配列を一律の値で初期化する
	 * @param from 初期化する３次元boolean型配列
	 * @param value 値
	 */
	public static void initArrayBoolean3(boolean[][][] from, boolean value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				for (int k = from[r][c].length - 1; k >= 0; k--) {
					from[r][c][k] = value;
				}
			}
		}
	}
	/**
	 * ３次元int型配列を一律の値で初期化する
	 * @param from 初期化する３次元boolean型配列
	 * @param value 値
	 */
	public static void initArrayInt3(int[][][] from, int value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				for (int k = from[r][c].length - 1; k >= 0; k--) {
					from[r][c][k] = value;
				}
			}
		}
	}
}
