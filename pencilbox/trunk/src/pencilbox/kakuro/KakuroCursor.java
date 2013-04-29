package pencilbox.kakuro;

import pencilbox.common.gui.CellCursor;

/**
 * 「カックロ」問題入力用カーソルクラス
 */
public class KakuroCursor extends CellCursor {
	
	static final int UPPER = 1;
	static final int LOWER = 0;
	/**
	 * 黒マスの斜線の上か下か
	 * 0 は下 1 は上
	 */
	private int stair;
	/**
	 * @return Returns the stair.
	 */
	int getStair() {
		return stair;
	}
	/**
	 * @param stair The stair to set.
	 */
	void setStair(int stair) {
		this.stair = stair;
	}
	/**
	 * 「カックロ」問題入力用カーソルを作成する
	 */
	public KakuroCursor() {
		super();
		stair = 0;
	}

}
