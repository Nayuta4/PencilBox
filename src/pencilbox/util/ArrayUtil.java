package pencilbox.util;

/**
 * ”z—ñ‘€ì—p•â•ƒƒ\ƒbƒh‚ðW‚ß‚½ƒNƒ‰ƒX
 */
public class ArrayUtil {

    private ArrayUtil() {}

	/**
	 * ‚QŽŸŒ³intŒ^”z—ñ‚ðƒRƒs[‚·‚é
	 * @param from ƒRƒs[Œ³‚QŽŸŒ³intŒ^”z—ñ
	 * @param to ƒRƒs[æ‚QŽŸŒ³intŒ^”z—ñ
	 */
	public static void copyArrayInt2(int[][] from, int[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * ‚QŽŸŒ³intŒ^”z—ñ‚ðˆê—¥‚Ì’l‚Å‰Šú‰»‚·‚é
	 * @param from ‰Šú‰»‚·‚é‚QŽŸŒ³intŒ^”z—ñ
	 * @param value ’l
	 */
	public static void initArrayInt2(int[][] from, int value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * ‚QŽŸŒ³intŒ^”z—ñ‚ðƒRƒs[‚·‚é
	 * @param from ƒRƒs[Œ³‚QŽŸŒ³booleanŒ^”z—ñ
	 * @param to ƒRƒs[æ‚QŽŸŒ³booleanŒ^”z—ñ
	 */
	public static void copyArrayBoolean2(boolean[][] from, boolean[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * ‚QŽŸŒ³booleanŒ^”z—ñ‚ðˆê—¥‚Ì’l‚Å‰Šú‰»‚·‚é
	 * @param from ‰Šú‰»‚·‚é‚QŽŸŒ³booleanŒ^”z—ñ
	 * @param value ’l
	 */
	public static void initArrayBoolean2(boolean[][] from, boolean value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * ‚QŽŸŒ³ObjectŒ^”z—ñ‚ðƒRƒs[‚·‚é
	 * @param from ƒRƒs[Œ³‚QŽŸŒ³ObjectŒ^”z—ñ
	 * @param to ƒRƒs[æ‚QŽŸŒ³ObjectŒ^”z—ñ
	 */
	public static void copyArrayObject2(Object[][] from, Object[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * ‚QŽŸŒ³ObjectŒ^”z—ñ‚ðˆê—¥‚Ì’l‚Å‰Šú‰»‚·‚é
	 * @param from ‰Šú‰»‚·‚é‚QŽŸŒ³objectŒ^”z—ñ
	 * @param value ’l
	 */
	public static void initArrayObject2(Object[][] from, Object value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * ‚RŽŸŒ³booleanŒ^”z—ñ‚ðˆê—¥‚Ì’l‚Å‰Šú‰»‚·‚é
	 * @param from ‰Šú‰»‚·‚é‚RŽŸŒ³booleanŒ^”z—ñ
	 * @param value ’l
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
	 * ‚RŽŸŒ³intŒ^”z—ñ‚ðˆê—¥‚Ì’l‚Å‰Šú‰»‚·‚é
	 * @param from ‰Šú‰»‚·‚é‚RŽŸŒ³booleanŒ^”z—ñ
	 * @param value ’l
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
