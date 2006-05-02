package pencilbox.common.factory;


/**
 * �N���X�����⏕���\�b�h���W�߂��N���X
 */
public class ClassUtil {

	/**
	 * �ʃp�Y���p�b�P�[�W�� className �N���X�̃C���X�^���X�𐶐�����
	 * @param pencilType �p�Y���̎��
	 * @param className �N���X�̖��O�̕�����
	 * @return ���������N���X�̃C���X�^���X
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
	
