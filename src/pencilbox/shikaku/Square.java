package pencilbox.shikaku;

/**
 * 「四角に切れ」四角クラス
 */
public class Square {
	
	static final int NO_NUMBER = 0;
	static final int MULTIPLE_NUMBER = -2;
	private static int NEXT_ID = 1;
	
	private int id;  // 領域番号
	
	private int r0;
	private int c0;
	private int r1;
	private int c1;
	private int number; // 四角の数字
	
	/**
	 * コンストラクタ
	 * @param ra 一方の角の行座標
	 * @param ca 一方の角の列座標
	 * @param rb 他方の角の行座標
	 * @param cb 他方の角の列座標
	 */
	public Square(int ra, int ca, int rb, int cb) {
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	/**
	 * コンストラクタ
	 * 引数の四角と同じ座標の四角を作成する。
	 * @param s 四角
	 */
	public Square(Square s) {
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
	 * 座標の設定
	 * @param ra 一方の角の行座標
	 * @param ca 一方の角の列座標
	 * @param rb 他方の角の行座標
	 * @param cb 他方の角の列座標
	 */
	public void set(int ra, int ca, int rb, int cb) {
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	
	/**
	 * 四角の1つの頂点を固定したまま，対角位置の頂点の座標を変更する。
	 * @param rOld 変更前の頂点の行座標
	 * @param cOld 変更前の頂点の行座標
	 * @param rNew 変更後の頂点の行座標
	 * @param cNew 変更後の頂点の行座標
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
	 * 長方形領域の面積を返す
	 * @return 領域の面積
	 */
	public int getSquareSize() {
		return (r1-r0+1) * (c1-c0+1);
	}
	
	public String toString() {
		return "["+r0+","+c0+","+r1+","+c1+"]";
	}
	/**
	 * @return 数字
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param i
	 */
	public void setNumber(int i) {
		number = i;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}

}
