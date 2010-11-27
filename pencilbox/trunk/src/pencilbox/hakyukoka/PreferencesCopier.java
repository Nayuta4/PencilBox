package pencilbox.hakyukoka;

import java.util.Arrays;

import pencilbox.common.gui.PreferenceKey;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	static {
		usedKeys = Arrays.asList(new PreferenceKey[] {
			PreferenceKey.HIGHLIGHT_SELECTION_MODE,
			PreferenceKey.INDICATE_ERROR_MODE,
			PreferenceKey.DOT_HINT_MODE,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.INPUT_COLOR,
			PreferenceKey.AREA_BORDER_COLOR,
		});
	}

}
