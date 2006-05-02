package pencilbox.heyawake;

/**
 * 「へやわけ」部屋クラス
 */
public class Square {

	static final int ANY = -1;

	int r0;
	int c0;
	int r1;
	int c1;
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
}
