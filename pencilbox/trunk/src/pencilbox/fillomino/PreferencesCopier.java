package pencilbox.fillomino;

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
		panel.setShowAreaBorderMode(getBooleanProperty(PreferencesKeys.SHOW_AREA_BORDER_MODE));
		panel.setIndicateErrorMode(getBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE));
		panel.setSeparateAreaColorMode(getBooleanProperty(PreferencesKeys.SEPARATE_AREA_COLOR_MODE));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setInputColor(getColorProperty(PreferencesKeys.INPUT_COLOR));
		panel.setAreaBorderColor(getColorProperty(PreferencesKeys.AREA_BORDER_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.SHOW_AREA_BORDER_MODE, panel.isShowAreaBorderMode());
		setBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE, panel.isIndicateErrorMode());
		setBooleanProperty(PreferencesKeys.SEPARATE_AREA_COLOR_MODE, panel.isSeparateAreaColorMode());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.INPUT_COLOR, panel.getInputColor());
		setColorProperty(PreferencesKeys.AREA_BORDER_COLOR, panel.getAreaBorderColor());
	}
	
}
