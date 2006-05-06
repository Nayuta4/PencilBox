package pencilbox.common.core;


/**
 * �Տ�̕ӂ̐����l�̍��W��\���N���X
 */
public class SideAddress extends Address {
	
	public static final SideAddress NOWEHER = new SideAddress(0, -1,-1);
	
	public int d;
//	public int r;
//	public int c;
	
	/**
	 * 
	 */
	public SideAddress() {
		this(0, 0, 0);
	}

	/**
	 * @param pos
	 */
	public SideAddress(SideAddress pos) {
		this(pos.d, pos.r, pos.c);
	}

	/**
	 * @param d
	 * @param r
	 * @param c
	 */
	public SideAddress(int d, int r, int c) {
		this.d = d;
		this.r = r;
		this.c = c;
	}
	public void set(int d, int r, int c){
		this.d = d;
		this.r = r;
		this.c = c;
	}
	public boolean equals(Object o) {
		if (!(o instanceof SideAddress))
			return false;
		SideAddress address = (SideAddress) o;
		if (address.r == r && address.c == c && address.d == d)
			return true;
		else
			return false;
	}
	public int hashCode() {
		return ((d * 1000) + r) * 1000 + c;
	}
	public boolean isNowhere() {
		return (r==-1 && c==-1);
	}
	public void setNowhere() {
		d = 0;
		r = -1;
		c = -1;
	}
	/** 
	 * �����̒�`
	 * VERT ���O�� HORIZ ����D
	 * �s���W r �������������O�C
	 * �s���W����������΁C����W c �������������O�D
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		SideAddress other = (SideAddress) o;
		if (this.d < other.d) return -1;
		else if (this.d > other.d) return 1;
		else {
			if (this.r < other.r) return -1;
			else if (this.r > other.r) return 1;
			else {
				if (this.r < other.r) return -1;
				if (this.c < other.c) return -1;
				else if (this.c > other.c) return 1;
				else return 0; 
			}
		}
	}
	public String toString(){
		return "["+d+","+r+","+c+"]";
	}

}