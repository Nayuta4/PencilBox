package pencilbox.slalom;

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
			PreferenceKey.WALL_COLOR,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.GATE_COLOR,
			PreferenceKey.LINE_COLOR,
			PreferenceKey.CROSS_COLOR,
		});
	}

}
