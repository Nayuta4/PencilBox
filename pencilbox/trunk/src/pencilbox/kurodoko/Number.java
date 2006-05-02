package pencilbox.kurodoko;

/**
 * 「黒マスはどこだ」数字クラス
 */
class Number {
	
	private int number;
	private int nSpace[] = new int[4];
	private int nWhite[] = new int[4];
	
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
	public int getNSpace(int direction) {
		return nSpace[direction];
	}
	public void setNSpace(int direction, int n) {
		nSpace[direction] = n;
	}
	public int getNWhite(int direction) {
		return nWhite[direction];
	}
	public void setNWhite(int direction, int n) {
		nWhite[direction] = n;
	}
	public int getSumSpace() {
		int ret = 1;
		for (int d=0; d<4; d++)
			ret += nSpace[d];
		return ret;
	}
	public int getSumWhite() {
		int ret = 1;
		for (int d=0; d<4; d++)
			ret += nWhite[d];
		return ret;
	}
	
	boolean tooSmallSpace() {
		if (number == Board.UNDECIDED_NUMBER) return false;
		return getSumSpace() < number;		
	}
	boolean tooLargeWhite() {
		if (number == Board.UNDECIDED_NUMBER) return false;
		return getSumWhite() > number;		
	}
	

}
