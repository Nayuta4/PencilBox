package pencilbox.hakyukoka;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�g�y���ʁv���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem inputColorItem;
	private JMenuItem warnWrongNumberItem;
	private JMenuItem hilightSelectedNumberItem;
	private JMenuItem showArrowedNumberDotItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		inputColorItem = addColorMenuItem("���͐���");
		addCursorMenu();
		addToViewMenu(warnWrongNumberItem = makeCheckBoxCommandMenuItem("����Ԃŕ\��(W)", 'W', false));
		addToViewMenu(hilightSelectedNumberItem = makeCheckBoxCommandMenuItem("�I�𐔎��n�C���C�g(H)", 'H', false));
		addToViewMenu(showArrowedNumberDotItem = makeCheckBoxCommandMenuItem("�\�Ȑ������h�b�g�\��(D)", 'D', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongNumberItem)
			getPanel().setWarnWrongNumber(target.isSelected());
		else if (target == hilightSelectedNumberItem)
			getPanel().setHighlightSelectedNumber(target.isSelected());
		else if (target == showArrowedNumberDotItem)
			getPanel().setShowAllowedNumberDot(target.isSelected());
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
	}
	
	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			getPanel().getInputColor();
		return null;
	}
}
