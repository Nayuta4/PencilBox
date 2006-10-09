package pencilbox.yajilin;

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
		panelD.setLineColor(panelS.getLineColor());
		panelD.setPaintColor(panelS.getPaintColor());
		panelD.setCircleColor(panelS.getCircleColor());
	}
}
