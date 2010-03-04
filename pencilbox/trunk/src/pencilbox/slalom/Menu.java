package pencilbox.slalom;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * �u�X�����[���v���j���[�N���X
 */
public class Menu extends MenuBase {
	
	private JMenuItem numberColorItem;
	private JMenuItem wallColorItem;
	private JMenuItem lineColorItem;
	private JMenuItem gateColorItem;
	private JMenuItem crossColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateLinkColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}
	
	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		wallColorItem = addColorMenuItem(Messages.getString("Menu.wallColorItem")); //$NON-NLS-1$
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		gateColorItem = addColorMenuItem(Messages.getString("Menu.gateColorItem")); //$NON-NLS-1$
		lineColorItem = addColorMenuItem(Messages.getString("Menu.lineColorItem")); //$NON-NLS-1$
		crossColorItem = addColorMenuItem(Messages.getString("Menu.crossColorItem")); //$NON-NLS-1$
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateLinkColorItem"), 'R', false)); //$NON-NLS-1$
		addRenewColorMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == separateLinkColorItem)
			getPanel().setSeparateLinkColorMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == lineColorItem)
			return getPanel().getLineColor();
		else if (target == crossColorItem)
			return getPanel().getCrossColor();
		else if (target == gateColorItem)
			return getPanel().getGateColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == wallColorItem)
			return getPanel().getWallColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == crossColorItem)
			getPanel().setCrossColor(color);
		else if (target == gateColorItem)
			getPanel().setGateColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else if (target == wallColorItem)
			getPanel().setWallColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
//		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
	}

}
