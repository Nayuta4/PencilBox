package pencilbox.hitori;

import java.util.Arrays;

import pencilbox.common.gui.MenuCommand;
import pencilbox.common.gui.PreferenceKey;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	static {
		usedKeys = Arrays.asList(new PreferenceKey[] {
			PreferenceKey.HIDE_SOLE_NUMBER_MODE,
			PreferenceKey.INDICATE_ERROR_MODE,
			PreferenceKey.LETTERS,
			PreferenceKey.NUMBER_COLOR,
			PreferenceKey.PAINT_COLOR,
			PreferenceKey.NO_PAINT_COLOR,
		});
	}

	public void applyCurrentPreferences(MenuCommand command) {
		super.applyCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		panel.setLetters(getStringProperty(PreferenceKey.LETTERS));
	}
	
	public void acquireCurrentPreferences(MenuCommand command) {
		super.acquireCurrentPreferences(command);
		Panel panel = (Panel) command.getPanelBase();
		setStringProperty(PreferenceKey.LETTERS, panel.getLetters());
	}
	
}
