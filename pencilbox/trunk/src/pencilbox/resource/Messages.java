package pencilbox.resource;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {

	private static final String BUNDLE_NAME = "pencilbox.resource.ResourceBundle";
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public Messages() {
	}

	/**
	 * リソースバンドルの与えたキーに対応する文字列を返す。
	 * ただし存在しないときは与えたキーの文字列をそのまま返す。
	 * @param key キー文字列
	 * @return　リソースバンドルの対応する文字列。
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
