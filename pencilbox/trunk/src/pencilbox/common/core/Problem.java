package pencilbox.common.core;

import java.io.File;

/**
 * ���N���X
 *�@Bord, Propety, File ���W�߂�����
 */
public class Problem {
	
	private BoardBase board;
	private Property property;
	private File file;

	/**
	 * �f�t�H���g�R���X�g���N�^ 
	 */
	public Problem () {
		this.property = new Property();
	}
	/**
	 * �R���X�g���N�^�ŁCBoard�͈����̂��̂�ݒ肷��
	 * @param board �Ֆ�
	 */
	public Problem (BoardBase board) {
		this.board = board;
		this.property = new Property();
	}

	/**
	 * �R�s�[�R���X�g���N�^���ǂ��B
	 * Board�ȊO�͂��Ƃ�Problem�𕡐�����B
	 * Board�͕��������G�Ȃ̂ł��炩���ߕ�������Ȃǂ��ĕʌ̈����Ƃ��ė^����B
	 * @param problem ���������
	 * @param board �ݒ肷��Ֆ�
	 */
	public Problem (Problem problem, BoardBase board) {
		this.board = board;
		this.property = new Property(problem.getProperty());
		this.file = problem.getFile();
	}

	/**
	 * @return Returns the board.
	 */
	public BoardBase getBoard() {
		return board;
	}
	/**
	 * @param board The board to set.
	 */
	public void setBoard(BoardBase board) {
		this.board = board;
	}
	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * @return Returns the property.
	 */
	public Property getProperty() {
		return property;
	}
	/**
	 * @param property The property to set.
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
	/**
	 * ���̃t�@�C�������擾����
	 * �܂��t�@�C���Ɗ֘A�t�����Ă��Ȃ��ꍇ�́C"����" �Ƃ���
	 * @return �t�@�C����
	 */
	public String getFileName() {
		if (file == null)
			return Messages.getString("Problem.Untitled"); //$NON-NLS-1$
		else
			return file.getName();
	}
}