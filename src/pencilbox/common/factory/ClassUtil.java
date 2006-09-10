package pencilbox.common.factory;


/**
 * �N���X�����⏕���\�b�h���W�߂��N���X
 */
public class ClassUtil {

	public static final String BOARD_CLASS = "Board";
	public static final String MENU_CLASS = "Menu";
	public static final String PANEL_CLASS = "Panel";
	public static final String PANEL_EVENT_HANDLER_CLASS = "PanelEventHandler";
	public static final String PROBLEM_COPIER_CLASS = "ProblemCopier";
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
		return createInstance(Constants.ROOT_PACKAGE_NAME + '.'
				+ pencilType.getPencilName() + '.' + className);
	}

	/**
	 * �����p�b�P�[�W�� className �N���X�̃C���X�^���X�𐶐�����
	 * @param c ����N���X
	 * @param className �N���X�̖��O�̕�����
	 * @return ���������N���X�̃C���X�^���X
	 * @throws PencilBoxClassException
	 */
	public static final Object createInstance(Class c, String className)
			throws PencilBoxClassException {
		return createInstance(c.getPackage().getName() + '.' + className);
	}

	/** �N���X������C���X�^���X�𐶐�����B
	 *  �e���O��PencilBoxClassException �ɕϊ�����B
	 * @param fullClassName �p�b�P�[�W�����݂̃N���X��
	 * @return ���������N���X�̃C���X�^���X
	 * @throws PencilBoxClassException
	 */
	public static final Object createInstance(String fullClassName)
			throws PencilBoxClassException {
		try {
			Class cls = Class.forName(fullClassName);
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
	
