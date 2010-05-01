package pencilbox.common.core;



public class CellNumberEditStep extends CellEditStep {
	
	/**
	   * コンストラクタ
	   * @param p 変更されたマスの座標
	   * @param b 変更前の状態
	   * @param a 変更後の状態
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
