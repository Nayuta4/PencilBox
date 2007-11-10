package pencilbox.heyawake;

import pencilbox.common.core.Address;

/**
 * 「へやわけ」部屋クラス
 */
public class Square {

	static final int ANY = -1;

	private int r0;
	private int c0;
	private int r1;
	private int c1;
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
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
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
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
		this.setNumber(ANY);
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
	 * 
	 */
	public void set(int ra, int ca, int rb, int cb) {
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	/**
	 * 四角の４隅のマス座標を左上，右上，左下，右下の順の長さ4の配列に入れて返す。
	 * @return
	 */
	public Address[] getCorners() {
		return new Address[] {new Address(r0, c0), new Address(r0, c1), new Address(r1, c0), new Address(r1, c1)};
	}
	/**
	 * 数字を設定する
	 * @param num
	 */
//	public void setNumber(int num) {
//		this.number = num;
//	}
	/**
	 * @return 部屋の列サイズ
	 */
	public int sizeC() {
		return c1 - c0 +1;
	}
	/**
	 * @return 部屋の行サイズ
	 */
	public int sizeR() {
		return r1 - r0 + 1;
	}
	/**
	 * @return 部屋の面積
	 */
	public int size() {
		return sizeR() * sizeC();
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
		return "["+r0+","+c0+","+r1+","+c1+","+getNumber()+"]";
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
