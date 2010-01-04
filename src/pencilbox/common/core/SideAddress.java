package pencilbox.common.core;


/**
 * 盤上の辺の整数値の座標を表すクラス
 */
public class SideAddress implements Comparable<SideAddress> {
	
	public static final SideAddress NOWHERE = new SideAddress(-1, -1,-1);
	
	private int d;
	private int r;
	private int c;
	
	/**
	 * マス座標と向きから辺座標を取得する。
	 * @param pos マス座標
	 * @param dir 向き
	 * @return 辺座標
	 */
	public static SideAddress get(Address pos, int dir) {
		switch (dir) {
		case Direction.UP :
			return new SideAddress(Direction.HORIZ, pos.r()-1, pos.c());
		case Direction.LT :
			return new SideAddress(Direction.VERT, pos.r(), pos.c()-1);
		case Direction.DN :
			return new SideAddress(Direction.HORIZ, pos.r(), pos.c());
		case Direction.RT :
			return new SideAddress(Direction.VERT, pos.r(), pos.c());
		default :
			return SideAddress.NOWHERE;
		}
	}
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
	
	public void set(SideAddress pos) {
		this.d = pos.d();
		this.r = pos.r();
		this.c = pos.c();
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
	
	public static Address nextCellFromBorder(SideAddress border, int direction) {
		int d = border.d();
		int r = border.r();
		int c = border.c();
		if (d==0) {
			if (direction == 0)
				return new Address(r, c);
			else if (direction == 1)
				return new Address(r, c+1);
		} else if (d==1) {
			if (direction == 0)
				return new Address(r, c);
			else if (direction == 1)
				return new Address(r+1, c);
		}
		return null;
	}

	public String toString(){
		return "["+d+","+r+","+c+"]";
	}

}
