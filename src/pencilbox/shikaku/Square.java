package pencilbox.shikaku;

/**
 * �u�l�p�ɐ؂�v�l�p�N���X
 */
/*
 * int �łȂ� Address ����̂̈����Ƃ���悤�ɂ�����
 * heyawake.Square �Ɠ��ꂵ����
 * r0, c0, r1, c1 �� private �ɂ�����
 */
public class Square {
	
	static final int NO_NUMBER = 0;
	static final int MULTIPLE_NUMBER = -2;
	private static int NEXT_ID = 1;
	
	private int id;  // �̈�ԍ�
	
	int r0;
	int c0;
	int r1;
	int c1;
	private int number; // �l�p�̐���
	
	/**
	 * �R���X�g���N�^
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 */
	public Square(int ra, int ca, int rb, int cb) {
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	/**
	 * ���W�̐ݒ�
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 */
	public void set(int ra, int ca, int rb, int cb) {
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	
	/**
	 * �����`�̈�̖ʐς�Ԃ�
	 * @return �̈�̖ʐ�
	 */
	public int getSquareSize() {
		return (r1-r0+1) * (c1-c0+1);
	}
	
	public String toString() {
		return "["+r0+","+c0+","+r1+","+c1+"]";
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
