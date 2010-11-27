package pencilbox.kurodoko;

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
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.PAINT_COLOR,
			PreferenceKey.NO_PAINT_COLOR,
		});
	}

}
