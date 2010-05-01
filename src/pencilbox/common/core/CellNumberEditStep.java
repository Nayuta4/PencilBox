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
		if (! (anEdit instanceof CellNumberEditStep))
			return false;
		CellNumberEditStep edit = (CellNumberEditStep) anEdit;
		if (edit.getPos().equals(getPos())) {
			this.setAfter(edit.getAfter());
			return true;
		} else {
			return false;
		}
	}
	  
}
