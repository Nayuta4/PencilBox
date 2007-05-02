package pencilbox.nurikabe;

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
		panel.setCountAreaSizeMode(getBooleanProperty(PreferencesKeys.COUNT_AREA_SIZE_MODE));
		panel.setSeparateAreaColorMode(getBooleanProperty(PreferencesKeys.SEPARATE_AREA_COLOR_MODE));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setPaintColor(getColorProperty(PreferencesKeys.PAINT_COLOR));
		panel.setCircleColor(getColorProperty(PreferencesKeys.NO_PAINT_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.COUNT_AREA_SIZE_MODE, panel.isCountAreaSizeMode());
		setBooleanProperty(PreferencesKeys.SEPARATE_AREA_COLOR_MODE, panel.isSeparateAreaColorMode());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.PAINT_COLOR, panel.getPaintColor());
		setColorProperty(PreferencesKeys.NO_PAINT_COLOR, panel.getCircleColor());
	}
	
}
