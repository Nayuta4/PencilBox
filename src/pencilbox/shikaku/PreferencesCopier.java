package pencilbox.shikaku;

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
		panelD.setShowAreaHint(panelS.isShowAreaHint());
		panelD.setColorfulMode(panelS.isColorfulMode());
		panelD.setAreaPaintColor(panelS.getAreaPaintColor());
		panelD.setAreaBorderColor(panelS.getAreaBorderColor());
	}
}