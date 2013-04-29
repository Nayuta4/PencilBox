package pencilbox.common.core;

/**
 * �ՖʃT�C�Y��\���N���X
 */
public class Size {

	private int rows;
	private int cols;

	/**
	 * �T�C�Y�I�u�W�F�N�g�����
	 * @param r �s��
	 * @param c ��
	 */
	public Size(int r, int c){
		this.rows = r;
		this.cols = c;
	}

	/**
	 * �T�C�Y�I�u�W�F�N�g�𕡐����� 
	 * @param size �R�s�[���̃T�C�Y
	 */
	public void copy(Size size){
		this.rows = size.getRows();
		this.cols = size.getCols();
	}
	/**
	 * �s��
	 * @return �s��
	 */
	final public int getRows() {
		return rows;
	}
	/**
	 * ��
	 * @return ��
	 */
	final public int getCols() {
		return cols;
	}
}
