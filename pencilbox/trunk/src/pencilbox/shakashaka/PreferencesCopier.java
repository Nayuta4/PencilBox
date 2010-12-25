package pencilbox.shakashaka;

import java.util.Arrays;

import pencilbox.common.gui.PreferenceKey;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	static {
		usedKeys = Arrays.asList(new PreferenceKey[] {
			PreferenceKey.WALL_COLOR,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.PAINT_COLOR,
			PreferenceKey.NO_PAINT_COLOR,
			PreferenceKey.INDICATE_ERROR_MODE,
		});
	}

}
