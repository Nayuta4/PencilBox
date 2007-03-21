package pencilbox.sudoku;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「数独」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem inputColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem highlightSelectionItem;
	private JMenuItem dotHintItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		inputColorItem = addColorMenuItem("入力数字");
		addCursorMenu();
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem("選択数字ハイライト(H)", 'H', false));
		addToViewMenu(dotHintItem = makeCheckBoxCommandMenuItem("可能な数字をドットで示す(D)", 'D', false));
		addSymmetricPlacementMenuItem();
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
			getPanel().getInputColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		highlightSelectionItem.setSelected(getPanel().isHighlightSelectionMode());
		dotHintItem.setSelected(getPanel().isDotHintMode());
	}

}
