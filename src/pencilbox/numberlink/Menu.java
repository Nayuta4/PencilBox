package pencilbox.numberlink;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「ナンバーリンク」メニュークラス
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
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateLinkColorItem"), 'R', false)); //$NON-NLS-1$
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.highlightSelectionItem"), 'H', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
//		if (target == indicateErrorItem)
//			getPanel().setIndicateErrorMode(target.isSelected());
		if (target == separateLinkColorItem)
			getPanel().setSeparateLinkColorMode(target.isSelected());
		else if (target == highlightSelectionItem)
			getPanel().setHighlightSelectionMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == lineColorItem)
			return getPanel().getLineColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
//		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
		highlightSelectionItem.setSelected(getPanel().isHighlightSelectionMode());
	}

}
