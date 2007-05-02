package pencilbox.bijutsukan;

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
		panel.setShowBeamMode(getBooleanProperty(PreferencesKeys.SHOW_BEAM_MODE));
		panel.setPaintIlluminatedCellMode(getBooleanProperty(PreferencesKeys.PAINT_ILLUMINATED_CELL_MODE));
		panel.setWallColor(getColorProperty(PreferencesKeys.WALL_COLOR));
		panel.setNumberColor(getColorProperty(PreferencesKeys.NUMBER_COLOR));
		panel.setBulbColor(getColorProperty(PreferencesKeys.BULB_COLOR));
		panel.setNoBulbColor(getColorProperty(PreferencesKeys.NO_BULB_COLOR));
		panel.setIlluminatedCellColor(getColorProperty(PreferencesKeys.ILLUMINATED_CELL_COLOR));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setBooleanProperty(PreferencesKeys.INDICATE_ERROR_MODE, panel.isIndicateErrorMode());
		setBooleanProperty(PreferencesKeys.SHOW_BEAM_MODE, panel.isShowBeamMode());
		setBooleanProperty(PreferencesKeys.PAINT_ILLUMINATED_CELL_MODE, panel.isPaintIlluminatedCellMode());
		setColorProperty(PreferencesKeys.WALL_COLOR, panel.getWallColor());
		setColorProperty(PreferencesKeys.NUMBER_COLOR, panel.getNumberColor());
		setColorProperty(PreferencesKeys.BULB_COLOR, panel.getBulbColor());
		setColorProperty(PreferencesKeys.NO_BULB_COLOR, panel.getNoBulbColor());
		setColorProperty(PreferencesKeys.ILLUMINATED_CELL_COLOR, panel.getIlluminatedCellColor());
	}
	
}
