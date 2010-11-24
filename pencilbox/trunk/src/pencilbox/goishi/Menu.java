package pencilbox.goishi;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「碁石ひろい」メニュークラス
 */
public class Menu extends MenuBase {
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.stoneOutlineColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.stoneColorItem")); //$NON-NLS-1$
		inputColorItem = addColorMenuItem(Messages.getString("Menu.inputColorItem")); //$NON-NLS-1$
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
	}

}
