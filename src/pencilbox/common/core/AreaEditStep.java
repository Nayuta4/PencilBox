package pencilbox.common.core;

/**
 * １手の操作を表すクラス
 * UNDO, REDO での編集の単位となる
 */
public class AreaEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private Address pos;
	private Address p0;
	private int operation;

	/**
	 * コンストラクタ
	 * @param r 変更されたマスの座標
	 * @param r0 変更された領域の代表マスの座標
	 */
	public AreaEditStep(Address p, Address p0, int operation) {
		super();
		this.pos = p;
		this.p0 = p0;
		this.operation = operation;
	}

	public Address getPos() {
		return pos;
	}

	public Address getP0() {
		return p0;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getOperation() {
		return operation;
	}
	
	public String toString() {
		return pos.toString() + " from " + p0 + " changed " + getOperation() + " " + getType();
	}
	
}
