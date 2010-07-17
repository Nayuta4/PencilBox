package pencilbox.heyawake;

import pencilbox.common.core.Address;

/**
 * �u�ւ�킯�v�����N���X
 */
public class Square extends pencilbox.common.core.Square {

	static final int ANY = -1;

	private int number;
	private int nBlack;  // ���m��}�X��
	private int nWhite;  // ���m��}�X��
	/**
	 * �R���X�g���N�^
	 * ���Ίp�_�̍��W�Ɛ������畔�����쐬
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 * @param number �����̐���
	 */
	public Square(int ra, int ca, int rb, int cb, int number){
		super(ra, ca, rb, cb);
		this.setNumber(number);
	}
	public Square(Address p0, Address p1, int number){
		this(p0.r(), p0.c(), p1.r(), p1.c(), number);
	}
	/**
	 * �R���X�g���N�^
	 * ���Ίp�_�̍��W���� �����Ȃ��̕������쐬
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 */
	public Square(int ra, int ca, int rb, int cb){
		super(ra, ca, rb, cb);
		this.setNumber(ANY);
	}
	/**
	 * �R���X�g���N�^
	 * ���Ίp�_�̍��W���� �����Ȃ��̕������쐬
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
	 * @param number The number to set.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param nBlack The nBlack to set.
	 */
	void setNBlack(int nBlack) {
		this.nBlack = nBlack;
	}
	/**
	 * @return Returns the nBlack.
	 */
	int getNBlack() {
		return nBlack;
	}
	/**
	 * @param nWhite The nWhite to set.
	 */
	void setNWhite(int nWhite) {
		this.nWhite = nWhite;
	}
	/**
	 * @return Returns the nWhite.
	 */
	int getNWhite() {
		return nWhite;
	}
	/**
	 * 
	 */
	public void clear() {
		setNBlack(0);
		setNWhite(0);
	}
	
	public String toString() {
		return super.toString() + ","+getNumber();
	}

	int mx() {
		
		int min;
		int max;
		int mx;
		
		if (this.sizeC() < this.sizeR()) {
			min = this.sizeC();
			max = this.sizeR();
		} else {
			min = this.sizeR();
			max = this.sizeC();
		}
		
		if (min == 1) {
			mx = (max + 1) / 2;
		} else if (min == 3) {
			mx = (max / 4) * 5;
			
			switch (max & 0x3) {
				case 1:
					mx += 2;
					break;
				case 2:
					mx += 3;
					break;
				case 3:
					mx += 5;
					break;
			}
		} else if ((min & max & 0x1) != 0) {
			if (min == max && (max & (max + 1)) == 0) {
				mx = (min * max + min + max) / 3;
			} else {
				mx = (min * max + min + max - 1) / 3;
			}
		} else {
			mx = (min * max + min + max - 2) / 3;
		}
		
		return mx;
	}
}
