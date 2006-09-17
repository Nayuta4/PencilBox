package pencilbox.common.gui;

import pencilbox.common.core.Address;
/**
 * �J�[�\���N���X
 * �Ֆʂ̓��͑ΏۂƂȂ��Ă���}�X�������̂ɗp����
 */
public class CellCursor {
	
	protected Address pos;

	/**
	 * �J�[�\�����쐬����
	 * Panel�Ɗ֘A�t����
	 * @param panel
	 */
	public CellCursor() {
		pos = new Address();
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
		return new Address(pos);
	}
	/**
	 * @param r
	 * @param c
	 */
	public void setPosition(int r, int c) {
		pos.set(r, c);
	}
	
	public void resetPosition() {
		setPosition(0, 0);
	}
	/**
	 * @param address
	 */
	public void setPosition(Address address) {
		pos.set(address);
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
