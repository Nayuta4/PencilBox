package pencilbox.common.core;


/**
 * �����l�̍��W��\���N���X
 */
public class Address implements Comparable<Address> {
	
	/**
	 *  �ՊO���W
	 */
	public static final Address NOWEHER = new Address(-1,-1);
	
	/**
	 *  �s���W
	 */
	private int r;
	/**
	 *  ����W
	 */
	private int c;
	
	/**
	 * �R���X�g���N�^
	 */
	public Address(){
		this(0,0);
	}
	/**
	 * �R���X�g���N�^�C�����̍��W�Ɠ��������W�ɐݒ�
	 * @param pos �ݒ肷����W
	 */
	public Address(Address pos){
		this.r = pos.r;
		this.c = pos.c;
	}
	/**
	 * �R���X�g���N�^�C�����̍��W�ɐݒ�
	 * @param r �ݒ肷��s���W
	 * @param c �ݒ肷�����W
	 */
	public Address(int r, int c){
		this.r = r;
		this.c = c;
	}
	/**
	 * @return Returns the r.
	 */
	public int r() {
		return r;
	}
	/**
	 * @return Returns the c.
	 */
	public int c() {
		return c;
	}
	/**
	 * �����̍��W�ɐݒ�
	 * @param pos �ݒ肷����W
	 */
	public void set(Address pos){
		this.r = pos.r;
		this.c = pos.c;
	}
	/**
	 * �����̍��W�ɐݒ�
	 * @param r �ݒ肷��s���W
	 * @param c �ݒ肷�����W
	 */
	public void set(int r, int c){
		this.r = r;
		this.c = c;
	}
	/**
	 * �����̍��W�Ɠ����������r����
	 * @param o ��r�Ώ�
	 * @return ���̍��W�ƈ����̍��W����������� true
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Address))
			return false;
		Address address = (Address)o;
		if (address.r == r && address.c == c)
			return true;
		else
			return false;
	}
	/**
	 * �����̍��W�Ɠ����������r����
	 * @param rr ��r�Ώۂ̍s���W
	 * @param cc ��r�Ώۂ̗���W
	 * @return ���̍��W�ƈ����̍��W����������� true
	 */
	public boolean equals(int rr, int cc) {
		if (rr == r && cc == c)
			return true;
		else
			return false;
	}
	public int hashCode() {
		return r * 1000 + c;
	}
//	/**
//	 * �����̍��W�Ə㉺���E�ɗאڂ��Ă��邩
//	 * @param address
//	 * @return �אڂ��Ă���� true
//	 */
//	public boolean isNextTo(Address address) {
//		if ((address.r == r && (address.c == c-1 || address.c == c+1))
//			|| (address.c == c && (address.r == r-1 || address.r == r+1)))
//		return true;
//		else
//			return false;
//	}
//	/**
//	 * �����̍��W�Ɠ����s�܂��͗�
//	 * @param address ��r���� Address
//	 * @return ���꒼����Ȃ� true
//	 */
//	public boolean isInLine(Address address) {
//		if ( address.r == r || address.c == c )
//			return true;
//		else
//			return false;
//	}
	/**
	 * �ՊO���W���ǂ���
	 * @return ���W���ՊO�_�ł���� true
	 */
	public boolean isNowhere() {
		return (r==-1 && c==-1);
	}
	/**
	 * �ՊO���W�ɐݒ肷��
	 */
	public void setNowhere() {
		r = -1;
		c = -1;
	}
	/**
	 * �ׂ̃}�X���W�Ɉړ�����
	 * @param direction �ړ��������
	 */
	public void move(int direction) {
		switch (direction) {
			case Direction.UP:
				r--;
				break;
			case Direction.LT:
				c--;
				break;
			case Direction.DN:
				r++;
				break;
			case Direction.RT:
				c++;
				break;
			default:
				break;
			}
	}
	/** 
	 * �����̒�`
	 * �s���W r �������������O�C
	 * �s���W����������΁C����W c �������������O�D
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Address o) {
		if (this.r < o.r) return -1;
		else if (this.r > o.r) return 1;
		else {
			if (this.c < o.c) return -1;
			else if (this.c > o.c) return 1;
			else return 0; 
		}
	}
	
	/**
	 * �}�X����}�X�ւ̌������擾����
	 * @param pos0 �n�_�}�X���W
	 * @param pos1 �I�_�}�X���W
	 * @return �n�_����I�_�ւ̌�����\���萔��Ԃ��B������ɂȂ��ꍇ��-1��Ԃ��B
	 */
	public static int getDirectionTo(Address pos0, Address pos1) {
		int r0 = pos0.r();
		int r1 = pos1.r();
		int c0 = pos0.c();
		int c1 = pos1.c();
		int ret = -1;
		if (r0 == r1) {
			if (c0 < c1)
				ret = Direction.RT;
			else if (c0 > c1)
				ret = Direction.LT;
		} else if (c0 == c1) {
			if (r0 < r1)
				ret = Direction.DN;
			else if (r0 > r1)
				ret = Direction.UP;
		}
		return ret;
	}

	/**
	 * �}�X�ւ̂��擾����
	 * @param pos �I�_�}�X���W
	 * @return �I�_�}�X�ւ̌�����\���萔��Ԃ��B������ɂȂ��ꍇ��-1��Ԃ��B
	 */
	public int getDirectionTo(Address pos) {
		return Address.getDirectionTo(this, pos);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return ""+'['+r+','+c+']';
	}
}
