package pencilbox.common.core;

import java.util.Set;
import java.util.TreeSet;

/**
 * 共通四角クラス
 */
public class SquareBase {

	private int r0;
	private int c0;
	private int r1;
	private int c1;

	/**
	 * コンストラクタ
	 * @param ra 一方の角の行座標
	 * @param ca 一方の角の列座標
	 * @param rb 他方の角の行座標
	 * @param cb 他方の角の列座標
	 */
	public SquareBase(int ra, int ca, int rb, int cb) {
		this.r0 = ra<rb?ra:rb;
		this.c0 = ca<cb?ca:cb;
		this.r1 = ra<rb?rb:ra;
		this.c1 = ca<cb?cb:ca;
	}
	/**
	 * コンストラクタ
	 * @param posA 一方の角の座標
	 * @param posB 他方の角の座標
	 */
	public SquareBase(Address posA, Address posB) {
		this(posA.r(), posA.c(), posB.r(), posB.c());
	}
	/**
	 * コンストラクタ
	 * 引数の四角と同じ座標の四角を作成する。
	 * @param s 四角
	 */
	public SquareBase(SquareBase s) {
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
	 * 四角の４隅のマス座標を左上，右上，左下，右下の順の長さ4の配列に入れて返す。
	 * @return
	 */
	public Address[] getCorners() {
		return new Address[] {Address.address(r0, c0), Address.address(r0, c1), Address.address(r1, c0), Address.address(r1, c1)};
	}

	public Address p0() {
		return Address.address(r0, c0);
	}

	public Address p1() {
		return Address.address(r1, c1);
	}

	public void set(Address p0, Address p1) {
		set(p0.r(), p0.c(), p1.r(), p1.c());
	}

	public void set(SquareBase a) {
		set(a.r0(), a.c0(), a.r1(), a.c1());
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
	 * 長方形領域の面積を返す
	 * @return 領域の面積
	 */
	public int getSquareSize() {
		return (r1-r0+1) * (c1-c0+1);
	}

	/**
	 * 四角の形状が等しいか
	 * @param o 比較対象の四角
	 * @return 四角の角の座標が一致すれば true
	 */
	public boolean equals(SquareBase o) {
		if (this == o) 
			return true;
		if (this.r0 == o.r0 && this.c0 == o.c0 && this.r1 == o.r1 && this.c1 == o.c1)
			return true;
		return false;
	}

	/**
	 * 長方形領域に含まれるマス座標の集合
	 * @return マス座標の集合
	 */
	public Set<Address> cellSet() {
		Set<Address> s = new TreeSet<Address>();
		for (int r = r0; r <= r1; r++) {
			for (int c = c0; c <= c1; c++) {
				s.add(Address.address(r, c));
			}
		}
		return s;
	}

	public String toString() {
		return "["+r0+","+c0+","+r1+","+c1+"]";
	}

}
