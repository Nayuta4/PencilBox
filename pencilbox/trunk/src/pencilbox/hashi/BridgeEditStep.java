package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;

class BridgeEditStep extends AbstractStep {

	private Address pos;
	private int direction;
	private int before;
	private int after;

	/**
	 * �R���X�g���N�^
	 * @param p �ύX���ꂽ���̋N�_�}�X�̍��W
	 * @param dir �}�X����݂��ύX���ꂽ���̕���
	 * @param b �ύX�O�̋��̐�
	 * @param a �ύX��̋��̐�
	 */
	public BridgeEditStep(Address p, int dir, int b, int a) {
		super();
		pos = p;
		direction = dir;
		before = b;
		after = a;
	}

	public Address getPos() {
		return pos;
	}

	public int getDirection() {
		return direction;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}

	public String toString() {
		return pos.toString() + getDirection() + " "+ getBefore() + "->" + getAfter() + " " + getType();
	}
	
}
