package pencilbox.heyawake;

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
		panelD.setWarnWrongWall(panelS.isWarnWrongWall());
		panelD.setShowContinuousRoom(panelS.isShowContinuousRoom());
		panelD.setPaintColor(panelS.getPaintColor());
		panelD.setCircleColor(panelS.getCircleColor());
	}
}
