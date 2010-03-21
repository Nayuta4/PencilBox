package pencilbox.common.core;

public class BorderEditStep extends AbstractStep {

	private int direction;
	private int row;
	private int col;
	private int before;
	private int after;
	/**
	 * �R���X�g���N�^
	 * @param d �����c��
	 * @param r �ύX���ꂽ�}�X�̍s���W
	 * @param c �ύX���ꂽ�}�X�̗���W
	 * @param b �ύX�O�̏��
	 * @param a �ύX��̏��
	 */
	public BorderEditStep(int d, int r, int c, int b, int a) {
		super();
		direction = d;
		row = r;
		col = c;
		before = b;
		after = a;
	}

	public int getDirection() {
		return direction;
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
}
