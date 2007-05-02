/**
 * 
 */
package pencilbox.common.gui;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * アプリケーションの設定の保存，読込，複写を行うためのクラス
 */
public class PreferencesCopierBase {
	
	private String pencilName;
	private Properties properties = new Properties();
	
	/**
	 * PreferencesCopier のインスタンスを作成する。
	 * @param pencilType 種類
	 * @return 作成した PreferencesCopier インスタンス
	 */
	public static PreferencesCopierBase createInstance(PencilType pencilType) {
		PreferencesCopierBase copier;
		try {
			copier = (PreferencesCopierBase) ClassUtil.createInstance(pencilType, ClassUtil.PREFERENCES_COPIER_CLASS);
		} catch (PencilBoxClassException ex) {
			copier = new PreferencesCopierBase();
		}
		copier.pencilName = pencilType.getPencilName() + '.';
		return copier;
	}
	
	/**
	 * 現在の properties に格納されている設定をアプリケーションに適用する。
	 * @param command
	 */
	public void applyCurrentPreferences(MenuCommand command) {
		PanelBase panel = command.getPanelBase();
		PanelEventHandlerBase handler = command.getPanelEventHandlerBase();
		handler.setProblemEditMode(getBooleanProperty(PreferencesKeys.EDIT_MODE));
		handler.setSymmetricPlacementMode(getBooleanProperty(PreferencesKeys.SYMMETRIC_PLACEMENT_MODE));
		handler.setImmediateAnswerCheckMode(getBooleanProperty(PreferencesKeys.IMMEDIATE_ANSWER_CHECK_MODE));
		panel.setDisplaySize(getIntProperty(PreferencesKeys.CELL_SIZE));
		panel.changeIndexMode(getBooleanProperty(PreferencesKeys.INDEX_MODE));
		panel.setGridStyle(getIntProperty(PreferencesKeys.GRID_STYLE));
		panel.setMarkStyle(getIntProperty(PreferencesKeys.MARK_STYLE));
		panel.setCursorMode(getBooleanProperty(PreferencesKeys.CURSOR_MODE));
		panel.setBackgroundColor(getColorProperty(PreferencesKeys.BACKGROUND_COLOR));
		panel.setGridColor(getColorProperty(PreferencesKeys.GRID_COLOR));
	}
	
	/**
	 * 現在のアプリケーションの設定を取得して properties に格納する。 
	 * @param command
	 */
	public void acquireCurrentPreferences(MenuCommand command) {
		PanelBase panel = command.getPanelBase();
		PanelEventHandlerBase handler = command.getPanelEventHandlerBase();
		setBooleanProperty(PreferencesKeys.EDIT_MODE, panel.isProblemEditMode());
		setBooleanProperty(PreferencesKeys.SYMMETRIC_PLACEMENT_MODE, handler.isSymmetricPlacementMode());
		setBooleanProperty(PreferencesKeys.IMMEDIATE_ANSWER_CHECK_MODE, handler.isImmediateAnswerCheckMode());
		setIntProperty(PreferencesKeys.CELL_SIZE, panel.getCellSize());
		setBooleanProperty(PreferencesKeys.INDEX_MODE, panel.isIndexMode());
		setIntProperty(PreferencesKeys.GRID_STYLE, panel.getGridStyle());
		setIntProperty(PreferencesKeys.MARK_STYLE, panel.getMarkStyle());
		setBooleanProperty(PreferencesKeys.CURSOR_MODE, panel.isCursorMode());
		setColorProperty(PreferencesKeys.BACKGROUND_COLOR, panel.getBackgroundColor());
		setColorProperty(PreferencesKeys.GRID_COLOR, panel.getGridColor());
	}
	
	/**
	 * メニュー選択をコピーする
	 * @param src コピー元フレームの MenuCommand インスタンス
	 * @param dst コピー先フレームの MenuCommand インスタンス
	 */
	public void copyPreferences(MenuCommand src, MenuCommand dst) {
		acquireCurrentPreferences(src);
		applyCurrentPreferences(dst);
	}

	/**
	 * 設定をファイルから読み込んでアプリケーションに適用する。
	 * ファイルにない項目は現在の設定のまま。
	 * @param command
	 * @param file 読み込むファイル
	 */
	public void loadPreferences(MenuCommand command, File file) {
		acquireCurrentPreferences(command);
		try {
			properties.load(new FileInputStream(file));
//			properties.list(System.out);
			applyCurrentPreferences(command);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * アプリケーションの設定をファイルに保存する。
	 * 保存するファイルがすでに存在する場合は，
	 * 同じ種類の項目については上書きし，それ以外の項目については元のファイルの値を保持する。
	 * @param command
	 * @param file 保存するファイル
	 */
	public void storePreferences(MenuCommand command, File file) {
		try {
			if (file.canRead()) {
				properties.load(new FileInputStream(file));
			}
			acquireCurrentPreferences(command);
			properties.store(new FileOutputStream(file), "PencilBox preferences");
//			properties.list(System.out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected String getStringProperty(String key) {
		return properties.getProperty(pencilName + key);
	}
	
	protected boolean getBooleanProperty(String key) {
		return Integer.parseInt(properties.getProperty(pencilName + key)) > 0;
//		return Boolean.parseBoolean(properties.getProperty(pencilName + key));
	}
	
	protected int getIntProperty(String key) {
		return Integer.parseInt(properties.getProperty(pencilName + key));
	}
	
	protected Color getColorProperty(String key) {
		return Color.decode(properties.getProperty(pencilName + key));
	}
	
	protected void setStringProperty(String key, String value) {
		properties.setProperty(pencilName + key, value);
	}

	protected void setBooleanProperty(String key, boolean value) {
		int i = value ? 1 : 0;
		properties.setProperty(pencilName + key, Integer.toString(i));
//		properties.setProperty(pencilName + key, Boolean.toString(value));
	}
	
	protected void setIntProperty(String key, int value) {
		properties.setProperty(pencilName + key, Integer.toString(value));
	}
	
	protected void setColorProperty(String key, Color value) {
		properties.setProperty(pencilName + key, getColorString(value));
	}

	public String getColorString(Color color) {
//		return Integer.toString(color.getRGB() & 0xFFFFFF);
		return String.format("0x%06X", new Object[] {Integer.valueOf(color.getRGB() & 0xFFFFFF)});
	}

}