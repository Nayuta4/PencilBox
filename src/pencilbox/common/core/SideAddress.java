package pencilbox.common.core;


/**
 * �Տ�̕ӂ̐����l�̍��W��\���N���X
 */
public class SideAddress implements Comparable<SideAddress> {

	private static int MAX = -2;
	private static SideAddress[][][] ADDRESS; // = new SideAddress[2][MAX][MAX];

	static {
		createSideAddressInstances(11);
	}

	public static void createSideAddressInstances(int m) {
		if (m <= MAX)
			return;
		SideAddress[][][] newAddress = new SideAddress[2][m+2][m+2];
		for (int d = 0; d <= 1; d++)
			for (int r = -1; r <= m; r++)
				for (int c = -1; c <= m; c++)
					if (r+1 <= MAX && c+1 <= MAX) {
						newAddress[d][r+1][c+1] = ADDRESS[d][r+1][c+1];
					} else {
						newAddress[d][r+1][c+1] = new SideAddress(d, r, c);
//						System.out.println(newAddress[d][r+1][c+1].toString() + " ���쐬����");
					}
		MAX = m;
		ADDRESS = newAddress;
	}

	public static void createSideAddressInstances(Size size) {
		int m = size.getCols() > size.getCols() ? size.getRows() : size.getCols();
		createSideAddressInstances(m);
	}

	/**
	 *  �ՊO���W
	 */
	public static final SideAddress NOWHERE = sideAddress(-1, -1, -1);

	/**
	 *  0 �� VERT �܂� �}�X�̍��E�̋��E���i�����N�̂Ƃ��͉����j
	 *  1 �� HORIZ �܂� �}�X�̏㉺�̋��E���i�����N�̂Ƃ��͏c���j
	 */
	private final int d;
	private final int r;
	private final int c;

	/**
	 * �t�@�N�g�����\�b�h�B�����Ɠ����Ӎ��W���擾����B
	 * @param d
	 * @param r
	 * @param c
	 * @return
	 */
	public static SideAddress sideAddress(int d, int r, int c) {
		if (d >= 0 && d <= 1)
			if (r >= -1 && r <= MAX)
				if (c >= -1 && c <= MAX) 
					return SideAddress.ADDRESS[d][r+1][c+1];
		return new SideAddress(d, r, c);
	}

	/**
	 * �}�X���W�ƌ�������Ӎ��W���擾����B
	 * @param pos �}�X���W
	 * @param dir ����
	 * @return �Ӎ��W
	 */
	public static SideAddress get(Address pos, int dir) {
		switch (dir) {
		case Direction.UP :
			return sideAddress(Direction.HORIZ, pos.r()-1, pos.c());
		case Direction.LT :
			return sideAddress(Direction.VERT, pos.r(), pos.c()-1);
		case Direction.DN :
			return sideAddress(Direction.HORIZ, pos.r(), pos.c());
		case Direction.RT :
			return sideAddress(Direction.VERT, pos.r(), pos.c());
		default :
			return SideAddress.NOWHERE;
		}
	}

	/**
	 * @param d
	 * @param r
	 * @param c
	 */
	private SideAddress(int d, int r, int c) {
		this.d = d;
		this.r = r;
		this.c = c;
	}

	/**
	 * @return Returns the d.
	 */
	public int d() {
		return d;
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

	public boolean equals(Object o) {
		if (!(o instanceof SideAddress))
			return false;
		SideAddress address = (SideAddress) o;
		if (address == this)
			return true;
		if (address.r == r && address.c == c && address.d == d)
			return true;
		else
			return false;
	}

	public int hashCode() {
		return ((d * 1000) + r) * 1000 + c;
	}
	/** 
	 * �����̒�`
	 * VERT ���O�� HORIZ ����D
	 * �s���W r �������������O�C
	 * �s���W����������΁C����W c �������������O�D
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(SideAddress o) {
		if (this.d < o.d) return -1;
		else if (this.d > o.d) return 1;
		else {
			if (this.r < o.r) return -1;
			else if (this.r > o.r) return 1;
			else {
				if (this.r < o.r) return -1;
				if (this.c < o.c) return -1;
				else if (this.c > o.c) return 1;
				else return 0;
			}
		}
	}

	/**
	 * ���E�̗��ׂ̋��E
	 * @param border ���E�̍��W
	 * @param direction 0 �Ȃ� ��܂��͍��A1�Ȃ牺�܂��͉E
	 * @return
	 */
	public static SideAddress nextBorder(SideAddress border, int direction) {
		int d = border.d();
		int r = border.r();
		int c = border.c();
		int dd = -1, rr = -1, cc = -1;
		if (d==Direction.VERT) {
			if (direction == 0) { dd = Direction.VERT  ; rr = r   ; cc = c-1; }
			else if (direction == 1) { dd = Direction.VERT  ; rr = r   ; cc = c+1; }
			else if (direction == 2) { dd = Direction.HORIZ ; rr = r-1 ; cc = c  ; }
			else if (direction == 3) { dd = Direction.HORIZ ; rr = r-1 ; cc = c+1; }
			else if (direction == 4) { dd = Direction.HORIZ ; rr = r   ; cc = c  ; }
			else if (direction == 5) { dd = Direction.HORIZ ; rr = r   ; cc = c+1; }
		} else if (d==Direction.HORIZ) {
			if (direction == 0) { dd = Direction.HORIZ ; rr = r-1 ; cc = c  ; }
			else if (direction == 1) { dd = Direction.HORIZ ; rr = r+1 ; cc = c  ; }
			else if (direction == 2) { dd = Direction.VERT  ; rr = r   ; cc = c-1; }
			else if (direction == 3) { dd = Direction.VERT  ; rr = r+1 ; cc = c-1; }
			else if (direction == 4) { dd = Direction.VERT  ; rr = r   ; cc = c  ; }
			else if (direction == 5) { dd = Direction.VERT  ; rr = r+1 ; cc = c  ; }
		}
		return sideAddress(dd, rr, cc);
	}

	public static Address nextCellFromBorder(SideAddress border, int direction) {
		int d = border.d();
		int r = border.r();
		int c = border.c();
		if (d==0) {
			if (direction == 0)
				return Address.address(r, c);
			else if (direction == 1)
				return Address.address(r, c+1);
		} else if (d==1) {
			if (direction == 0)
				return Address.address(r, c);
			else if (direction == 1)
				return Address.address(r+1, c);
		}
		return null;
	}

	public String toString(){
		return "["+d+","+r+","+c+"]";
	}

}
