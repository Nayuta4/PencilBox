package pencilbox.numberlink;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�i���o�[�����N�v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem lineColorItem;
	private JMenuItem warnBranchedLinkItem;
	private JMenuItem colorForEachLinkItem;
	private JMenuItem highlightSelectedLinkItem;
	
	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	
	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		lineColorItem = addColorMenuItem("��");
		addToViewMenu(warnBranchedLinkItem = makeCheckBoxCommandMenuItem("����Ԃŕ\��(W)", 'W', false));
		addToViewMenu(colorForEachLinkItem = makeCheckBoxCommandMenuItem("���̐F����(D)", 'D', false));
		addToViewMenu(highlightSelectedLinkItem = makeCheckBoxCommandMenuItem("�I�𐔎��n�C���C�g(H)", 'H', false));
		addRenewColorMenu();
//		addTrimAnswerMenuItem();
		addSymmetricPlacementMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnBranchedLinkItem)
			getPanel().setWarnBranchedLink(target.isSelected());
		else if (target == colorForEachLinkItem)
			getPanel().setColorForEachLink(target.isSelected());
		else if (target == highlightSelectedLinkItem)
			getPanel().setHighlightSelectedLink(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == lineColorItem)
			return getPanel().getLineColor();
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
	}

	protected void buildGridStyleMenu() {
		super.buildGridStyleMenu();
		makeDisplayStyleItem(0, "�ʏ�\��").setSelected(true);
		makeDisplayStyleItem(1, "�o�H�\��");
		makeDisplayStyleItem(2, "�r����\��");
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		warnBranchedLinkItem.setSelected(getPanel().isWarnBranchedLink());
		colorForEachLinkItem.setSelected(getPanel().isColorForEachLink());
		highlightSelectedLinkItem.setSelected(getPanel().isHighlightSelectedLink());
	}

}
