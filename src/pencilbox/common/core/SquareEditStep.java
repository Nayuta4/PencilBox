package pencilbox.common.core;

public class SquareEditStep extends AbstractStep {
	
	public static final int ADDED = 1;
	public static final int REMOVED = 0;
	public static final int CHANGED = 2;
	
	private Address p0 = Address.NOWHERE;
	private Address p1 = Address.NOWHERE;
	private Address q0 = Address.NOWHERE;
	private Address q1 = Address.NOWHERE;
	private int operation;

	/**
	 * �R���X�g���N�^
	 * @param sq ����ꂽ�̈�
	 * @param operation ����̎�ށF�ǉ����ꂽ�̂��C�������ꂽ�̂�
	 */
	public SquareEditStep(Address p0, Address p1, int operation) {
		super();
		if (operation == ADDED) {
			this.q0 = p0;
			this.q1 = p1;
		} else if (operation == REMOVED) {
			this.p0 = p0;
			this.p1 = p1;
		}
		this.operation = operation;
	}

	/**
	 * �R���X�g���N�^
	 * @param p0 �ύX�O�̊p�̍��W
	 * @param p1 �ύX�O�̊p�̍��W
	 * @param q0 �ύX��̊p�̍��W
	 * @param q1 �ύX��̊p�̍��W
	 * @param operation ����̎�ށF�ǉ����ꂽ�̂��C�������ꂽ�̂��C�ύX���ꂽ�̂�
	 */
	public SquareEditStep(Address p0, Address p1, Address q0, Address q1, int operation) {
		super();
		this.p0 = p0;
		this.p1 = p1;
		this.q0 = q0;
		this.q1 = q1;
		this.operation = operation;
	}

	public Address getP0() {
		return p0;
	}

	public Address getP1() {
		return p1;
	}

	public Address getQ0() {
		return q0;
	}

	public Address getQ1() {
		return q1;
	}

	public int getOperation() {
		return operation;
	}
	
}
