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
		panelD.setShowStar(panelS.isShowStar());
		panelD.setShowAreaBorder(panelS.isShowAreaBorder());
		panelD.setShowAreaHint(panelS.isShowAreaHint());
		panelD.setAreaBorderColor(panelS.getAreaBorderColor());
		panelD.setWhiteAreaColor(panelS.getWhiteAreaColor());
		panelD.setBlackAreaColor(panelS.getBlackAreaColor());
	}
}
