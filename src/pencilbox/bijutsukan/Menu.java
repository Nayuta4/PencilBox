package pencilbox.bijutsukan;



import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「美術館」メニュークラス
 */
public class Menu extends MenuBase {

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		buildMarkStyleMenu(Messages.getString("Menu.noBulbStyleMenu"), 'N', new int[] {3, 4}); //$NON-NLS-1$
		super.buildViewMenu();
		wallColorItem = addColorMenuItem(Messages.getString("Menu.wallColorItem")); //$NON-NLS-1$
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		bulbColorItem = addColorMenuItem(Messages.getString("Menu.bulbColorItem")); //$NON-NLS-1$
		noBulbColorItem = addColorMenuItem(Messages.getString("Menu.noBulbColorItem")); //$NON-NLS-1$
		illuminatedCellColorItem = addColorMenuItem(Messages.getString("Menu.illuminatedCellColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(paintIlluminatedCellItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.paintIlluminatedCellItem"), 'P', true)); //$NON-NLS-1$
		addToViewMenu(showBeamItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.showBeamItem"), 'B', false)); //$NON-NLS-1$
	}

}
