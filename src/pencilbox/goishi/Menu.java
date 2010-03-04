package pencilbox.goishi;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;

/**
 * 「碁石ひろい」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem numberColorItem;
	private JMenuItem inputColorItem;
	private JMenuItem paintColorItem;
	
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

	public void executeCommand2(JMenuItem target) {
	}

	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			return getPanel().getInputColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == paintColorItem)
			return getPanel().getPaintColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
	}

}
