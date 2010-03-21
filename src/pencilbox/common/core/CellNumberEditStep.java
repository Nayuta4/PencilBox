package pencilbox.common.core;



public class CellNumberEditStep extends CellEditStep {
	
	/**
	   * �R���X�g���N�^
	   * @param p �ύX���ꂽ�}�X�̍��W
	   * @param b �ύX�O�̏��
	   * @param a �ύX��̏��
	   */
	public CellNumberEditStep(Address p, int b, int a) {
		super(p, b, a);
	}

	public boolean addEdit(AbstractStep anEdit) {
		CellNumberEditStep edit = (CellNumberEditStep) anEdit;
		if (edit.getPos() == getPos()) {
			this.setAfter(edit.getAfter());
			return true;
		} else {
			return false;
		}
	}
	  
}
