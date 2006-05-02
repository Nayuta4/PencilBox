package pencilbox.shikaku;

/**
 * 「四角に切れ」四角クラス
 */
/*
 * int でなく Address を主体の引数とするようにしたい
 * heyawake.Square と統一したい
 * r0, c0, r1, c1 を private にしたい
 */
public class Square {
	
	static final int NO_NUMBER = 0;
	static final int MULTIPLE_NUMBER = -2;
	private static int NEXT_ID = 1;
	
	private int id;  // 領域番号
	
	int r0;
	int c0;
	int r1;
	int c1;
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
