package pencilbox.lits;

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
		panel.setPaintColor(getColorProperty(PreferencesKeys.PAINT_COLOR));
		panel.setCircleColor(getColorProperty(PreferencesKeys.NO_PAINT_COLOR));
		panel.setAreaBorderColor(getColorProperty(PreferencesKeys.AREA_BORDER_COLOR));
		panel.setSeparateTetrominoColorMode(getBooleanProperty(PreferencesKeys.SEPARATE_TETROMINO_COLOR_MODE));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setColorProperty(PreferencesKeys.PAINT_COLOR, panel.getPaintColor());
		setColorProperty(PreferencesKeys.NO_PAINT_COLOR, panel.getCircleColor());
		setColorProperty(PreferencesKeys.AREA_BORDER_COLOR, panel.getAreaBorderColor());
		setBooleanProperty(PreferencesKeys.SEPARATE_TETROMINO_COLOR_MODE, panel.isSeparateTetrominoColorMode());
	}
	
}
