/**
 * 
 */
package pencilbox.common.core;


/**
 * ���W��]�v�Z�p�⏕�N���X
 * �}�X���S��ݒ肵�ĉ�]����
 */
public class Rotator2 {
	
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
	 * ��]�ϊ��̌���
	 * @param a�@1�߂̉�]�ԍ�
	 * @param b�@2�߂̉�]�ԍ�
	 * @return�@��]�ԍ��̐�
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
//	private int rows; // ��]���S
//	private int cols; // ��]���S
	private int rows2; // ��]���S��2�{
	private int cols2; // ��]���S��2�{
	
	/**
	 * �R���X�g���N�^
	 */
	public Rotator2() {
	}
	
	/**
	 * �R���X�g���N�^
	 * @param rows �s��
	 * @param cols ��
	 * @param rotation ��]�E���]�ԍ�
	 */
	public Rotator2(int rows, int cols, int rotation) {
//		this.rows = rows;
//		this.cols = cols;
		this.rows2 = rows * 2;
		this.cols2 = cols * 2;
		this.rotation = rotation;
	}
	
	/**
	 * �R���X�g���N�^
	 * @param center ���S���W
	 * @param rotation ��]�E���]�ԍ�
	 */
	public Rotator2(Address center, int rotation) {
		this(center.r(), center.c(), rotation);
	}
	
	/**
	 * ��Ԃ�ݒ肷��
	 * @param rows �s��
	 * @param cols ��
	 * @param rotation ��]�E���]�ԍ�
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
	
}
