package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;

class BridgeEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = -1;

	private int row;
	private int col;
	private int direction;
	private int change;

	/**
	 * コンストラクタ
	 * @param r 変更された橋の起点マスの行座標
	 * @param c 変更された橋の起点マスの列座標
	 * @param dir マスからみた変更された橋の方向
	 * @param ch 追加されたのか，除去されたのか
	 */
	public BridgeEditStep(int r, int c, int dir, int ch) {
		super();
		row = r;
		col = c;
		direction = dir;
		change = ch;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getDirection() {
		return direction;
	}

	public int getChange() {
		return change;
	}
	
}
