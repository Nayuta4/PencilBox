package pencilbox.sudoku;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u���Ɓv���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem numberColorItem;
	private JMenuItem areaBorderColorItem;
	private JMenuItem inputColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem highlightSelectionItem;
	private JMenuItem dotHintItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addCursorMenuItem();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem("�̈拫�E");
		numberColorItem = addColorMenuItem("��萔��");
		inputColorItem = addColorMenuItem("�𓚐���");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("����ԂŎ���(E)", 'E', false));
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem("�I�𐔎��n�C���C�g(H)", 'H', false));
		addToViewMenu(dotHintItem = makeCheckBoxCommandMenuItem("�\�Ȑ������h�b�g�Ŏ���(D)", 'D', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == highlightSelectionItem)
			getPanel().setHighlightSelectionMode(target.isSelected());
		else if (target == dotHintItem)
			getPanel().setDotHintMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			return getPanel().getInputColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		highlightSelectionItem.setSelected(getPanel().isHighlightSelectionMode());
		dotHintItem.setSelected(getPanel().isDotHintMode());
	}

}
