package pencilbox.kurodoko;

/**
 * 「黒マスはどこだ」数字クラス
 */
class Number {

	private int number;
	private int nSpace;
	private int nWhite;

	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number The number to set.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	public Number() {
	}
	public Number(int number) {
		this.number = number;
	}
	public int getNSpace() {
		return nSpace;
	}
	public void setNSpace(int n) {
		nSpace = n;
	}
	public int getNWhite() {
		return nWhite;
	}
	public void setNWhite(int n) {
		nWhite = n;
	}
}
