package pencilbox.masyu;

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
		panel.setLinkWidth(getIntProperty(PreferencesKeys.LINK_WIDTH));
		panel.setIndicateErrorMode(getBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE));
		panel.setSeparateLinkColorMode(getBooleanProperty(PreferencesKeys.SEPARATE_LINK_COLOR_MODE));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setLineColor(getColorProperty(PreferencesKeys.LINE_COLOR));
		panel.setCrossColor(getColorProperty(PreferencesKeys.CROSS_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setIntProperty(PreferencesKeys.LINK_WIDTH, panel.getLinkWidth());
		setBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE, panel.isIndicateErrorMode());
		setBooleanProperty(PreferencesKeys.SEPARATE_LINK_COLOR_MODE, panel.isSeparateLinkColorMode());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.LINE_COLOR, panel.getLineColor());
		setColorProperty(PreferencesKeys.CROSS_COLOR, panel.getCrossColor());
	}
	
}
