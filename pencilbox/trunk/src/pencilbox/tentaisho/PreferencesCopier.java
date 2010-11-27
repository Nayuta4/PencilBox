package pencilbox.tentaisho;

import java.util.Arrays;

import pencilbox.common.gui.PreferenceKey;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	static {
		usedKeys = Arrays.asList(new PreferenceKey[] {
			PreferenceKey.SHOW_AREA_BORDER_MODE,
			PreferenceKey.HIDE_STAR_MODE,
			PreferenceKey.SEPARATE_AREA_COLOR_MODE,
			PreferenceKey.WHITE_AREA_COLOR,
			PreferenceKey.BLACK_AREA_COLOR,
			PreferenceKey.AREA_BORDER_COLOR,
			PreferenceKey.BORDER_COLOR,
		});
	}

}
