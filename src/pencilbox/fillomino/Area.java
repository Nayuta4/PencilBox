package pencilbox.fillomino;

/**
 * �u�t�B���I�~�m�v�̈�N���X
 */
public class Area extends pencilbox.common.core.AreaBase {

	private int number; // �̈�̐���

	/**
	 * �R���X�g���N�^
	 * @param number �̈�̐���
	 */
	public Area(int number) {
		super();
		this.number = number;
	}
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number The number to set.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * Area �̏�Ԃ�Ԃ�
	 * @return �T�C�Y��������菬���� : 0
	 * �T�C�Y�������ɓ����� : 1
	 * �T�C�Y���������傫�� : -1
	 */
	public int getStatus() {
		if (size() < number) return 0;
		else if (size() == number) return 1;
		else if (size() > number) return -1;
		return -1;
	}

}
