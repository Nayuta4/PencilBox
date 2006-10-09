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
		panelD.setShowAreaBorder(panelS.isShowAreaBorder());
		panelD.setShowAreaHint(panelS.isShowAreaHint());
		panelD.setInputColor(panelS.getInputColor());
		panelD.setAreaBorderColor(panelS.getAreaBorderColor());
	}
}
