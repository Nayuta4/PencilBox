package pencilbox.hitori;

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
		panelD.setHideSoleNumberMode(panelS.isHideSoleNumberMode());
		panelD.setIndicateErrorMode(panelS.isIndicateErrorMode());
//		panelD.setIndicateRedundantNumberMode(panelS.isIndicateRedundantNumberMode());
		panelD.setPaintColor(panelS.getPaintColor());
		panelD.setCircleColor(panelS.getCircleColor());
		panelD.setLetters(panelS.getLetters());
	}
}
