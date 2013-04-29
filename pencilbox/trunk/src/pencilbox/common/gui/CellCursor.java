package pencilbox.common.gui;

import pencilbox.common.core.Address;
/**
 * �J�[�\���N���X
 * �Ֆʂ̓��͑ΏۂƂȂ��Ă���}�X�������̂ɗp����
 */
public class CellCursor {

	private Address pos;

	/**
	 * �J�[�\�����쐬����
	 * Panel�Ɗ֘A�t����
	 */
	public CellCursor() {
		pos = Address.address(0, 0);
	}
	/**
	 * ���݂̃J�[�\���ʒu�̍s���W���擾����
	 * @return �s���W
	 */
	public int r() {
		return pos.r();
	}
	/**
	 * ���݂̃J�[�\���ʒu�̗���W���擾����
	 * @return ����W
	 */
	public int c() {
		return pos.c();
	}
	/**
	 * ���݂̃J�[�\���ʒu���擾����
	 * @return �J�[�\���ʒu
	 */
	public Address getPosition() {
		return pos;
	}

	/**
	 * @param address
	 */
	public void setPosition(Address address) {
		pos = address;
	}
	/**
	 * �J�[�\���������ɔՖʍ��W��^����ꂽ�}�X�ɂ��邩
	 * @param position ���ׂ�Ֆʍ��W
	 * @return 	 �J�[�\����position�ɂ���� true
	 */
	public boolean isAt(Address position) {
		return pos.equals(position);
	}
}
