package pencilbox.nurikabe;


/**
 * 「ぬりかべ」領域クラス
 */
public class Area extends pencilbox.common.core.Area {

	static int NEXT_ID = 1;
	static int MULTIPLE_NUMBER = -1;

	private int id;
	private int areaType = 0;
	private int number = 0; // 部屋の数字
	

	/**
	 * 領域を作成する
	 * @param areaType 領域タイプ:黒マス領域か白マス領域か
	 */
	public Area(int areaType) {
		super();
		id = NEXT_ID++;
		if (NEXT_ID == Integer.MAX_VALUE) NEXT_ID = 1;
		this.areaType = areaType;
	}
	
	/**
	 * 領域に数字を設定する
	 * その領域に以前に数字が含まれていなかった場合は，その数字を設定する
	 * すでに他の数字が含まれていた場合は，複数数字領域となる
	 * @param n 追加する数字
	 */
	public void addNumber(int n) {
		if (number ==0) {
			number = n;
		} else if (number != 0) {
			number = MULTIPLE_NUMBER;
		}
	}
	
	/**
	 * @return Returns the number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return Returns the areaType.
	 */
	public int getAreaType() {
		return areaType;
	}

	/**
	 *  領域番号を取得する
	 * @return 領域番号 
	 */
	public int getID() {
		return id;
	}

	/**
	 * 領域番号を設定する
	 * @param i 設定する番号
	 */
	public void setID(int i) {
		id = i;
	}
	/**
	 * 
	 */
	public static void resetID() {
		NEXT_ID = 1;
	}

	
}
