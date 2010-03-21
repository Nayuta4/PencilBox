package pencilbox.common.core;

/**
 * �P��̑����\���N���X
 * UNDO, REDO �ł̕ҏW�̒P�ʂƂȂ�
 */
public class AreaEditStep extends AbstractStep {

	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private int r;
	private int c;
	private int r0;
	private int c0;
	private int operation;

	/**
	 * �R���X�g���N�^
	 * @param r �ύX���ꂽ�}�X�̍s���W
	 * @param c �ύX���ꂽ�}�X�̗���W
	 * @param r0 �ύX���ꂽ�̈�̑�\�}�X�̍s���W
	 * @param c0 �ύX���ꂽ�̈�̑�\�}�X�̗���W
	 */
	public AreaEditStep(int r, int c, int r0, int c0, int operation) {
		super();
		this.r = r;
		this.c = c;
		this.r0 = r0;
		this.c0 = c0;
		this.operation = operation;
	}

	public int getR() {
		return r;
	}

	public int getC() {
		return c;
	}

	public int getR0() {
		return r0;
	}

	public int getC0() {
		return c0;
	}

	public void setOperation(int operation) {
		this.operation = operation;
	}

	public int getOperation() {
		return operation;
	}
	
}
