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
	private JMenuItem showStarItem;
	private JMenuItem showAreaBorderItem;
	private JMenuItem showAreaHintItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		areaBorderColorItem = addColorMenuItem("�̈拫�E");
		whiteAreaColorItem = addColorMenuItem("�����̈�");
		blackAreaColorItem = addColorMenuItem("�����̈�");
		addToViewMenu(showStarItem = makeCheckBoxCommandMenuItem("���̕\��(S)", 'S', true));
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem("���E���\��(B)", 'B', true));
		addToViewMenu(showAreaHintItem = makeCheckBoxCommandMenuItem("�̈�̐F����(D)", 'D', false));
//		addRenewColorMenu();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == showAreaHintItem)
			getPanel().setShowAreaHint(target.isSelected());
		else if (target == showAreaBorderItem)
			getPanel().setShowAreaBorder(target.isSelected());
		else if (target == showStarItem)
			getPanel().setShowStar(target.isSelected());
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
		showStarItem.setSelected(getPanel().isShowStar());
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorder());
		showAreaHintItem.setSelected(getPanel().isShowAreaHint());
	}

}
