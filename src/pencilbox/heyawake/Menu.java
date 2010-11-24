package pencilbox.heyawake;

import pencilbox.common.gui.MenuBase;
import pencilbox.common.io.IOController.DataFormat;
import pencilbox.resource.Messages;

/**
 * �u�ւ�킯�v���j���[�N���X
 */
public class Menu extends MenuBase {

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildEditMenu() {
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildExportDataMenu() {
		super.buildExportDataMenu();
		makeDataExportItem(pencilbox.resource.Messages.getString("MenuBase.exportItemHeyawake"), 'H', DataFormat.HEYAWAKE); //$NON-NLS-1$
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.noPaintColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
//		addToViewMenu(indicateContinuousRoomItem = makeCheckBoxCommandMenuItem("�R�����A���x��(C)", 'C', false));
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
//		indicateContinuousRoomItem.setSelected(getPanel().isIndicateContinuousRoomMode());
	}

}
