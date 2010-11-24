package pencilbox.lits;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * �u�k�h�s�r�v���j���[�N���X
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
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.noPaintColorItem")); //$NON-NLS-1$
		addToViewMenu(separateTetrominoColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateTetrominoColorItem"), 'D', false)); //$NON-NLS-1$
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		separateTetrominoColorItem.setSelected(getPanel().isSeparateTetrominoColorMode());
	}

}
