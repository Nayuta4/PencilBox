/**
 * 
 */
package pencilbox.common.io;

import pencilbox.common.core.PencilBoxException;

/**
 * PencilBox用例外クラス
 * 問題のファイルフォーマット異常に対して使用するつもりだが，今のところほとんど活用されていない
 */
public class DataFormatException extends PencilBoxException {

	/**
	 * @param arg0
	 */
	public DataFormatException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public DataFormatException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
