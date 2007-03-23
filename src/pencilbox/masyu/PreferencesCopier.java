package pencilbox.masyu;

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
		panelD.setIndicateErrorMode(panelS.isIndicateErrorMode());
		panelD.setSeparateLinkColorMode(panelS.isSeparateLinkColorMode());
		panelD.setLineColor(panelS.getLineColor());
		panelD.setCrossColor(panelS.getCrossColor());
	}
}
