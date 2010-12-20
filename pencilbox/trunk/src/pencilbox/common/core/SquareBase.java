package pencilbox.common.core;

import java.util.Set;
import java.util.TreeSet;

/**
 * ���ʎl�p�N���X
 */
public class SquareBase {
	
	private int r0;
	private int c0;
	private int r1;
	private int c1;

	/**
	 * �R���X�g���N�^
	 * @param ra ����̊p�̍s���W
	 * @param ca ����̊p�̗���W
	 * @param rb �����̊p�̍s���W
	 * @param cb �����̊p�̗���W
	 */
	public SquareBase(int ra, int ca, int rb, int cb) {
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	/**
	 * �R���X�g���N�^
	 * @param posA ����̊p�̍��W
	 * @param posB �����̊p�̍��W
	 */
	public SquareBase(Address posA, Address posB) {
		this(posA.r(), posA.c(), posB.r(), posB.c());
	}
	/**
	 * �R���X�g���N�^
	 * �����̎l�p�Ɠ������W�̎l�p���쐬����B
	 * @param s �l�p
	 */
	public SquareBase(SquareBase s) {
		this(s.r0, s.c0, s.r1, s.c1);
	}

	/**
	 * @return the r0
	 */
	public int r0() {
		return r0;
	}
	/**
	 * @return the c0
	 */
	public int c0() {
		return c0;
	}
	/**
	 * @return the r1
	 */
	public int r1() {
		return r1;
	}
	/**
	 * @return the c1
	 */
	public int c1() {
		return c1;
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
	 * �l�p�̂S���̃}�X���W������C�E��C�����C�E���̏��̒���4�̔z��ɓ���ĕԂ��B
	 * @return
	 */
	public Address[] getCorners() {
		return new Address[] {Address.address(r0, c0), Address.address(r0, c1), Address.address(r1, c0), Address.address(r1, c1)};
	}

	public Address p0() {
		return Address.address(r0, c0);
	}

	public Address p1() {
		return Address.address(r1, c1);
	}

	public void set(Address p0, Address p1) {
		set(p0.r(), p0.c(), p1.r(), p1.c());
	}

	public void set(SquareBase a) {
		set(a.r0(), a.c0(), a.r1(), a.c1());
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
	 * �����`�̈�̖ʐς�Ԃ�
	 * @return �̈�̖ʐ�
	 */
	public int getSquareSize() {
		return (r1-r0+1) * (c1-c0+1);
	}
	
	/**
	 * �l�p�̌`�󂪓�������
	 * @param o ��r�Ώۂ̎l�p
	 * @return �l�p�̊p�̍��W����v����� true
	 */
	public boolean equals(SquareBase o) {
		if (this == o) 
			return true;
		if (this.r0 == o.r0 && this.c0 == o.c0 && this.r1 == o.r1 && this.c1 == o.c1)
			return true;
		return false;
	}
	
	/**
	 * �����`�̈�Ɋ܂܂��}�X���W�̏W��
	 * @return �}�X���W�̏W��
	 */
	public Set<Address> cellSet() {
		Set<Address> s = new TreeSet<Address>();
		for (int r = r0; r <= r1; r++) {
			for (int c = c0; c <= c1; c++) {
				s.add(Address.address(r, c));
			}
		}
		return s;
	}

	public String toString() {
		return "["+r0+","+c0+","+r1+","+c1+"]";
	}

}
