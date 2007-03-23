package pencilbox.yajilin;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u���W�����v���j���[�N���X
 */
public class Menu extends MenuBase {
	
	private JMenuItem lineColorItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateLinkColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}
	
	protected void buildIndividualMenu() {
		lineColorItem = addColorMenuItem("��");
		paintColorItem = addColorMenuItem("���}�X");
		circleColorItem = addColorMenuItem("���}�X");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("����ԂŎ���(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem("���̐F����(R)", 'R', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		addNoPaintMarkStyleMenu();
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
		else if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
	}

}
