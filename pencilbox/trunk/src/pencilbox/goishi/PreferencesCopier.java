package pencilbox.goishi;

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
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setInputColor(getColorProperty(PreferencesKeys.INPUT_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.INPUT_COLOR, panel.getInputColor());
	}
	
}
