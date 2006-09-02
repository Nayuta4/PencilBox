package pencilbox.kakuro;

import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;

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
	 * Panelと関連付ける
	 * @param panel
	 */
	public KakuroCursor(PanelEventHandlerBase aPanel) {
		super(aPanel);
		stair = 0;
	}

	public void moveUp() {
//		if (panel.isProblemEditMode() && stair==LOWER) {
//			stair = UPPER;
//			panel.resetPreviousInput();
//		} 
//		else{
			setPosition(pos.r() - 1, pos.c());
			stair = LOWER;
//		}
	}
	public void moveLt() {
//		if (panel.isProblemEditMode() && stair==UPPER) {
//			stair = LOWER;
//			panel.resetPreviousInput();
//		} 
//		else {
			setPosition(pos.r(), pos.c() - 1);
			stair = UPPER;
//		}
	}
	public void moveDn() {
//		if (h==UPPER) h = LOWER;
//		else if (h==LOWER) {
			setPosition(pos.r() + 1, pos.c());
//		}
	}
	public void moveRt() {
//		if (h==LOWER) h = UPPER;
//		else if (h==UPPER) {
			setPosition(pos.r(), pos.c() + 1);
//		}
	}
}
