package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;

class BridgeEditStep extends AbstractStep {

	private Address pos;
	private int direction;
	private int before;
	private int after;

	/**
	 * コンストラクタ
	 * @param p 変更された橋の起点マスの座標
	 * @param dir マスからみた変更された橋の方向
	 * @param b 変更前の橋の数
	 * @param a 変更後の橋の数
	 */
	public BridgeEditStep(Address p, int dir, int b, int a) {
		super();
		pos = p;
		direction = dir;
		before = b;
		after = a;
	}

	public Address getPos() {
		return pos;
	}

	public int getDirection() {
		return direction;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}

	public String toString() {
		return pos.toString() + getDirection() + " "+ getBefore() + "->" + getAfter() + " " + getType();
	}
	
}
