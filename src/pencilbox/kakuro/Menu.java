package pencilbox.kakuro;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「カックロ」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem inputColorItem;
	private JMenuItem warnWrongNumberItem;
	private JMenuItem showArrowedNumberDotItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		inputColorItem = addColorMenuItem("入力数字");
		addCursorMenu();
		addToViewMenu(warnWrongNumberItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(showArrowedNumberDotItem = makeCheckBoxCommandMenuItem("可能な数字をドット表示(D)", 'D', false));
		addSymmetricPlacementMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongNumberItem)
			getPanel().setWarnWrongNumber(target.isSelected());
		else if (target == showArrowedNumberDotItem)
			getPanel().setShowAllowedNumberDot(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			getPanel().getInputColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
	}

	protected void buildRotationMenu() {
		super.buildRotationMenu2();
	}
	
}

