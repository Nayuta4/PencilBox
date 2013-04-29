package pencilbox.nurikabe;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;


/**
 * 「ぬりかべ」メニュークラス
 */
public class Menu extends MenuBase {

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.noPaintColorItem")); //$NON-NLS-1$
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateWallColorItem"), 'R', false)); //$NON-NLS-1$
		addToViewMenu(countAreaSizeItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.countAreaSizeItem"),'C', false)); //$NON-NLS-1$
		addRenewColorMenuItem();	
	}

}
