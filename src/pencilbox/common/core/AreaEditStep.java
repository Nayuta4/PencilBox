package pencilbox.common.core;

/**
 * １手の操作を表すクラス
 * UNDO, REDO での編集の単位となる
 */
public class AreaEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private int r;
	private int c;
	private int r0;
	private int c0;
	private int operation;

	/**
	 * コンストラクタ
	 * @param r 変更されたマスの行座標
	 * @param c 変更されたマスの列座標
	 * @param r0 変更された領域の代表マスの行座標
	 * @param c0 変更された領域の代表マスの列座標
	 */
	public AreaEditStep(int r, int c, int r0, int c0, int operation) {
		super();
		this.r = r;
		this.c = c;
		this.r0 = r0;
		this.c0 = c0;
		this.operation = operation;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

	public int getR0() {
		return r0;
	}

	public int getC0() {
		return c0;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getOperation() {
		return operation;
	}
	
}
