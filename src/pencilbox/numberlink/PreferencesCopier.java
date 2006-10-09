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
		panelD.setWarnBranchedLink(panelS.isWarnBranchedLink());
		panelD.setColorForEachLink(panelS.isColorForEachLink());
		panelD.setHighlightSelectedLink(panelS.isHighlightSelectedLink());
		panelD.setLineColor(panelS.getLineColor());
	}
}
