package pencilbox.heyawake;

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
		panel.setAreaBorderColor(getColorProperty(PreferencesKeys.AREA_BORDER_COLOR));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setPaintColor(getColorProperty(PreferencesKeys.PAINT_COLOR));
		panel.setCircleColor(getColorProperty(PreferencesKeys.NO_PAINT_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE, panel.isIndicateErrorMode());
		setColorProperty(PreferencesKeys.AREA_BORDER_COLOR, panel.getAreaBorderColor());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.PAINT_COLOR, panel.getPaintColor());
		setColorProperty(PreferencesKeys.NO_PAINT_COLOR, panel.getCircleColor());
	}
	
}
