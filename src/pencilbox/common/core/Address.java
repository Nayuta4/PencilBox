package pencilbox.common.core;


/**
 * �����l�̍��W��\���N���X
 */
public class Address implements Comparable<Address> {

	private static int MAX = -2;
	private static Address[][] ADDRESS; // = new Address[MAX+2][MAX+2];

	static {
		createAddressInstances(11);
	}

	public static void createAddressInstances(int m) {
		if (m <= MAX)
			return;
		Address[][] newAddress = new Address[m+2][m+2];
		for (int r = -1; r <= m; r++)
			for (int c = -1; c <= m; c++)
				if (r+1 <= MAX && c+1 <= MAX) {
					newAddress[r+1][c+1] = ADDRESS[r+1][c+1];
				} else {
					newAddress[r+1][c+1] = new Address(r, c);
//					System.out.println(newAddress[r+1][c+1].toString() + " ���쐬����");
				}
		MAX = m;
		ADDRESS = newAddress;
	}

	public static void createAddressInstances(Size size) {
		int m = size.getCols() > size.getCols() ? size.getRows() : size.getCols();
		createAddressInstances(m);
	}

	/**
	 *  �ՊO���W
	 */
	public static final Address NOWHERE = address(-1, -1);

	/**
	 *  �s���W
	 */
	private final int r;
	/**
	 *  ����W
	 */
	private final int c;

	/**
	 * �R���X�g���N�^
	 */
	protected Address(){
		this(0,0);
	}
	/**
	 * �R���X�g���N�^�C�����̍��W�Ɠ��������W�ɐݒ�
	 * @param pos �ݒ肷����W
	 */
	protected Address(Address pos){
		this.r = pos.r;
		this.c = pos.c;
	}
	/**
	 * �R���X�g���N�^�C�����̍��W�ɐݒ�
	 * @param r �ݒ肷��s���W
	 * @param c �ݒ肷�����W
	 */
	protected Address(int r, int c){
		this.r = r;
		this.c = c;
	}

	/**
	 * �t�@�N�g�����\�b�h
	 */
	public static Address address() {
		return new Address();
	}

	/**
	 * �t�@�N�g�����\�b�h
	 * @param pos ���W
	 * @return �������W�Ɠ������W
	 */
	public static Address address(Address pos) {
		return new Address(pos);
	}

	/**
	 * �^����ꂽ���W��Address���擾����
	 * @param r �s���W
	 * @param c ����W
	 * @return �������W�������W
	 */
	public static Address address(int r, int c){
		if (r >= -1 && r <= MAX)
			if (c >= -1 && c <= MAX)
				return ADDRESS[r+1][c+1];
		return new Address(r, c);
	}

	/**
	 * �ՊO���W��Ԃ�
	 * @return
	 */
	public static Address nowhere() {
		return NOWHERE;
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
	 * �����̍��W�Ɠ����������r����
	 * @param o ��r�Ώ�
	 * @return ���̍��W�ƈ����̍��W����������� true
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Address))
			return false;
		Address address = (Address)o;
		if (address == this)
			return true;
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

	/**
	 * �Z������direction �����̃Z��
	 */
	public static Address nextCell(Address p, int direction) {
		int r = p.r();
		int c = p.c();
		switch (direction) {
		case Direction.UP:
			return address(r-1, c);
		case Direction.LT:
			return address(r, c-1);
		case Direction.DN:
			return address(r+1, c);
		case Direction.RT:
			return address(r, c+1);
		case Direction.LTUP:
			return address(r-1, c-1);
		case Direction.LTDN:
			return address(r+1, c-1);
		case Direction.RTDN:
			return address(r+1, c+1);
		case Direction.RTUP:
			return address(r-1, c+1);
		default:
			return Address.NOWHERE;
		}
	}

	public Address nextCell(int direction) {
		return Address.nextCell(this, direction);
	}
	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return ""+'['+r+','+c+']';
	}
}
