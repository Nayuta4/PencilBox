package pencilbox.fillomino;

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
		panelD.setShowAreaBorderMode(panelS.isShowAreaBorderMode());
		panelD.setSeparateAreaColorMode(panelS.isSeparateAreaColorMode());
		panelD.setIndicateErrorMode(panelS.isIndicateErrorMode());
		panelD.setInputColor(panelS.getInputColor());
		panelD.setAreaBorderColor(panelS.getAreaBorderColor());
	}
}
