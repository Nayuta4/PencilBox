package pencilbox.common.core;


/**
 * 整数値の座標を表すクラス
 */
public class Address implements Comparable {
	
	/**
	 *  盤外座標
	 */
	public static final Address NOWEHER = new Address(-1,-1);
	
	/**
	 *  行座標
	 */
	public int r;
	/**
	 *  列座標
	 */
	public int c;
	
	/**
	 * コンストラクタ
	 */
	public Address(){
		this(0,0);
	}
	/**
	 * コンストラクタ，引数の座標と等しい座標に設定
	 * @param pos 設定する座標
	 */
	public Address(Address pos){
		this.r = pos.r;
		this.c = pos.c;
	}
	/**
	 * コンストラクタ，引数の座標に設定
	 * @param r 設定する行座標
	 * @param c 設定する列座標
	 */
	public Address(int r, int c){
		this.r = r;
		this.c = c;
	}
	/**
	 * 引数の座標に設定
	 * @param pos 設定する座標
	 */
	public void set(Address pos){
		this.r = pos.r;
		this.c = pos.c;
	}
	/**
	 * 引数の座標に設定
	 * @param r 設定する行座標
	 * @param c 設定する列座標
	 */
	public void set(int r, int c){
		this.r = r;
		this.c = c;
	}
	/**
	 * 引数の座標と等しいかを比較する
	 * @param o 比較対象
	 * @return この座標と引数の座標が等しければ true
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
	 * 引数の座標と等しいかを比較する
	 * @param rr 比較対象の行座標
	 * @param cc 比較対象の列座標
	 * @return この座標と引数の座標が等しければ true
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
	 * 引数の座標と上下左右に隣接しているか
	 * @param address
	 * @return 隣接していれば true
	 */
	public boolean isNextTo(Address address) {
		if ((address.r == r && (address.c == c-1 || address.c == c+1))
			|| (address.c == c && (address.r == r-1 || address.r == r+1)))
		return true;
		else
			return false;
	}
	/**
	 * 引数の座標と同じ行または列か
	 * @param address 比較する Address
	 * @return 同一直線状なら true
	 */
	public boolean isInLine(Address address) {
		if ( address.r == r || address.c == c )
			return true;
		else
			return false;
	}
	/**
	 * 盤外座標かどうか
	 * @return 座標が盤外点であれば true
	 */
	public boolean isNowhere() {
		return (r==-1 && c==-1);
	}
	/**
	 * 盤外座標に設定する
	 */
	public void setNowhere() {
		r = -1;
		c = -1;
	}
	/**
	 * 隣のマス座標に移動する
	 * @param direction 移動する向き
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
	 * 順序の定義
	 * 行座標 r が小さい方が前，
	 * 行座標が等しければ，列座標 c が小さい方が前．
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
