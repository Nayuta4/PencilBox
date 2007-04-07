package pencilbox.numberlink;

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
//		panelD.setIndicateErrorMode(panelS.isIndicateErrorMode());
		panelD.setSeparateLinkColorMode(panelS.isSeparateLinkColorMode());
		panelD.setHighlightSelectionMode(panelS.isHighlightSelectionMode());
		panelD.setLineColor(panelS.getLineColor());
	}
}
