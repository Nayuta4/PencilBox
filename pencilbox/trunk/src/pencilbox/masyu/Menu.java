package pencilbox.masyu;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�܂���v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem lineColorItem;
	private JMenuItem crossColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateLinkColorItem;
	
	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		lineColorItem = addColorMenuItem("��");
		crossColorItem = addColorMenuItem("�~��");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("����ԂŎ���(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem("���̐F����(R)", 'R', false));
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
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == crossColorItem)
			getPanel().setCrossColor(color);
	}
	
	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
	}

}
