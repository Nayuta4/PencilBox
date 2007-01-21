package pencilbox.heyawake;

/**
 * �u�ւ�킯�v�����N���X
 */
public class Square {

	static final int ANY = -1;

	int r0;
	int c0;
	int r1;
	int c1;
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
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
		this.setNumber(number);
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
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
		this.setNumber(ANY);
	}
	/**
	 * ���W�̐ݒ�
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 * 
	 */
	public void set(int ra, int ca, int rb, int cb) {
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	/**
	 * ������ݒ肷��
	 * @param num
	 */
//	public void setNumber(int num) {
//		this.number = num;
//	}
	/**
	 * @return �����̗�T�C�Y
	 */
	public int sizeC() {
		return c1 - c0 +1;
	}
	/**
	 * @return �����̍s�T�C�Y
	 */
	public int sizeR() {
		return r1 - r0 + 1;
	}
	/**
	 * @return �����̖ʐ�
	 */
	public int size() {
		return sizeR() * sizeC();
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
		return "["+r0+","+c0+","+r1+","+c1+","+getNumber()+"]";
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
