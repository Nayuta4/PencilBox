package pencilbox.masyu;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「ましゅ」メニュークラス
 */
public class Menu extends MenuBase {

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addLinkWidthMenuItem();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.circleColorItem")); //$NON-NLS-1$
		lineColorItem = addColorMenuItem(Messages.getString("Menu.lineColorItem")); //$NON-NLS-1$
		crossColorItem = addColorMenuItem(Messages.getString("Menu.crossColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateLinkColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

}
