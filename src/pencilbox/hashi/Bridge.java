package pencilbox.hashi;

/**
 * 橋クラス
 * ２つの数字に挟まれた橋をかけることのできるものを表す
 */
class Bridge {
	
	private int bridge = 0;
	private int capacity = 2;
	
	private Pier pier0; // Solverのみで使用
	private Pier pier1; // Solverのみで使用
	
	Bridge(Pier p0, Pier p1) {
		pier0 = p0;
		pier1 = p1;
	}

	void limitCapacity(int n) {
		if (capacity > n) 
			capacity = n;
	}
	void increaseBridge() {
		bridge ++;
		capacity --;
	}
	void decreaseBridge() {
		bridge --;
		capacity ++;
	}
	void decreaseCapacity() {
		capacity --;
	}
	int getBridge() {
		return bridge;
	}
	void setBridge(int n) {
		bridge = n;
	}
	int getCapacity() {
		return capacity;
	}
	void setCapacity(int n) {
		capacity = n;
	}

	/**
	 * @param pier0 The pier0 to set.
	 */
	void setPier0(Pier pier0) {
		this.pier0 = pier0;
	}

	/**
	 * @return Returns the pier0.
	 */
	Pier getPier0() {
		return pier0;
	}

	/**
	 * @param pier1 The pier1 to set.
	 */
	void setPier1(Pier pier1) {
		this.pier1 = pier1;
	}

	/**
	 * @return Returns the pier1.
	 */
	Pier getPier1() {
		return pier1;
	}

}
