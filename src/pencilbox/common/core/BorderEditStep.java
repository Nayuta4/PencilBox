package pencilbox.common.core;

public class BorderEditStep extends AbstractStep {

	private SideAddress pos;
	private int before;
	private int after;
	/**
	 * �R���X�g���N�^
	 * @param p �ύX���ꂽ�ӂ̍��W
	 * @param b �ύX�O�̏��
	 * @param a �ύX��̏��
	 */
	public BorderEditStep(SideAddress p, int b, int a) {
		super();
		pos = p;
		before = b;
		after = a;
	}

	public SideAddress getPos() {
		return pos;
	}

	public int getBefore() {
		return before;
	}

	public int getAfter() {
		return after;
	}
}
