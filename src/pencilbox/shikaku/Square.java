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
	 * �R���X�g���N�^
	 * �����̎l�p�Ɠ������W�̎l�p���쐬����B
	 * @param s �l�p
	 */
	public Square(Square s) {
		this(s.r0, s.c0, s.r1, s.c1);
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
	 * �l�p��1�̒��_���Œ肵���܂܁C�Ίp�ʒu�̒��_�̍��W��ύX����B
	 * @param rOld �ύX�O�̒��_�̍s���W
	 * @param cOld �ύX�O�̒��_�̍s���W
	 * @param rNew �ύX��̒��_�̍s���W
	 * @param cNew �ύX��̒��_�̍s���W
	 */
	public void changeCorner(int rOld, int cOld, int rNew, int cNew) {
		int ra = this.r0;
		int ca = this.c0;
		int rb = this.r1;
		int cb = this.c1;
		if (ra == rOld) {
			ra = rNew;
		} else if (rb == rOld) {
			rb = rNew;
		}
		if (ca == cOld) {
			ca = cNew;
		} else if (cb == cOld) {
			cb = cNew;
		}
		this.set(ra, ca, rb, cb);
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
