package pencilbox.nurikabe;


/**
 * �u�ʂ肩�ׁv�̈�N���X
 */
public class Area extends pencilbox.common.core.Area {

	static int NEXT_ID = 1;
	static int MULTIPLE_NUMBER = -1;

	private int id;
	private int areaType = 0;
	private int number = 0; // �����̐���
	

	/**
	 * �̈���쐬����
	 * @param areaType �̈�^�C�v:���}�X�̈悩���}�X�̈悩
	 */
	public Area(int areaType) {
		super();
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
		this.areaType = areaType;
	}
	
	/**
	 * �̈�ɐ�����ݒ肷��
	 * ���̗̈�ɈȑO�ɐ������܂܂�Ă��Ȃ������ꍇ�́C���̐�����ݒ肷��
	 * ���łɑ��̐������܂܂�Ă����ꍇ�́C���������̈�ƂȂ�
	 * @param n �ǉ����鐔��
	 */
	public void addNumber(int n) {
		if (number ==0) {
			number = n;
		} else if (number != 0) {
			number = MULTIPLE_NUMBER;
		}
	}
	
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return Returns the areaType.
	 */
	public int getAreaType() {
		return areaType;
	}

	/**
	 *  �̈�ԍ����擾����
	 * @return �̈�ԍ� 
	 */
	public int getID() {
		return id;
	}

	/**
	 * �̈�ԍ���ݒ肷��
	 * @param i �ݒ肷��ԍ�
	 */
	public void setID(int i) {
		id = i;
	}
	/**
	 * 
	 */
	public static void resetID() {
		NEXT_ID = 1;
	}

	
}
