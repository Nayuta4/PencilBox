package pencilbox.satogaeri;

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
			PreferenceKey.INDICATE_ERROR_MODE,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.LINE_COLOR,
			PreferenceKey.AREA_BORDER_COLOR,
			PreferenceKey.MARK_STYLE,
		});
	}

}
