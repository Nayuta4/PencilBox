package pencilbox.tentaisho;

import pencilbox.common.core.Address;

/**
 * 「天体ショー」星用アドレス
 * 今のところただの目印
 */
public class StarAddress extends Address {

	/**
	 * 
	 */
	public StarAddress() {
		super();
	}

	/**
	 * @param pos
	 */
	public StarAddress(Address pos) {
		super(pos);
	}

	/**
	 * @param r
	 * @param c
	 */
	public StarAddress(int r, int c) {
		super(r, c);
	}

}
