package pencilbox.fillomino;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「フィルオミノ」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem numberColorItem;
	private JMenuItem inputColorItem;
	private JMenuItem areaBorderColorItem;
	private JMenuItem showAreaBorderItem;
	private JMenuItem borderColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateAreaColorItem;
	
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

	public void executeCommand2(JMenuItem target) {
		if (target == showAreaBorderItem) {
			getPanel().setShowAreaBorderMode(target.isSelected());
		} else if (target == indicateErrorItem) {
			getPanel().setIndicateErrorMode(target.isSelected());
		} else if (target == separateAreaColorItem) {
			getPanel().setSeparateAreaColorMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			return getPanel().getInputColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else if (target == borderColorItem)
			return getPanel().getBorderColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == borderColorItem)
			getPanel().setBorderColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorderMode());
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
	}

}
