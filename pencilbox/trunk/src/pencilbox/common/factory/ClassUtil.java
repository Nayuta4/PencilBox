package pencilbox.common.factory;


/**
 * クラス生成補助メソッドを集めたクラス
 */
public class ClassUtil {

	/**
	 * 個別パズルパッケージの className クラスのインスタンスを生成する
	 * @param pencilType パズルの種類
	 * @param className クラスの名前の文字列
	 * @return 生成したクラスのインスタンス
	 * @throws PencilBoxClassException
	 */
	public static Object createInstance(PencilType pencilType, String className)
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
	
