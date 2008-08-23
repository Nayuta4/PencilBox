package pencilbox.common.core;

/**
 * ���t�����N���X
 */
public class Property {
	
	private String author = ""; 
	private String source = ""; 
	private String difficulty = ""; 
	
	/**
	 * �f�t�H���g�R���X�g���N�^
	 */
	public Property () {
	}

	/**
	 * �R�s�[�R���X�g���N�^
	 * @param p �R�s�[��Property
	 */
	public Property (Property p) {
		this.author = p.author;
		this.source = p.source;
		this.difficulty = p.difficulty;
	}
	
	/**
	 * @return ��Җ�
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return ��Փx
	 */
	public String getDifficulty() {
		return difficulty;
	}

	/**
	 * @return�@�o�T
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param string ��Җ�
	 */
	public void setAuthor(String string) {
		author = string;
	}

	/**
	 * @param string ��Փx
	 */
	public void setDifficulty(String string) {
		difficulty = string;
	}

	/**
	 * @param string �o�T
	 */
	public void setSource(String string) {
		source = string;
	}

}
