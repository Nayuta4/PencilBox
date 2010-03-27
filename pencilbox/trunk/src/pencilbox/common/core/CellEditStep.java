package pencilbox.common.core;


public class CellEditStep extends AbstractStep {
	
	private Address pos;
	private int before;
	private int after;

	/**
	 * �R���X�g���N�^
	 * @param p �ύX���ꂽ�}�X�̍��W
	 * @param b �ύX�O�̏��
	 * @param a �ύX��̏��
	 */
	public CellEditStep(Address p, int b, int a) {
		super();
		pos = p;
		before = b;
		after = a;
	}

	public Address getPos() {
		return pos;
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
