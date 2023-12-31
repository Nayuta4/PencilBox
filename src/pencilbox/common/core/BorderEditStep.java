package pencilbox.common.core;

public class BorderEditStep extends AbstractStep {

	private SideAddress pos;
	private int before;
	private int after;
	/**
	 * コンストラクタ
	 * @param p 変更された辺の座標
	 * @param b 変更前の状態
	 * @param a 変更後の状態
	 */
	public BorderEditStep(SideAddress p, int b, int a) {
		super();
		pos = p;
		before = b;
		after = a;
	}

	public SideAddress getPos() {
		return pos;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}
}
