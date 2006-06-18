package pencilbox.common.core;


/**
 * 盤上の辺の整数値の座標を表すクラス
 */
public class SideAddress implements Comparable {
	
	public static final SideAddress NOWEHER = new SideAddress(0, -1,-1);
	
	private int d;
	private int r;
	private int c;
	
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

	/**
	 * @param d
	 * @param r
	 * @param c
	 */
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
	/**
	 * @return
	 */
	public boolean isNowhere() {
		return (r==-1 && c==-1);
	}
	/**
	 * 
	 */
	public void setNowhere() {
		d = 0;
		r = -1;
		c = -1;
	}
	/** 
	 * 順序の定義
	 * VERT が前で HORIZ が後．
	 * 行座標 r が小さい方が前，
	 * 行座標が等しければ，列座標 c が小さい方が前．
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
