package pencilbox.hashi;

import pencilbox.common.core.Address;

/**
 * 「橋をかけろ」橋脚
 */
public class Pier {
	private Address pos ;
	private Pier[] nextPier = new Pier[4];
	private Bridge[] bridge = new Bridge[4];
	private int number;
	private int chain;

	/**
	 * コンストラクタ
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 数字
	 */
	public Pier(Address p, int n) {
		pos = p;
		number = n;
	}
	int totalLines() {
		int ret = 0;
		for (int d=0; d<4; d++) {
			ret += getLine(d);
		}
		return ret;
	}

	int getNumber() {
		return number;
	}
	void setNumber(int n) {
		number = n;
	}
	int getLine(int d) {
		if (bridge[d] == null)
			return 0;
		else
			return bridge[d].getLine();
	}
	void setLine(int d, int n) {
		if (bridge[d] == null)
			return;
		else
			bridge[d].setLine(n);
	}
	void changeLine(int d, int n) {
		bridge[d].changeLine(n);
	}

	int getChain() {
		return chain;
	}
	void setChain(int i) {
		chain = i;
	}
	
	void clear() {
		chain = 0;
		for (int d=0; d<4; d++) 
			setLine(d, 0);
	}
	/**
	 * @param pos The pos to set.
	 */
	void setPos(Address pos) {
		this.pos = pos;
	}
	/**
	 * @return Returns the pos.
	 */
	Address getPos() {
		return pos;
	}
	/**
	 * @param d direction of the pier
	 * @param p The pier to set.
	 */
	void setNextPier(int d, Pier p) {
		this.nextPier[d] = p;
	}
	/**
	 * @param d direction to search pier.
	 * @return Returns the nextPier of direction d.
	 */
	Pier getNextPier(int d) {
		return nextPier[d];
	}
	/**
	 * @param d Directon to set bridge
	 * @param b The bridge to set.
	 */
	void setBridge(int d, Bridge b) {
		this.bridge[d] = b;
	}
	/**
	 * @param d Direction to search bridge.
	 * @return Returns the bridge.
	 */
	Bridge getBridge(int d) {
		return bridge[d];
	}

}
