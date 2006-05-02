package pencilbox.common.core;

import java.io.File;

/**
 * 問題クラス
 *　Bord, Size, Propety, File を集めたもの
 */
public class Problem {
	
	private BoardBase board;
	private Size size;
	private Property property;
	private File file;
	/**
	 * デフォルトコンストラクタ 
	 *
	 */
	public Problem () {
		this.property = new Property();
	}
	/**
	 * コンストラクタで，Boardは引数のものを設定する
	 * @param board 盤面
	 */
	public Problem (BoardBase board) {
		this.board = board;
		this.property = new Property();
		this.size = board.getSize();
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
	 * @return Returns the size.
	 */
	public Size getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(Size size) {
		this.size = size;
	}
	/**
	 * 問題のファイル名を取得する
	 * まだファイルと関連付けられていない場合は，"無題" とする
	 * @return ファイル名
	 */
	public String getFileName() {
		if (file == null)
			return "無題";
		else
			return file.getName();
	}
}
