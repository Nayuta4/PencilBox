package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandler;

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
	public KakuroCursor(PanelEventHandler aPanel) {
		super(aPanel);
		pos = new Address();
		stair = 0;
		panel = aPanel;
	}
	public Address getPosition() {
		return pos;
	}
	public void setPosition(int r, int c) {
		super.setPosition(r,c);
	}
	public void moveUp() {
//		if (panel.isProblemEditMode() && stair==LOWER) {
//			stair = UPPER;
//			panel.resetPreviousInput();
//		} 
//		else{
			setPosition(pos.r - 1, pos.c);
			stair = LOWER;
//		}
	}
	public void moveLt() {
//		if (panel.isProblemEditMode() && stair==UPPER) {
//			stair = LOWER;
//			panel.resetPreviousInput();
//		} 
//		else {
			setPosition(pos.r, pos.c - 1);
			stair = UPPER;
//		}
	}
	public void moveDn() {
//		if (h==UPPER) h = LOWER;
//		else if (h==LOWER) {
			setPosition(pos.r + 1, pos.c);
//		}
	}
	public void moveRt() {
//		if (h==LOWER) h = UPPER;
//		else if (h==UPPER) {
		setPosition(pos.r, pos.c + 1);
//		}
	}
	public void moveLU() {
		setPosition(pos.r - 1, pos.c - 1);
	}
	public void moveLD() {
		setPosition(pos.r + 1, pos.c - 1);
	}
	public void moveRU() {
		setPosition(pos.r - 1, pos.c + 1);
	}
	public void moveRD() {
		setPosition(pos.r + 1, pos.c + 1);
	}
}
