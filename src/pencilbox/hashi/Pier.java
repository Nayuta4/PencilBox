package pencilbox.hashi;

import pencilbox.common.core.Address;

/**
 * 「橋をかけろ」橋脚
 */
class Pier {
	private Address pos ;
	private Pier[] nextPier = new Pier[4];
	private Bridge[] bridge = new Bridge[4];
	private int number;
	private int chain;
	private int exit; // Solverで使用

	/**
	 * コンストラクタ
	 * @param r 行座標
	 * @param c 列座標
	 * @param n 数字
	 */
	Pier(int r, int c, int n) {
		this.setPos(new Address(r,c));
		number = n;
	}
	int totalBridges() {
		int ret = 0;
		for (int d=0; d<4; d++) {
			ret += getNBridge(d);
		}
		return ret;
	}
	int necessity() {
		return number - totalBridges();
	}

	int getNumber() {
		return number;
	}
	void setNumber(int n) {
		number = n;
	}
	int getNBridge(int d) {
		if (bridge[d] == null)
			return 0;
		else
			return bridge[d].getBridge();
	}
	void setNBridge(int d, int n) {
		if (bridge[d] == null)
			return;
		else
			bridge[d].setBridge(n);
	}
	void increaseBridge(int d) {
		bridge[d].increaseBridge();
	}
	void decreaseBridge(int d) {
		bridge[d].decreaseBridge();
	}
	int getCapacity(int d) {
		if (bridge[d] == null)
			return 0;
		else
			return bridge[d].getCapacity();
	}
	int totalCapacity() {
		int ret = 0;
		for (int d=0; d<4; d++) {
			ret += getCapacity(d);
		}
		return ret;
	}
	void limitCapacity(int d, int n) {
		if (bridge[d] == null)
			return;
		else
			bridge[d].limitCapacity(n);
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
			setNBridge(d, 0);
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
	 * @param exit The exit to set.
	 */
	void setExit(int exit) {
		this.exit = exit;
	}
	/**
	 * @return Returns the exit.
	 */
	int getExit() {
		return exit;
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
