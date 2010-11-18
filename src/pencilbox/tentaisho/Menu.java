package pencilbox.tentaisho;

import java.awt.Color;

import javax.swing.JMenuItem;

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

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == showAreaBorderItem)
			getPanel().setShowAreaBorderMode(target.isSelected());
		else if (target == separateAreaColorItem)
			getPanel().setSeparateAreaColorMode(target.isSelected());
		else if (target == hideStarItem)
			getPanel().setHideStarMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else if (target == whiteAreaColorItem)
			return getPanel().getWhiteAreaColor();
		else if (target == blackAreaColorItem)
			return getPanel().getBlackAreaColor();
		else if (target == borderColorItem)
			return getPanel().getBorderColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == whiteAreaColorItem)
			getPanel().setWhiteAreaColor(color);
		else if (target == blackAreaColorItem)
			getPanel().setBlackAreaColor(color);
		else if (target == borderColorItem)
			getPanel().setBorderColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		hideStarItem.setSelected(getPanel().isHideStarMode());
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorderMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
	}

}
