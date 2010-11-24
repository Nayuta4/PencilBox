package pencilbox.yajilin;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「ヤジリン」メニュークラス
 */
public class Menu extends MenuBase {
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}
	
	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		addLinkWidthMenuItem();
		super.buildViewMenu();
//		wallColorItem = addColorMenuItem("数字マス");
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		lineColorItem = addColorMenuItem(Messages.getString("Menu.lineColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.noPaintColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateLinkColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
	}

}
