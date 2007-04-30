package pencilbox.tentaisho;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�V�̃V���[�v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem areaBorderColorItem;
	private JMenuItem whiteAreaColorItem;
	private JMenuItem blackAreaColorItem;
	private JMenuItem hideStarItem;
	private JMenuItem showAreaBorderItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateAreaColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem("�̈拫�E");
		whiteAreaColorItem = addColorMenuItem("�����̈�");
		blackAreaColorItem = addColorMenuItem("�����̈�");
		addToViewMenu(hideStarItem = makeCheckBoxCommandMenuItem("���̔�\��(H)", 'H', false));
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem("���E���\��(B)", 'B', true));
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("�����F�Ŏ���(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem("�̈�̐F����(R)", 'R', false));
//		addRenewColorMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == showAreaBorderItem)
			getPanel().setShowAreaBorderMode(target.isSelected());
		else if (target == separateAreaColorItem)
			getPanel().setSeparateAreaColorMode(target.isSelected());
		else if (target == hideStarItem)
			getPanel().setHideStarMode(target.isSelected());
	}
	
	public Color getColor(JMenuItem target) {
		if (target == areaBorderColorItem)
			getPanel().getAreaBorderColor();
		else if (target == whiteAreaColorItem)
			getPanel().getWhiteAreaColor();
		else if (target == blackAreaColorItem)
			getPanel().getBlackAreaColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == whiteAreaColorItem)
			getPanel().setWhiteAreaColor(color);
		else if (target == blackAreaColorItem)
			getPanel().setBlackAreaColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		hideStarItem.setSelected(getPanel().isHideStarMode());
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorderMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
	}

}
