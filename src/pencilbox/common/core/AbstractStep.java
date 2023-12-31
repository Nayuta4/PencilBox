package pencilbox.common.core;

/**
 * アンドゥ／リドゥで用いる盤面操作の単位
 */
public class AbstractStep {

	protected EditType type = EditType.NONE;

	public enum EditType {
		NONE,
		NUMBER,
		FIXED,
		STATE,
		LINE,
		;
	}
	/**
	 * 操作種類の記号
	 * @return
	 */
	public EditType getType() {
		return type;
	}
	/**
	 * 新しい操作を現在の操作と合わせて一つの操作に合成するかどうか。
	 * 操作を合成する場合があるものについてはサブクラスでオーバーライドする。
	 * @param edit 新しい操作
	 * @return 操作を合成するなら true, そうでなければ false
	 */
	public boolean attachEdit(AbstractStep edit) {
		return false;
	}

}
