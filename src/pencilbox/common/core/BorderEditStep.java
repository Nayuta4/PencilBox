package pencilbox.common.core;

public class BorderEditStep extends AbstractStep {

	private int direction;
	private int row;
	private int col;
	private int before;
	private int after;
	/**
	 * コンストラクタ
	 * @param d 横か縦か
	 * @param r 変更されたマスの行座標
	 * @param c 変更されたマスの列座標
	 * @param b 変更前の状態
	 * @param a 変更後の状態
	 */
	public BorderEditStep(int d, int r, int c, int b, int a) {
		super();
		direction = d;
		row = r;
		col = c;
		before = b;
		after = a;
	}

	public int getDirection() {
		return direction;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}
}
