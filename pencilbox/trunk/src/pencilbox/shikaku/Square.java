package pencilbox.shikaku;

import pencilbox.common.core.Address;

/**
 * �u�l�p�ɐ؂�v�l�p�N���X
 */
public class Square extends pencilbox.common.core.SquareBase {
	
	static final int NO_NUMBER = 0;
	static final int MULTIPLE_NUMBER = -2;
	private static int NEXT_ID = 1;
	
	private int id;  // �̈�ԍ�
	
	private int number; // �l�p�̐���
	
	/**
	 * �R���X�g���N�^
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 */
	public Square(int ra, int ca, int rb, int cb) {
		super(ra, ca, rb, cb);
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
	}
	/**
	 * �R���X�g���N�^
	 * @param posA ����̊p�̍��W
	 * @param posB �����̊p�̍��W
	 */
	public Square(Address posA, Address posB) {
		this(posA.r(), posA.c(), posB.r(), posB.c());
	}
	/**
	 * �R���X�g���N�^
	 * �����̎l�p�Ɠ������W�̎l�p���쐬����B
	 * @param s �l�p
	 */
	public Square(Square s) {
		this(s.r0(), s.c0(), s.r1(), s.c1());
	}

	/**
	 * @return ����
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param i
	 */
	public void setNumber(int i) {
		number = i;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

}
