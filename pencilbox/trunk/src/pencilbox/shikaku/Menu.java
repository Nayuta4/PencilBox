package pencilbox.shikaku;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「四角に切れ」メニュークラス
 */
public class Menu extends MenuBase {

	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		addTrimAnswerMenuItem();
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		areaPaintColorItem = addColorMenuItem(Messages.getString("Menu.areaPaintColorItem")); //$NON-NLS-1$
		borderColorItem = addColorMenuItem(Messages.getString("Menu.borderColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateCompletionItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateAreaColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
	}

}
