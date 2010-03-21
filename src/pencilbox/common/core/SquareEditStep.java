package pencilbox.common.core;

public class SquareEditStep extends AbstractStep {
	
	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private int r0;
	private int c0;
	private int r1;
	private int c1;
	private int operation;

	/**
	 * コンストラクタ
	 * @param sq 操作れた領域
	 * @param operation 操作の種類：追加されたのか，除去されたのか
	 */
	public SquareEditStep(int r0, int c0, int r1, int c1, int operation) {
		super();
		this.r0 = r0;
		this.c0 = c0;
		this.r1 = r1;
		this.c1 = c1;
		this.operation = operation;
	}

	public int getR0() {
		return r0;
	}

	public int getC0() {
		return c0;
	}

	public int getR1() {
		return r1;
	}

	public int getC1() {
		return c1;
	}

	public int getOperation() {
		return operation;
	}
	
}
