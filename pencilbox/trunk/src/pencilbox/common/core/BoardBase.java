package pencilbox.common.core;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;



/**
 * �e�ՖʃN���X�̐e�N���X
 */
public class BoardBase {

	private UndoManager undoManager;
	private Size size;
	
	/**
	 * �ՖʃT�C�Y���������āCBoard ������̏������������s��
	 * @param size �Ֆʂ̍s��T�C�Y
	 */
	public void setSize(Size size){
		this.size = size;
		setup();
	}
	/**
	 * Board ������̏���������
	 * �z�񐶐��Ȃǂ̏������������s��
	 * �e�T�u�N���X�ŃI�[�o�[���C�h����
	 */
	protected void setup(){
	}	
	/**
	 * UndoManager ��ݒ肷��
	 * @param u UndoManager
	 */
	public void setUndoManager(UndoManager u) {
		undoManager = u;
	}
	/**
	 * �Ֆ� Size ���擾����
	 * @return �Ֆʂ�Size
	 */
	public Size getSize() {
		return size;
	}
	/**
	 * �Ֆʂ̍s�����擾����
	 * @return �s��
	 */
	public final int rows() {
		return size.getRows();
	}
	/**
	 * �Ֆʂ̗񐔂��擾����
	 * @return ��
	 */
	public final int cols() {
		return size.getCols();
	}
	/**
	 * �����ō��W��^����ꂽ�}�X���Տ�ɂ��邩�ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �Տ�ɂ���� true
	 */
	public boolean isOn(int r, int c) {
		return (r >= 0 && r < rows() && c >= 0 && c < cols());
	}
	/**
	 * �����ō��W��^����ꂽ�}�X���Տ�ɂ��邩�ǂ���
	 * �ՖʃT�C�Y�� adjustRow, adjustCol �̒l�̕␳��������
	 * 0 <= r < rows()+adjustRow, 0 <= c < col()+adjustCol �̂Ƃ��� true ��Ԃ�
	 * @param r �s���W
	 * @param c ����W
	 * @param adjustRow �s�̕␳�l
	 * @param adjustCol ��̕␳�l
	 * @return �Տ�ɂ���� true
	 */
	public boolean isOn(int r, int c, int adjustRow, int adjustCol) {
		return (r >= 0 && r < rows()+adjustRow && c >= 0 && c < cols()+adjustCol);
	}
	/**
	 * �����ō��W��^����ꂽ�}�X���Տ�ɂ��邩�ǂ���
	 * @param position ���ׂ���W
	 * @return �Տ�ɂ���� true
	 */
	public boolean isOn(Address position) {
		return isOn(position.r, position.c);
	}
	/**
	 * �����ō��W��^����ꂽ�ӂ��Տ�ɂ��邩�ǂ���
	 * @param d �c������
	 * @param r �s���W
	 * @param c ����W
	 * @return �Տ�ɂ���� true
	 */
	public boolean isSideOn(int d, int r, int c) {
		if (d==Direction.VERT)
			return (r >= 0 && r < rows() && c >= 0 && c < cols()-1);
		else if (d==Direction.HORIZ)
			return (r >= 0 && r < rows()-1 && c >= 0 && c < cols());
		else
			return false;
	}
	/**
	 * �����ō��W��^����ꂽ�ӂ��Տ�ɂ��邩�ǂ���
	 * @param position
	 * @return �Տ�ɂ���� true
	 */
	public boolean isSideOn(SideAddress position) {
		return isSideOn(position.d, position.r, position.c);
	}
	/**
	 * �𓚂��������C���̏����Ֆʂɖ߂�
	 * �e�T�u�N���X�Ŏ�������
	 */
	public void clearBoard() {
	}
	/**
	 * �𓚂���Ֆʂ��甒�}�X��~��Ȃǂ̕⏕�I���͂���������
	 * �i����C�j�e�T�u�N���X�Ŏ�������
	 */
	public void trimAnswer() {
	}
	/**
	 * �Ֆʐݒ肵�����ƂɌĂсC�Ֆʏ�Ԃ�����������
	 * �e�T�u�N���X�ŕK�v�ȏ������L�q����
	 */
	public void initBoard() {
	}
	/**
	 * �����ō��W��^����ꂽ�}�X���Տ�̍ŊO���}�X�ł��邩�ǂ���
	 * @param r �s���W
	 * @param c ����W
	 * @return �ŊO���}�X�Ȃ� true
	 */
	public boolean isOnPeriphery(int r, int c) {
		return (r == 0 || r == rows() - 1 || c == 0 || c == cols() - 1);
	}
	/**
	 * �A���h�D�C�x���g���X�i�[�ɃC�x���g�̔�����ʒm����
	 * �A���h�D�Ώۂ̑��삪���������Ƃ��ɌĂ�
	 * @param e the event �A���h�D�Ώۂ̑���ƂȂ�C�x���g
	 */
	protected void fireUndoableEditUpdate(UndoableEditEvent e) {
		undoManager.undoableEditHappened(e);
	}
	/**
	 * �����`�F�b�N�Ŋ����̏ꍇ�̃R�����g������
	 */
	public static final String COMPLETE_MESSAGE = "�����ł�";
	
	/**
	 * �������`�F�b�N���C���ʂ𕶎���ŕԂ�
	 * @return ���ʂ�\��������
	 */
	public String checkAnswerString() {
		return "����@�\������܂���";
	}
	/**
	 * �����`�F�b�N���C���ʂ𐔒l�ŕԂ�
	 * @return �����Ȃ� 0
	 */
	public int checkAnswerCode() {
		return 0;
	}
}