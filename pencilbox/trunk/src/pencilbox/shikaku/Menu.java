package pencilbox.shikaku;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「四角に切れ」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem numberColorItem;
	private JMenuItem areaBorderColorItem;
	private JMenuItem areaPaintColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateAreaColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		areaPaintColorItem = addColorMenuItem(Messages.getString("Menu.areaPaintColorItem")); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateCompletionItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateAreaColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem) {
			getPanel().setIndicateErrorMode(target.isSelected());
		} else if (target == separateAreaColorItem) {
			getPanel().setSeparateAreaColorMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else if (target == areaPaintColorItem)
			return getPanel().getAreaPaintColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == areaPaintColorItem)
			getPanel().setAreaPaintColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}
	
	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
	}
	
}
