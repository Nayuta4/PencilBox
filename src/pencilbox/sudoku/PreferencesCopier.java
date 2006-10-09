package pencilbox.sudoku;

import pencilbox.common.gui.MenuCommand;
import pencilbox.common.gui.PreferencesCopierBase;

/**
 * 
 */
public class PreferencesCopier extends PreferencesCopierBase {

	public void copyPreferences(MenuCommand src, MenuCommand dst) {
		super.copyPreferences(src, dst);
		Panel panelS = (Panel) src.getPanelBase();
		Panel panelD = (Panel) dst.getPanelBase();
		panelD.setWarnWrongNumber(panelS.isWarnWrongNumber());
		panelD.setHighlightSelectedNumber(panelS.isHighlightSelectedNumber());
		panelD.setShowAllowedNumberDot(panelS.isShowAllowedNumberDot());
		panelD.setInputColor(panelS.getInputColor());
	}
}
