package pencilbox.kakuro;

import pencilbox.common.gui.MenuCommand;
import pencilbox.common.gui.PreferencesCopierBase;
import pencilbox.common.gui.PreferencesKeys;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	public void applyCurrentPreferences(MenuCommand command) {
		super.applyCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		panel.setIndicateErrorMode(getBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE));
		panel.setDotHintMode(getBooleanProperty(PreferencesKeys.DOT_HINT_MODE));
		panel.setWallColor(getColorProperty(PreferencesKeys.WALL_COLOR));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setInputColor(getColorProperty(PreferencesKeys.INPUT_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE, panel.isIndicateErrorMode());
		setBooleanProperty(PreferencesKeys.DOT_HINT_MODE, panel.isDotHintMode());
		setColorProperty(PreferencesKeys.WALL_COLOR, panel.getWallColor());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.INPUT_COLOR, panel.getInputColor());
	}
	
}
