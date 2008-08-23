package pencilbox.lits;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * ÅuÇkÇhÇsÇrÅvÉÅÉjÉÖÅ[ÉNÉâÉX
 */
public class Menu extends MenuBase {

	private JMenuItem areaBorderColorItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem separateTetrominoColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem(Messages.getString("Menu.areaBorderColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.circleColorItem")); //$NON-NLS-1$
		addToViewMenu(separateTetrominoColorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.separateTetrominoColorItem"), 'D', false)); //$NON-NLS-1$
	}

	public void executeCommand2(JMenuItem target) {
		if (target == separateTetrominoColorItem)
			getPanel().setSeparateTetrominoColorMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		separateTetrominoColorItem.setSelected(getPanel().isSeparateTetrominoColorMode());
	}

}
