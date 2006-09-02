package pencilbox.kakuro;

import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�J�b�N���v�����͗p�J�[�\���N���X
 */
public class KakuroCursor extends CellCursor {
	
	static final int UPPER = 1;
	static final int LOWER = 0;
	/**
	 * ���}�X�̎ΐ��̏ォ����
	 * 0 �͉� 1 �͏�
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
	 * �u�J�b�N���v�����͗p�J�[�\�����쐬����
	 * Panel�Ɗ֘A�t����
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
