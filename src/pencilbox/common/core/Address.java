package pencilbox.common.core;


/**
 * �����l�̍��W��\���N���X
 */
public class Address implements Comparable {
	
	/**
	 *  �ՊO���W
	 */
	public static final Address NOWEHER = new Address(-1,-1);
	
	/**
	 *  �s���W
	 */
	public int r;
	/**
	 *  ����W
	 */
	public int c;
	
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
	/**
	 * �����̍��W�Ə㉺���E�ɗאڂ��Ă��邩
	 * @param address
	 * @return �אڂ��Ă���� true
	 */
	public boolean isNextTo(Address address) {
		if ((address.r == r && (address.c == c-1 || address.c == c+1))
			|| (address.c == c && (address.r == r-1 || address.r == r+1)))
		return true;
		else
			return false;
	}
	/**
	 * �����̍��W�Ɠ����s�܂��͗�
	 * @param address ��r���� Address
	 * @return ���꒼����Ȃ� true
	 */
	public boolean isInLine(Address address) {
		if ( address.r == r || address.c == c )
			return true;
		else
			return false;
	}
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
//	public void moveUp() {
//		r--;
//	}
//	public void moveLt() {
//		c--;
//	}
//	public void moveDn() {
//		r++;
//	}
//	public void moveRt() {
//		c++;
//	}
	/** 
	 * �����̒�`
	 * �s���W r �������������O�C
	 * �s���W����������΁C����W c �������������O�D
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		Address other = (Address) o;
		if (this.r < other.r) return -1;
		else if (this.r > other.r) return 1;
		else {
			if (this.c < other.c) return -1;
			else if (this.c > other.c) return 1;
			else return 0; 
		}
	}
	
	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return ""+'['+r+','+c+']';
	}
}
