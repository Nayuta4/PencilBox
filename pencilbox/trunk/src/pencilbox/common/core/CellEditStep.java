package pencilbox.common.core;


public class CellEditStep extends AbstractStep {
	
	private int row;
	private int col;
	private int before;
	private int after;

	/**
	 * �R���X�g���N�^
	 * @param r �ύX���ꂽ�}�X�̍s���W
	 * @param c �ύX���ꂽ�}�X�̗���W
	 * @param b �ύX�O�̏��
	 * @param a �ύX��̏��
	 */
	public CellEditStep(int r, int c, int b, int a) {
		super();
		row = r;
		col = c;
		before = b;
		after = a;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}
	
	protected void setAfter(int a) {
		this.after = a;
	}
	
}
