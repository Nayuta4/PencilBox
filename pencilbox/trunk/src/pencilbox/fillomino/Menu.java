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
	private JMenuItem showAreaHintItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		inputColorItem = addColorMenuItem("���͐���");
		areaBorderColorItem = addColorMenuItem("���E��");
		addCursorMenu();
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem("���E���\��(B)", 'B', true));
		addToViewMenu(showAreaHintItem = makeCheckBoxCommandMenuItem("�ʐςŐF����(H)", 'H', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == showAreaBorderItem) {
			getPanel().setShowAreaBorder(target.isSelected());
		} else if (target == showAreaHintItem) {
			getPanel().setShowAreaHint(target.isSelected());
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
}
