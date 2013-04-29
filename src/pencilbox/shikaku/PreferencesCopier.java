package pencilbox.shikaku;

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
			PreferenceKey.SEPARATE_AREA_COLOR_MODE,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.AREA_PAINT_COLOR,
			PreferenceKey.AREA_BORDER_COLOR,
			PreferenceKey.BORDER_COLOR,
		});
	}

}
