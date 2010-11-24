package pencilbox.sudoku;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「数独」メニュークラス
 */
public class Menu extends MenuBase {

	public Panel getPanel() {
		return (Panel)getPanelBase();
	}
	
//	protected void buildFileMenu() {
//		super.buildFileMenu();
//		removeChangeBoardSizeMenuItem();
//	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addCursorMenuItem();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		numberColorItem = addColorMenuItem(Messages.getString("Menu.fixedNumberColorItem")); //$NON-NLS-1$
		inputColorItem = addColorMenuItem(Messages.getString("Menu.inputColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.highlightSelectionItem"), 'H', false)); //$NON-NLS-1$
		addToViewMenu(dotHintItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.dotHintItem"), 'D', false)); //$NON-NLS-1$
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		highlightSelectionItem.setSelected(getPanel().isHighlightSelectionMode());
		dotHintItem.setSelected(getPanel().isDotHintMode());
	}

}
