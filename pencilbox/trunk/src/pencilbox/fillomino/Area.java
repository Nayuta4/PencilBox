package pencilbox.fillomino;

/**
 * 「フィルオミノ」領域クラス
 */
public class Area extends pencilbox.common.core.AreaBase {

	private int number; // 領域の数字

	/**
	 * コンストラクタ
	 * @param number 領域の数字
	 */
	public Area(int number) {
		super();
		this.number = number;
	}
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number The number to set.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * Area の状態を返す
	 * @return サイズが数字より小さい : 0
	 * サイズが数字に等しい : 1
	 * サイズが数字より大きい : -1
	 */
	public int getStatus() {
		if (size() < number) return 0;
		else if (size() == number) return 1;
		else if (size() > number) return -1;
		return -1;
	}

}
