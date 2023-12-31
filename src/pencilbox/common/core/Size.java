package pencilbox.common.core;

/**
 * 盤面サイズを表すクラス
 */
public class Size {

	private int rows;
	private int cols;

	/**
	 * サイズオブジェクトを作る
	 * @param r 行数
	 * @param c 列数
	 */
	public Size(int r, int c){
		this.rows = r;
		this.cols = c;
	}

	/**
	 * サイズオブジェクトを複製する 
	 * @param size コピー元のサイズ
	 */
	public void copy(Size size){
		this.rows = size.getRows();
		this.cols = size.getCols();
	}
	/**
	 * 行数
	 * @return 行数
	 */
	final public int getRows() {
		return rows;
	}
	/**
	 * 列数
	 * @return 列数
	 */
	final public int getCols() {
		return cols;
	}
}
