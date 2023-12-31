package pencilbox.shikaku;

import pencilbox.common.core.Address;

/**
 * 「四角に切れ」四角クラス
 */
public class Square extends pencilbox.common.core.SquareBase {

	static final int NO_NUMBER = 0;
	static final int MULTIPLE_NUMBER = -2;
	private static int NEXT_ID = 1;

	private int id;  // 領域番号

	private int number; // 四角の数字

	/**
	 * コンストラクタ
	 * @param ra 一方の角の行座標
	 * @param ca 一方の角の列座標
	 * @param rb 他方の角の行座標
	 * @param cb 他方の角の列座標
	 */
	public Square(int ra, int ca, int rb, int cb) {
		super(ra, ca, rb, cb);
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
	}
	/**
	 * コンストラクタ
	 * @param posA 一方の角の座標
	 * @param posB 他方の角の座標
	 */
	public Square(Address posA, Address posB) {
		this(posA.r(), posA.c(), posB.r(), posB.c());
	}
	/**
	 * コンストラクタ
	 * 引数の四角と同じ座標の四角を作成する。
	 * @param s 四角
	 */
	public Square(Square s) {
		this(s.r0(), s.c0(), s.r1(), s.c1());
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
