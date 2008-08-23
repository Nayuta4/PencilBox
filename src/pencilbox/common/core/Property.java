package pencilbox.common.core;

/**
 * 問題付属情報クラス
 */
public class Property {
	
	private String author = ""; 
	private String source = ""; 
	private String difficulty = ""; 
	
	/**
	 * デフォルトコンストラクタ
	 */
	public Property () {
	}

	/**
	 * コピーコンストラクタ
	 * @param p コピー元Property
	 */
	public Property (Property p) {
		this.author = p.author;
		this.source = p.source;
		this.difficulty = p.difficulty;
	}
	
	/**
	 * @return 作者名
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @return 難易度
	 */
	public String getDifficulty() {
		return difficulty;
	}

	/**
	 * @return　出典
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param string 作者名
	 */
	public void setAuthor(String string) {
		author = string;
	}

	/**
	 * @param string 難易度
	 */
	public void setDifficulty(String string) {
		difficulty = string;
	}

	/**
	 * @param string 出典
	 */
	public void setSource(String string) {
		source = string;
	}

}
