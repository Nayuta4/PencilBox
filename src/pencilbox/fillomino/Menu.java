package pencilbox.fillomino;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�t�B���I�~�m�v���j���[�N���X
 */
public class Menu extends MenuBase {
	
	private JMenuItem inputColorItem;
	private JMenuItem areaBorderColorItem;
	private JMenuItem showAreaBorderItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateAreaColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		inputColorItem = addColorMenuItem("���͐���");
		areaBorderColorItem = addColorMenuItem("�̈拫�E");
		addCursorMenu();
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem("���E���\��(B)", 'B', true));
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("�����F�Ŏ���(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem("�̈�̐F����(R)", 'R', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == showAreaBorderItem) {
			getPanel().setShowAreaBorderMode(target.isSelected());
		} else if (target == indicateErrorItem) {
			getPanel().setIndicateErrorMode(target.isSelected());
		} else if (target == separateAreaColorItem) {
			getPanel().setSeparateAreaColorMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			getPanel().getInputColor();
		else if (target == areaBorderColorItem)
			getPanel().getAreaBorderColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorderMode());
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
	}

}
