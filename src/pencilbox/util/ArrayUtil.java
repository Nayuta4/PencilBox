package pencilbox.util;

/**
 * �z�񑀍�p�⏕���\�b�h���W�߂��N���X
 */
public class ArrayUtil {

    private ArrayUtil() {}

	/**
	 * �Q����int�^�z����R�s�[����
	 * @param from �R�s�[���Q����int�^�z��
	 * @param to �R�s�[��Q����int�^�z��
	 */
	public static void copyArrayInt2(int[][] from, int[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * �Q����int�^�z����ꗥ�̒l�ŏ���������
	 * @param from ����������Q����int�^�z��
	 * @param value �l
	 */
	public static void initArrayInt2(int[][] from, int value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * �Q����int�^�z����R�s�[����
	 * @param from �R�s�[���Q����boolean�^�z��
	 * @param to �R�s�[��Q����boolean�^�z��
	 */
	public static void copyArrayBoolean2(boolean[][] from, boolean[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * �Q����boolean�^�z����ꗥ�̒l�ŏ���������
	 * @param from ����������Q����boolean�^�z��
	 * @param value �l
	 */
	public static void initArrayBoolean2(boolean[][] from, boolean value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * �Q����Object�^�z����R�s�[����
	 * @param from �R�s�[���Q����Object�^�z��
	 * @param to �R�s�[��Q����Object�^�z��
	 */
	public static void copyArrayObject2(Object[][] from, Object[][] to) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				to[r][c] = from[r][c];
			}
		}
	}
	/**
	 * �Q����Object�^�z����ꗥ�̒l�ŏ���������
	 * @param from ����������Q����object�^�z��
	 * @param value �l
	 */
	public static void initArrayObject2(Object[][] from, Object value) {
		for (int r = from.length - 1; r >= 0; r--) {
			for (int c = from[r].length - 1; c >= 0; c--) {
				from[r][c] = value;
			}
		}
	}
	/**
	 * �R����boolean�^�z����ꗥ�̒l�ŏ���������
	 * @param from ����������R����boolean�^�z��
	 * @param value �l
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
	 * �R����int�^�z����ꗥ�̒l�ŏ���������
	 * @param from ����������R����boolean�^�z��
	 * @param value �l
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
