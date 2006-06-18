package pencilbox.common.gui;

import pencilbox.common.core.Address;
/**
 * �J�[�\���N���X
 * �Ֆʂ̓��͑ΏۂƂȂ��Ă���}�X�������̂ɗp����
 */
public class CellCursor {
	
	protected Address pos;
	protected PanelEventHandler panel;

	/**
	 * �J�[�\�����쐬����
	 * Panel�Ɗ֘A�t����
	 * @param panel
	 */
	public CellCursor(PanelEventHandler panel) {
		pos = new Address();
		this.panel = panel;
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
	 * @param r
	 * @param c
	 */
	public void setPosition(int r, int c) {
		if (panel.isOn(r, c)) {
			pos.set(r, c);
			panel.resetPreviousInput();
		}
	}
	/**
	 * @param address
	 */
	public void setPosition(Address address) {
		pos.set(address.r(), address.c());
	}
	/**
	 * 
	 */
	public void moveUp() {
		setPosition(pos.r() - 1, pos.c());
	}
	/**
	 * 
	 */
	public void moveLt() {
		setPosition(pos.r(), pos.c() - 1);
	}
	/**
	 * 
	 */
	public void moveDn() {
		setPosition(pos.r() + 1, pos.c());
	}
	/**
	 * 
	 */
	public void moveRt() {
		setPosition(pos.r(), pos.c() + 1);
	}
	protected int rows() {
		return panel.rows();
	}
	protected int cols() {
		return panel.cols();
	}
	protected boolean isOn(int r, int c) {
		return panel.isOn(r, c);
	}
	protected boolean isOn(int r, int c, int adjustRow, int adjustCol){
		return panel.isOn(r, c, adjustRow, adjustCol);
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
