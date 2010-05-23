package pencilbox.common.core;

/**
 * �P��̑����\���N���X
 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
 */
public class AreaEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private Address pos;
	private Address p0;
	private int operation;

	/**
	 * �R���X�g���N�^
	 * @param r �ύX���ꂽ�}�X�̍��W
	 * @param r0 �ύX���ꂽ�̈�̑�\�}�X�̍��W
	 */
	public AreaEditStep(Address p, Address p0, int operation) {
		super();
		this.pos = p;
		this.p0 = p0;
		this.operation = operation;
	}

	public Address getPos() {
		return pos;
	}

	public Address getP0() {
		return p0;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getOperation() {
		return operation;
	}
	
	public String toString() {
		return pos.toString() + " from " + p0 + " changed " + getOperation() + " " + getType();
	}
	
}
