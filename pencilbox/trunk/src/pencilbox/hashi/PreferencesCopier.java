package pencilbox.hashi;

import java.util.Arrays;

import pencilbox.common.gui.PreferenceKey;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	static {
		usedKeys = Arrays.asList(new PreferenceKey[] {
			PreferenceKey.LINK_WIDTH,
			PreferenceKey.SEPARATE_LINK_COLOR_MODE,
			PreferenceKey.INDICATE_ERROR_MODE,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.LINE_COLOR,
		});
	}

}
