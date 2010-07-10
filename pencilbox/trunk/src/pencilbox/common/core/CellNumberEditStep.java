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

	public boolean attachEdit(AbstractStep edit) {
		if (! (edit instanceof CellNumberEditStep))
			return false;
		CellNumberEditStep ed = (CellNumberEditStep) edit;
		if (ed.getPos().equals(this.getPos())) {
			this.setAfter(ed.getAfter());
			return true;
		} else {
			return false;
		}
	}
	  
}
