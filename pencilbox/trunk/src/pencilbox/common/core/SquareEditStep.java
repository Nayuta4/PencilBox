package pencilbox.common.core;

public class SquareEditStep extends AbstractStep {
	
	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private Address p0 = Address.NOWHERE;
	private Address p1 = Address.NOWHERE;
	private Address q0 = Address.NOWHERE;
	private Address q1 = Address.NOWHERE;
	private int operation;

	/**
	 * コンストラクタ
	 * @param sq 操作れた領域
	 * @param operation 操作の種類：追加されたのか，除去されたのか
	 */
	public SquareEditStep(Address p0, Address p1, int operation) {
		super();
		if (operation == ADDED) {
			this.q0 = p0;
			this.q1 = p1;
		} else if (operation == REMOVED) {
			this.p0 = p0;
			this.p1 = p1;
		}
		this.operation = operation;
	}

	/**
	 * コンストラクタ
	 * @param p0 変更前の角の座標
	 * @param p1 変更前の角の座標
	 * @param q0 変更後の角の座標
	 * @param q1 変更後の角の座標
	 * @param operation 操作の種類：追加されたのか，除去されたのか，変更されたのか
	 */
	public SquareEditStep(Address p0, Address p1, Address q0, Address q1, int operation) {
		super();
		this.p0 = p0;
		this.p1 = p1;
		this.q0 = q0;
		this.q1 = q1;
		this.operation = operation;
	}

	public Address getP0() {
		return p0;
	}

	public Address getP1() {
		return p1;
	}

	public Address getQ0() {
		return q0;
	}

	public Address getQ1() {
		return q1;
	}

	public int getOperation() {
		return operation;
	}
	
}
