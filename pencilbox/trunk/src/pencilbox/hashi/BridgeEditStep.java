package pencilbox.hashi;

import pencilbox.common.core.AbstractStep;
import pencilbox.common.core.Address;

class BridgeEditStep extends AbstractStep {

	private Address pos;
	private int direction;
	private int change;

	/**
	 * �R���X�g���N�^
	 * @param p �ύX���ꂽ���̋N�_�}�X�̍��W
	 * @param dir �}�X����݂��ύX���ꂽ���̕���
	 * @param ch �ǉ����ꂽ�̂��C�������ꂽ�̂�
	 */
	public BridgeEditStep(Address p, int dir, int ch) {
		super();
		pos = Address.address(p);
		direction = dir;
		change = ch;
	}

	public Address getPos() {
		return pos;
	}

	public int getDirection() {
		return direction;
	}

	public int getChange() {
		return change;
	}
	
}
