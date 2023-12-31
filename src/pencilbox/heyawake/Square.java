package pencilbox.heyawake;

import pencilbox.common.core.Address;

/**
 * 「へやわけ」部屋クラス
 */
public class Square extends pencilbox.common.core.SquareBase {

	static final int ANY = -1;

	private int number;
	private int nBlack;  // 黒確定マス数
	private int nWhite;  // 白確定マス数
	/**
	 * コンストラクタ
	 * 両対角点の座標と数字から部屋を作成
	 * @param ra 一方の角の行座標
	 * @param ca 一方の角の列座標
	 * @param rb 他方の角の行座標
	 * @param cb 他方の角の列座標
	 * @param number 部屋の数字
	 */
	public Square(int ra, int ca, int rb, int cb, int number){
		super(ra, ca, rb, cb);
		this.setNumber(number);
	}
	/**
	 * コンストラクタ
	 * 両対角点の座標から 数字なしの部屋を作成
	 * @param ra 一方の角の行座標
	 * @param ca 一方の角の列座標
	 * @param rb 他方の角の行座標
	 * @param cb 他方の角の列座標
	 */
	public Square(int ra, int ca, int rb, int cb){
		super(ra, ca, rb, cb);
		this.setNumber(ANY);
	}
	/**
	 * コンストラクタ
	 * 両対角点の座標から 数字なしの部屋を作成
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
	 * @param number The number to set.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param nBlack The nBlack to set.
	 */
	void setNBlack(int nBlack) {
		this.nBlack = nBlack;
	}
	/**
	 * @return Returns the nBlack.
	 */
	int getNBlack() {
		return nBlack;
	}
	/**
	 * @param nWhite The nWhite to set.
	 */
	void setNWhite(int nWhite) {
		this.nWhite = nWhite;
	}
	/**
	 * @return Returns the nWhite.
	 */
	int getNWhite() {
		return nWhite;
	}
	/**
	 * 
	 */
	public void clear() {
		setNBlack(0);
		setNWhite(0);
	}

	public String toString() {
		return super.toString() + ","+getNumber();
	}

	int mx() {

		int min;
		int max;
		int mx;

		if (this.sizeC() < this.sizeR()) {
			min = this.sizeC();
			max = this.sizeR();
		} else {
			min = this.sizeR();
			max = this.sizeC();
		}

		if (min == 1) {
			mx = (max + 1) / 2;
		} else if (min == 3) {
			mx = (max / 4) * 5;

			switch (max & 0x3) {
				case 1:
					mx += 2;
					break;
				case 2:
					mx += 3;
					break;
				case 3:
					mx += 5;
					break;
			}
		} else if ((min & max & 0x1) != 0) {
			if (min == max && (max & (max + 1)) == 0) {
				mx = (min * max + min + max) / 3;
			} else {
				mx = (min * max + min + max - 1) / 3;
			}
		} else {
			mx = (min * max + min + max - 2) / 3;
		}

		return mx;
	}
}
