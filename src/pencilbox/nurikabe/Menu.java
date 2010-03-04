package pencilbox.nurikabe;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;


/**
 * 「ぬりかべ」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem numberColorItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
//	private JMenuItem indicateErrorItem;
	private JMenuItem countAreaSizeItem;
	private JMenuItem separateAreaColorItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.noPaintColorItem")); //$NON-NLS-1$
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateWallColorItem"), 'R', false)); //$NON-NLS-1$
		addToViewMenu(countAreaSizeItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.countAreaSizeItem"),'C', false)); //$NON-NLS-1$
		addRenewColorMenuItem();	
	}

	public void executeCommand2(JMenuItem target) {
//		if (target == indicateErrorItem)
//			getPanel().setIndicateError(target.isSelected());
//		else
		if (target == countAreaSizeItem)
			getPanel().setCountAreaSizeMode(target.isSelected());
		else if (target == separateAreaColorItem)
			getPanel().setSeparateAreaColorMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
//		indicateErrorItem.setSelected(getPanel().isIndicateError());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
		countAreaSizeItem.setSelected(getPanel().isCountAreaSizeMode());
	}

}
