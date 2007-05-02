package pencilbox.tentaisho;

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
		panel.setHideStarMode(getBooleanProperty(PreferencesKeys.HIDE_STAR_MODE));
		panel.setSeparateAreaColorMode(getBooleanProperty(PreferencesKeys.SEPARATE_AREA_COLOR_MODE));
		panel.setWhiteAreaColor(getColorProperty(PreferencesKeys.WHITE_AREA_COLOR));
		panel.setBlackAreaColor(getColorProperty(PreferencesKeys.BLACK_AREA_COLOR));
		panel.setAreaBorderColor(getColorProperty(PreferencesKeys.AREA_BORDER_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.SHOW_AREA_BORDER_MODE, panel.isShowAreaBorderMode());
		setBooleanProperty(PreferencesKeys.HIDE_STAR_MODE, panel.isHideStarMode());
		setBooleanProperty(PreferencesKeys.SEPARATE_AREA_COLOR_MODE, panel.isSeparateAreaColorMode());
		setColorProperty(PreferencesKeys.WHITE_AREA_COLOR, panel.getWhiteAreaColor());
		setColorProperty(PreferencesKeys.BLACK_AREA_COLOR, panel.getBlackAreaColor());
		setColorProperty(PreferencesKeys.AREA_BORDER_COLOR, panel.getAreaBorderColor());
	}

}
