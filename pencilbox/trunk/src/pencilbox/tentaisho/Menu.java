package pencilbox.tentaisho;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「天体ショー」メニュークラス
 */
public class Menu extends MenuBase {

	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		whiteAreaColorItem = addColorMenuItem(Messages.getString("Menu.whiteAreaColorItem")); //$NON-NLS-1$
		blackAreaColorItem = addColorMenuItem(Messages.getString("Menu.blackAreaColorItem")); //$NON-NLS-1$
		borderColorItem = addColorMenuItem(Messages.getString("Menu.borderColorItem")); //$NON-NLS-1$
		addToViewMenu(hideStarItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.hideStarItem"), 'H', false)); //$NON-NLS-1$
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.showAreaBorderItem"), 'B', true)); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateCompletionItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateAreaColorItem"), 'R', false)); //$NON-NLS-1$
//		addRenewColorMenuItem();
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		hideStarItem.setSelected(getPanel().isHideStarMode());
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorderMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
	}

}
