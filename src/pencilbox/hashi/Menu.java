package pencilbox.hashi;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「橋をかけろ」メニュークラス
 */
public class Menu extends MenuBase {

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addLinkWidthMenuItem();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		lineColorItem = addColorMenuItem(Messages.getString("Menu.lineColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateCompletionItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateLinkColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

}
