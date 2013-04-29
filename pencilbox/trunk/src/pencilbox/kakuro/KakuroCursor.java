package pencilbox.kakuro;

import pencilbox.common.gui.CellCursor;

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
	 */
	public KakuroCursor() {
		super();
		stair = 0;
	}

}
