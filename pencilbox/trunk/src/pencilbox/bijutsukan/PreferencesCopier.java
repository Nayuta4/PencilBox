package pencilbox.bijutsukan;

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
		panelD.setWarnWrongIllumination(panelS.isWarnWrongIllumination());
		panelD.setShowBeamMode(panelS.isShowBeamMode());
		panelD.setIlluminationColor(panelS.getIlluminationColor());
		panelD.setNoilluminationColor(panelS.getNoilluminationColor());
		panelD.setIlluminatedColor(panelS.getIlluminatedColor());
	}
}
