package pencilbox.hitori;

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
		panel.setHideSoleNumberMode(getBooleanProperty(PreferencesKeys.HIDE_SOLE_NUMBER_MODE));
		panel.setIndicateErrorMode(getBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE));
		panel.setLetters(getStringProperty(PreferencesKeys.LETTERS));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setPaintColor(getColorProperty(PreferencesKeys.PAINT_COLOR));
		panel.setCircleColor(getColorProperty(PreferencesKeys.NO_PAINT_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.HIDE_SOLE_NUMBER_MODE, panel.isHideSoleNumberMode());
		setBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE, panel.isIndicateErrorMode());
		setStringProperty(PreferencesKeys.LETTERS, panel.getLetters());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.PAINT_COLOR, panel.getPaintColor());
		setColorProperty(PreferencesKeys.NO_PAINT_COLOR, panel.getCircleColor());
	}
	
}
