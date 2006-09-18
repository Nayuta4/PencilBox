package pencilbox.yajilin;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u���W�����v���j���[�N���X
 */
public class Menu extends MenuBase {
	
	private JMenuItem lineColorItem;
	private JMenuItem blackColorItem;
	private JMenuItem whiteColorItem;
	private JMenuItem warnBranchedLinkItem;
	private JMenuItem colorForEachLinkItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}
	
	protected void buildIndividualMenu() {
		lineColorItem = addColorMenuItem("��");
		blackColorItem = addColorMenuItem("���}�X");
		whiteColorItem = addColorMenuItem("����");
		addToViewMenu(warnBranchedLinkItem = makeCheckBoxCommandMenuItem("����Ԃŕ\��(W)", 'W', false));
		addToViewMenu(colorForEachLinkItem = makeCheckBoxCommandMenuItem("���̐F����(D)", 'D', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
	}
	
	public void executeCommand2(JMenuItem target) {
		if (target == warnBranchedLinkItem)
			getPanel().setWarnBranchedLink(target.isSelected());
		else if (target == colorForEachLinkItem)
			getPanel().setColorForEachLink(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == lineColorItem)
			return getPanel().getLineColor();
		else if (target == blackColorItem)
			return getPanel().getBlackColor();
		else if (target == whiteColorItem)
			return getPanel().getWhiteColor();
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == blackColorItem)
			getPanel().setBlackColor(color);
		else if (target == whiteColorItem)
			getPanel().setWhiteColor(color);
	}

}
