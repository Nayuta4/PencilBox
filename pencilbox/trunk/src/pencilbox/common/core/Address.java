package pencilbox.common.core;


/**
 * 整数値の座標を表すクラス
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
//					System.out.println(newAddress[r+1][c+1].toString() + " を作成した");
				}
		MAX = m;
		ADDRESS = newAddress;
	}

	public static void createAddressInstances(Size size) {
		int m = size.getCols() > size.getCols() ? size.getRows() : size.getCols();
		createAddressInstances(m);
	}

	/**
	 *  盤外座標
	 */
	public static final Address NOWHERE = address(-1, -1);

	/**
	 *  行座標
	 */
	private final int r;
	/**
	 *  列座標
	 */
	private final int c;

	/**
	 * コンストラクタ
	 */
	protected Address(){
		this(0,0);
	}
	/**
	 * コンストラクタ，引数の座標と等しい座標に設定
	 * @param pos 設定する座標
	 */
	protected Address(Address pos){
		this.r = pos.r;
		this.c = pos.c;
	}
	/**
	 * コンストラクタ，引数の座標に設定
	 * @param r 設定する行座標
	 * @param c 設定する列座標
	 */
	protected Address(int r, int c){
		this.r = r;
		this.c = c;
	}

	/**
	 * ファクトリメソッド
	 */
	public static Address address() {
		return new Address();
	}

	/**
	 * ファクトリメソッド
	 * @param pos 座標
	 * @return 引数座標と同じ座標
	 */
	public static Address address(Address pos) {
		return new Address(pos);
	}

	/**
	 * 与えられた座標のAddressを取得する
	 * @param r 行座標
	 * @param c 列座標
	 * @return 引数座標をもつ座標
	 */
	public static Address address(int r, int c){
		if (r >= -1 && r <= MAX)
			if (c >= -1 && c <= MAX)
				return ADDRESS[r+1][c+1];
		return new Address(r, c);
	}

	/**
	 * 盤外座標を返す
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
	 * 引数の座標と等しいかを比較する
	 * @param o 比較対象
	 * @return この座標と引数の座標が等しければ true
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
//	/**
//	 * 引数の座標と上下左右に隣接しているか
//	 * @param address
//	 * @return 隣接していれば true
//	 */
//	public boolean isNextTo(Address address) {
//		if ((address.r == r && (address.c == c-1 || address.c == c+1))
//			|| (address.c == c && (address.r == r-1 || address.r == r+1)))
//		return true;
//		else
//			return false;
//	}
//	/**
//	 * 引数の座標と同じ行または列か
//	 * @param address 比較する Address
//	 * @return 同一直線状なら true
//	 */
//	public boolean isInLine(Address address) {
//		if ( address.r == r || address.c == c )
//			return true;
//		else
//			return false;
//	}
	/**
	 * 盤外座標かどうか
	 * @return 座標が盤外点であれば true
	 */
	public boolean isNowhere() {
		return (r==-1 && c==-1);
	}

	/** 
	 * 順序の定義
	 * 行座標 r が小さい方が前，
	 * 行座標が等しければ，列座標 c が小さい方が前．
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
	 * マスからマスへの向きを取得する
	 * @param pos0 始点マス座標
	 * @param pos1 終点マス座標
	 * @return 始点から終点への向きを表す定数を返す。同一列上にない場合は-1を返す。
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
	 * マスへのを取得する
	 * @param pos 終点マス座標
	 * @return 終点マスへの向きを表す定数を返す。同一列上にない場合は-1を返す。
	 */
	public int getDirectionTo(Address pos) {
		return Address.getDirectionTo(this, pos);
	}

	/**
	 * セルからdirection 方向のセル
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
