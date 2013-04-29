package pencilbox.numberlink;


/**
 * 「ナンバーリンク」リンククラス
 */
public class Link extends pencilbox.common.core.Link {

	private int number;

	/**
	 * リンクを作成する
	 * 数字の初期値は 0
	 */
	public Link() {
		super();
		number = 0;
	}

	public void clear() {
		super.clear();
		number = 0;
	}

	public boolean addAll(Link c) {
		setNumber(c.getNumber());
		return super.addAll(c);
	}

	/**
	 * 色分け用に Link の状態を返す
	 * @return
	 * 1種類の数字にのみつながっていればその数字を返す
	 * 数字につながっていなければ 0 を返す
	 * 複数の数字につながっていれば -1 を返す
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * 色分け用に Link の数字を設定する
	 * @param i 設定する数字
	 */
	public void setNumber(int i) {
		if (number == 0)
			number = i;
		else if (number == i)
			;
		else if (number > 0 && i > 0)
			number = -1;
	}

}
