package pencilbox.common.core;



public class CellNumberEditStep extends CellEditStep {
	
	/**
	   * コンストラクタ
	   * @param r 変更されたマスの行座標
	   * @param c 変更されたマスの列座標
	   * @param b 変更前の状態
	   * @param a 変更後の状態
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
