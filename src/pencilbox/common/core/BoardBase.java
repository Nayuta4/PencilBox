package pencilbox.common.core;

import pencilbox.resource.Messages;



/**
 * �e�ՖʃN���X�̐e�N���X
 */
public class BoardBase {

	private UndoManager undoManager;
	private Size size;

	protected Address[] cellAddrs; // �ՖʑS�̂̃}�X���W���X�g

	/**
	 * �ՖʃT�C�Y���������āCBoard ������̏������������s��
	 * @param size �Ֆʂ̍s��T�C�Y
	 */
	public void setSize(Size size){
		Address.createAddressInstances(size);
		SideAddress.createSideAddressInstances(size);
		this.size = size;
		prepareAddresse();
		setup();
	}

	public void setSize(int r, int c){
		this.setSize(new Size(r, c));
	}
	/**
	 * Board ������̏���������
	 * �z�񐶐��Ȃǂ̏������������s��
	 * �e�T�u�N���X�ŃI�[�o�[���C�h����
	 */
	protected void setup(){
	}	

	protected void prepareAddresse() {
		this.cellAddrs = new Address[rows()*cols()];
		for (int r=0; r<rows(); r++) {
			for (int c=0; c<cols(); c++) {
				cellAddrs[r*cols()+c] = Address.address(r, c);
			}
		}
	}

	/**
	 * UndoManager ��ݒ肷��
	 * @param u UndoManager
	 */
	public void setUndoManager(UndoManager u) {
		undoManager = u;
	}

	public UndoManager getUndoManager() {
		return undoManager;
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

	public boolean isRecordUndo() {
		if (getUndoManager() != null)
			return getUndoManager().isRecordUndo();
		return false;
	}

	public void setRecordUndo(boolean b) {
		if (getUndoManager() != null)
			getUndoManager().setRecordUndo(b);
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
		return isOn(position.r(), position.c());
	}
	/**
	 * �����ō��W��^����ꂽ�}�X���Տ�ɂ��邩�ǂ���
	 * @param position ���ׂ���W
	 * @return �Տ�ɂ���� true
	 */
	public boolean isOnAll(Address... positions) {
		for (Address p : positions)
			if (! isOn(p))
				return false;
		return true;
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
		return isSideOn(position.d(), position.r(), position.c());
	}

	/**
	 * �����ō��W��^����ꂽ�ӂ��Տ�ɂ��邩�ǂ���
	 * @param position
	 * @return �Տ�ɂ���� true
	 */
	public boolean isSideOn(Address pos, int dir) {
		switch (dir) {
		case Direction.UP :
			return isSideOn(Direction.HORIZ, pos.r()-1, pos.c());
		case Direction.LT :
			return isSideOn(Direction.VERT, pos.r(), pos.c()-1);
		case Direction.DN :
			return isSideOn(Direction.HORIZ, pos.r(), pos.c());
		case Direction.RT :
			return isSideOn(Direction.VERT, pos.r(), pos.c());
		default :
			return false;
		}
	}
	/**
	 * �����ŗ^����ꂽ�̈�S�̂��Փ��ɂ��邩
	 * @param area �̈�
	 * @return �̈�S�̂��Փ��ɂ���� true, �����łȂ���� false ��Ԃ��B
	 */
	public boolean isAreaOn(Area area) {
		for (Address pos : area) {
			if (!isOn(pos))
				return false;
		}
		return true;
	}

	/**
	 * �ՖʑS�̂��܂ޗ̈���쐬���ĕԂ��B
	 * @return �ՖʑS�̂��܂ޗ̈�B
	 */
	public Area getWholeBoardArea() {
		Area area = new Area();
		for (int r = 0; r < rows(); r++) {
			for (int c = 0; c < cols(); c++) {
				area.add(Address.address(r, c));
			}
		}
		return area;
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
	protected void fireUndoableEditUpdate(AbstractStep e) {
//		if (isRecordUndo())
		undoManager.addEdit(e);
	}
	/**
	 * �A���h�D
	 * ��̓I�ȑ���͊e�T�u�N���X�Ŏ�������
	 * @param step
	 */
	public void undo(AbstractStep step) {
	}
	/**
	 * ���h�D
	 * ��̓I�ȑ���͊e�T�u�N���X�Ŏ�������
	 * @param step
	 */
	public void redo(AbstractStep step) {
	}
	/**
	 * �����`�F�b�N�Ŋ����̏ꍇ�̃R�����g������
	 */
	public static final String COMPLETE_MESSAGE = Messages.getString("BoardBase.MessageComplete"); //$NON-NLS-1$
	
	/**
	 * �������`�F�b�N���C���ʂ𕶎���ŕԂ�
	 * @return ���ʂ�\��������
	 */
	public String checkAnswerString() {
		return Messages.getString("BoardBase.MessageUnavailable"); //$NON-NLS-1$
	}
	/**
	 * �����`�F�b�N���C���ʂ𐔒l�ŕԂ�
	 * @return �����Ȃ� 0
	 */
	public int checkAnswerCode() {
		return 0;
	}

}
