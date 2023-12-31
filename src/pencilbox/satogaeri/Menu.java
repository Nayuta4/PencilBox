package pencilbox.satogaeri;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「さとがえり」メニュークラス
 */
public class Menu extends MenuBase {

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildMarkStyleMenu(Messages.getString("Menu.anyMarkStyleMenu"), 'M', new int[]{2,4});
		addLinkWidthMenuItem();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		numberColorItem = addColorMenuItem(Messages.getString("Menu.fixedNumberColorItem")); //$NON-NLS-1$
		lineColorItem = addColorMenuItem(Messages.getString("Menu.lineColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateCompletionItem"), 'E', false)); //$NON-NLS-1$
	}

//	protected void buildExportDataMenu() {
//		makeDataExportItem(Messages.getString("MenuBase.exportItemKanpen"), 'K', DataFormat.KANPEN); //$NON-NLS-1$
//		makeDataExportItem(Messages.getString("MenuBase.exportItemPzprv3"), 'Z', DataFormat.PZPRV3); //$NON-NLS-1$
//	}

}
