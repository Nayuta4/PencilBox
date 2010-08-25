package pencilbox.bijutsukan;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「美術館」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem bulbColorItem;
	private JMenuItem noBulbColorItem;
	private JMenuItem illuminatedCellColorItem;
	private JMenuItem wallColorItem;
	private JMenuItem numberColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem paintIlluminatedCellItem;
	private JMenuItem showBeamItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

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

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem) {
			getPanel().setIndicateErrorMode(target.isSelected());
		} else if (target == paintIlluminatedCellItem) {
			getPanel().setPaintIlluminatedCellMode(target.isSelected());
		} else if (target == showBeamItem) {
			getPanel().setShowBeamMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == bulbColorItem)
			return getPanel().getBulbColor();
		else if (target == noBulbColorItem)
			return getPanel().getNoBulbColor();
		else if (target == illuminatedCellColorItem)
			return getPanel().getIlluminatedCellColor();
		else if (target == wallColorItem)
			return getPanel().getWallColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else
			return super.getColor(target);
		}

	public void setColor(JMenuItem target, Color color) {
		if (target == bulbColorItem)
			getPanel().setBulbColor(color);
		else if (target == noBulbColorItem)
			getPanel().setNoBulbColor(color);
		else if (target == illuminatedCellColorItem)
			getPanel().setIlluminatedCellColor(color);
		else if (target == wallColorItem)
			getPanel().setWallColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		paintIlluminatedCellItem.setSelected(getPanel().isPaintIlluminatedCellMode());
		showBeamItem.setSelected(getPanel().isShowBeamMode());
	}

}
