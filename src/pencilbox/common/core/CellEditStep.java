package pencilbox.common.core;


public class CellEditStep extends AbstractStep {
	
	private Address pos;
	private int before;
	private int after;

	/**
	 * コンストラクタ
	 * @param p 変更されたマスの座標
	 * @param b 変更前の状態
	 * @param a 変更後の状態
	 */
	public CellEditStep(Address p, int b, int a) {
		super();
		pos = p;
		before = b;
		after = a;
	}

	public Address getPos() {
		return pos;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}
	
	protected void setAfter(int a) {
		this.after = a;
	}
	
}
