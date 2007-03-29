package pencilbox.tentaisho;

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
		panelD.setHideStarMode(panelS.isHideStarMode());
		panelD.setShowAreaBorderMode(panelS.isShowAreaBorderMode());
		panelD.setIndicateErrorMode(panelS.isIndicateErrorMode());
		panelD.setAreaBorderColor(panelS.getAreaBorderColor());
		panelD.setWhiteAreaColor(panelS.getWhiteAreaColor());
		panelD.setBlackAreaColor(panelS.getBlackAreaColor());
	}
}
