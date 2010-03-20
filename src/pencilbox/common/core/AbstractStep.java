package pencilbox.common.core;

/**
 * アンドゥ／リドゥで用いる盤面操作の単位
 */
public class AbstractStep {

	/**
	 * 新しい操作を現在の操作と合わせて一つの操作に合成するかどうか。
	 * 操作を合成する場合があるものについてはサブクラスでオーバーライドする。
	 * @param edit 新しい操作
	 * @return 操作を合成するなら true, そうでなければ false
	 */
	public boolean addEdit(AbstractStep edit) {
		return false;
	}

}
