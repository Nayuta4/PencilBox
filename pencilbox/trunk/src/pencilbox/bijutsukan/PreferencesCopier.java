package pencilbox.bijutsukan;

import java.util.Arrays;

import pencilbox.common.gui.PreferenceKey;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {
	
	static {
		usedKeys = Arrays.asList(new PreferenceKey[] {
			PreferenceKey.INDICATE_ERROR_MODE,
			PreferenceKey.SHOW_BEAM_MODE,
			PreferenceKey.PAINT_ILLUMINATED_CELL_MODE,
			PreferenceKey.WALL_COLOR,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.BULB_COLOR,
			PreferenceKey.NO_BULB_COLOR,
			PreferenceKey.ILLUMINATED_CELL_COLOR,
		});
	}

}
