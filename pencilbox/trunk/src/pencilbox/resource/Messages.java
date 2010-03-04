package pencilbox.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "pencilbox.resource.ResourceBundle";
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public Messages() {
	}

	/**
	 * ���\�[�X�o���h���̗^�����L�[�ɑΉ����镶�����Ԃ��B
	 * ���������݂��Ȃ��Ƃ��͗^�����L�[�̕���������̂܂ܕԂ��B
	 * @param key �L�[������
	 * @return�@���\�[�X�o���h���̑Ή����镶����B
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
