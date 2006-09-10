/**
 * 
 */
package pencilbox.common.core;

import java.util.Iterator;

/**
 * ���W��]�v�Z�p�⏕�N���X
 */
public class Rotator {
	
	/**
	 * �����ŗ^������]�ԍ��́C�ՖʃT�C�Y�̏c�������ɊY�����邩�ۂ��𓚂���
	 * @param n�@��]�ԍ�
	 * @return �c�����������Ȃ� true, ����Ȃ��Ȃ� false
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
	 * ��]�E���]�ԍ�
	 * 0�@��]�Ȃ�
	 * 1 ��90����]
	 * 2�@��180����]
	 * 3 ��270����]
	 * 4 �c�Ɖ�������
	 * 5 ��90����]���C���̌�c�Ɖ���������
	 * 6 ��180����]���C���̌�c�Ɖ�������
	 * 7 ��270����]���C���̌�c�Ɖ�������
	 */
	private int rotation = 0;
	private int rows; // �S�̂̍s��
	private int cols; // �S�̗̂�
	
	/**
	 * �R���X�g���N�^
	 */
	public Rotator() {
	}
	
	/**
	 * ��Ԃ�ݒ肷��
	 * @param rows �s��
	 * @param cols ��
	 * @param rotation ��]�E���]�ԍ�
	 */
	public Rotator(int rows, int cols, int rotation) {
		this.rows = rows;
		this.cols = cols;
		this.rotation = rotation;
	}
	
	/**
	 * ��Ԃ�ݒ肷��
	 * @param rows �s��
	 * @param cols ��
	 * @param rotation ��]�E���]�ԍ�
	 */
	public void setup(int rows, int cols, int rotation) {
		this.rows = rows;
		this.cols = cols;
		this.rotation = rotation;
	}

	/**
	 * @return Returns the rotation.
	 */
	public int getRotation() {
		return rotation;
	}
	
	/**
	 * �ՖʃT�C�Y�̏c������������邩�ǂ���
	 * @return �c�����������Ȃ�� true
	 */
	public boolean isTransposed() {
		return isTransposed(rotation);
	}

	/**
	 * �ϊ��������W��Ԃ�
	 * @param pos
	 */
	public Address rotateAddress(Address pos) {
		Address dst = new Address();
		switch (rotation) {
			case 0 :
				dst.set(pos.r(), pos.c());
				break;
			case 1 :
				dst.set(cols - 1 - pos.c(), pos.r());
				break;
			case 2 :
				dst.set(rows - 1 - pos.r(), cols - 1 - pos.c());
				break;
			case 3 :
				dst.set(pos.c(), rows - 1 - pos.r());
				break;
			case 4 :
				dst.set(pos.c(), pos.r());
				break;
			case 5 :
				dst.set(pos.r(), cols - 1 - pos.c());
				break;
			case 6 :
				dst.set(cols - 1 - pos.c(), rows - 1 - pos.r());
				break;
			case 7 :
				dst.set(rows - 1 - pos.r(), pos.c());
				break;
		}
		return dst;
	}

	/**
	 * �Տ�̕������p�l����̕����ɕϊ�����
	 * @param direction �ϊ����̕�����\�����l
	 * @return �ϊ���̕�����\�����l
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
	 * Area����]���ĕ�������
	 * @param src ������Area
	 * @param dst ������Area
	 * @return dst�Ɠ���
	 */
	public Area rotateArea(Area src, Area dst) {
		for (Iterator itr = src.iterator(); itr.hasNext(); ) {
			dst.add(rotateAddress((Address)itr.next()));
		}
		return dst;
	}

	/**
	 * �Q����int�^�z�����]���ĕ�������
	 * ������z��͂��炩���ߍ���Ă����Ȃ��Ƃ����Ȃ�
	 * @param src �������Q����int�^�z��
	 * @param dst ������Q����int�^�z��
	 */
	public void rotateArrayInt2(int[][] src, int[][] dst) {
		Address address = new Address();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				address.set(r, c);
				address = rotateAddress(address);
				dst[address.r()][address.c()] = src[r][c];
			}
		}
	}
	
	/**
	 * �Ӎ��W�^�̂R����int�^�z�����]���ĕ�������
	 * ������z��͂��炩���ߍ���Ă����Ȃ��Ƃ����Ȃ�
	 * @param src �������R����int�^�z��
	 * @param dst ������R����int�^�z��
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
