package pencilbox.common.factory;


/**
 * クラス生成補助メソッドを集めたクラス
 */
public class ClassUtil {

	public static final String BOARD_CLASS = "Board";
	public static final String MENU_CLASS = "Menu";
	public static final String PANEL_CLASS = "Panel";
	public static final String PANEL_EVENT_HANDLER_CLASS = "PanelEventHandler";
	public static final String TXTREADER_CLASS = "TxtReader";
	public static final String TXTWRITER_CLASS = "TxtWriter";
	public static final String XMLREADER_CLASS = "XmlReader";
	public static final String XMLWRITER_CLASS = "XmlWriter";
	public static final String PCLREADER_CLASS = "PclReader";
	public static final String PCLWRITER_CLASS = "PclWriter";
	/**
	 * 個別パズルパッケージの className クラスのインスタンスを生成する
	 * @param pencilType パズルの種類
	 * @param className クラスの名前の文字列
	 * @return 生成したクラスのインスタンス
	 * @throws PencilBoxClassException
	 */
	public static final Object createInstance(PencilType pencilType, String className)
	throws PencilBoxClassException {
	try {
		Class cls = Class.forName(Constants.ROOT_PACKAGE_NAME + '.' + pencilType.getPencilName() + '.' + className);
		return cls.newInstance();
		} catch (ClassNotFoundException e) {
			throw new PencilBoxClassException(e);
		} catch (InstantiationException e) {
			throw new PencilBoxClassException(e);
		} catch (IllegalAccessException e) {
			throw new PencilBoxClassException(e);
		}
	}
	
}
	
