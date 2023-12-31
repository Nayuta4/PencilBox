package pencilbox.common.factory;

import pencilbox.common.core.PencilBoxException;

/**
 * PencilBox用例外クラス
 * 今のところClassUtilのみから発行される
 */
public class PencilBoxClassException extends PencilBoxException {

	/**
	 * 
	 */
	public PencilBoxClassException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public PencilBoxClassException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public PencilBoxClassException(Throwable arg0) {
		super(arg0);
	}
}
