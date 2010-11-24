package pencilbox.fillomino;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「フィルオミノ」メニュークラス
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
		addCursorMenuItem();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.fixedNumberColorItem")); //$NON-NLS-1$
		inputColorItem = addColorMenuItem(Messages.getString("Menu.inputColorItem")); //$NON-NLS-1$
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		borderColorItem = addColorMenuItem(Messages.getString("Menu.borderColorItem")); //$NON-NLS-1$
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.showAreaBorderItem"), 'B', true)); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateCompletionItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateAreaColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

}
