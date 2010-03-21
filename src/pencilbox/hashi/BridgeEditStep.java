package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;

class BridgeEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = -1;

	private Address pos;
	private int direction;
	private int change;

	/**
	 * コンストラクタ
	 * @param p 変更された橋の起点マスの座標
	 * @param dir マスからみた変更された橋の方向
	 * @param ch 追加されたのか，除去されたのか
	 */
	public BridgeEditStep(Address p, int dir, int ch) {
		super();
		pos = new Address(p);
		direction = dir;
		change = ch;
	}

	public Address getPos() {
		return pos;
	}

	public int getDirection() {
		return direction;
	}

	public int getChange() {
		return change;
	}
	
}
