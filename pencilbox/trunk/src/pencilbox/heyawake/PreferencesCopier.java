package pencilbox.heyawake;

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
			PreferenceKey.AREA_BORDER_COLOR,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.PAINT_COLOR,
			PreferenceKey.NO_PAINT_COLOR,
		});
	}

}
