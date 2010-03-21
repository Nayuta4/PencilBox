package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;

class BridgeEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = -1;

	private int row;
	private int col;
	private int direction;
	private int change;

	/**
	 * �R���X�g���N�^
	 * @param r �ύX���ꂽ���̋N�_�}�X�̍s���W
	 * @param c �ύX���ꂽ���̋N�_�}�X�̗���W
	 * @param dir �}�X����݂��ύX���ꂽ���̕���
	 * @param ch �ǉ����ꂽ�̂��C�������ꂽ�̂�
	 */
	public BridgeEditStep(int r, int c, int dir, int ch) {
		super();
		row = r;
		col = c;
		direction = dir;
		change = ch;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getDirection() {
		return direction;
	}

	public int getChange() {
		return change;
	}
	
}
