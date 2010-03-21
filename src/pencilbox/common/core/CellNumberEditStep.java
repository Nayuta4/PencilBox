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
		CellNumberEditStep edit = (CellNumberEditStep) anEdit;
		if (edit.getPos() == getPos()) {
			this.setAfter(edit.getAfter());
			return true;
		} else {
			return false;
		}
	}
	  
}
