/**
 * 
 */
package pencilbox.common.core;


/**
 * ���W��]�v�Z�p�⏕�N���X
 */
public class Rotator2 {
	
    private Rotator2() {}

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
	 * �^����ꂽ���Wpos�ɁC
	 * ���Wfrom������Wto�ւ̈ړ��Ɠ������s�ړ������̂��ɁC
	 * to�𒆐S�Ƃ�����]�ԍ�rotation�̉�]���{�������W��Ԃ�
	 * @param pos �ϊ������W
	 * @param from ���s�ړ��̋N�_
	 * @param to ���s�ړ��̏I�_
	 * @param rotation ��]�ԍ�
	 * @return �ϊ���̍��W
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
	 * �Տ�̕������p�l����̕����ɕϊ�����
	 * @param direction �ϊ����̕�����\�����l
	 * @return �ϊ���̕�����\�����l
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
