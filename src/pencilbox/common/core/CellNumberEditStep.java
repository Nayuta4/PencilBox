package pencilbox.common.core;



public class CellNumberEditStep extends CellEditStep {
	
	/**
	   * �R���X�g���N�^
	   * @param r �ύX���ꂽ�}�X�̍s���W
	   * @param c �ύX���ꂽ�}�X�̗���W
	   * @param b �ύX�O�̏��
	   * @param a �ύX��̏��
	   */
	public CellNumberEditStep(int r, int c, int b, int a) {
		super(r, c, b, a);
	}

	public boolean addEdit(AbstractStep anEdit) {
		CellNumberEditStep edit = (CellNumberEditStep) anEdit;
		if (edit.getRow() == getRow() && edit.getCol() == getCol()) {
			this.setAfter(edit.getAfter());
			return true;
		} else {
			return false;
		}
	}
	  
}
