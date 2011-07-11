package pencilbox.common.core;


public class CellEditStep extends AbstractStep {
	
	private Address pos;
	private int before;
	private int after;

	/**
	 * コンストラクタ
	 * @param t 操作種類
	 * @param p 変更されたマスの座標
	 * @param b 変更前の状態
	 * @param a 変更後の状態
	 */
	public CellEditStep(EditType t, Address p, int b, int a) {
		super();
		type = t;
		pos = p;
		before = b;
		after = a;
	}

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

	public boolean attachEdit(AbstractStep edit) {
		if (edit instanceof CellEditStep) {
			CellEditStep ed = (CellEditStep) edit;
			if (ed.getPos().equals(this.getPos())) {
				if (edit.getType() == EditType.NUMBER && this.getType() == EditType.NUMBER) {
					this.setAfter(ed.getAfter());
					return true;
				}
			}
		}
		return false;
	}

	public String toString() {
		return pos.toString() + getBefore() + "->" + getAfter() + " " + getType();
	}
}
