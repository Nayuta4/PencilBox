package pencilbox.common.factory;


/**
 * �N���X�����⏕���\�b�h���W�߂��N���X
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
	 * �ʃp�Y���p�b�P�[�W�� className �N���X�̃C���X�^���X�𐶐�����
	 * @param pencilType �p�Y���̎��
	 * @param className �N���X�̖��O�̕�����
	 * @return ���������N���X�̃C���X�^���X
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
	
