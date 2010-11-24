package pencilbox.numberlink;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * �u�i���o�[�����N�v���j���[�N���X
 */
public class Menu extends MenuBase {

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
//		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addLinkWidthMenuItem();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		lineColorItem = addColorMenuItem(Messages.getString("Menu.lineColorItem")); //$NON-NLS-1$
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("����ԂŎ���(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateLinkColorItem"), 'R', false)); //$NON-NLS-1$
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.highlightSelectionItem"), 'H', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

}
