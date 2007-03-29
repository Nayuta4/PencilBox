package pencilbox.shikaku;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�l�p�ɐ؂�v���j���[�N���X
 */
public class Menu extends MenuBase {
	
	private JMenuItem areaBorderColorItem;
	private JMenuItem areaPaintColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateAreaColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		areaBorderColorItem = addColorMenuItem("�̈拫�E");
		areaPaintColorItem = addColorMenuItem("�̈����");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("�����F�Ŏ���(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem("�̈�̐F����(R)", 'R', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
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
			getPanel().getAreaBorderColor();
		else if (target == areaPaintColorItem)
			getPanel().getAreaPaintColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == areaPaintColorItem)
			getPanel().setAreaPaintColor(color);
	}
	
	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
	}
	
}
