package pencilbox.numberlink;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「ナンバーリンク」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem lineColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateLinkColorItem;
	private JMenuItem highlightSelectionItem;
	
	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	
	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		lineColorItem = addColorMenuItem("線");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem("線の色分け(R)", 'R', false));
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem("選択数字ハイライト(H)", 'H', false));
		addRenewColorMenu();
//		addTrimAnswerMenuItem();
		addSymmetricPlacementMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == separateLinkColorItem)
			getPanel().setSeparateLinkColorMode(target.isSelected());
		else if (target == highlightSelectionItem)
			getPanel().setHighlightSelectionMode(target.isSelected());
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

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
		highlightSelectionItem.setSelected(getPanel().isHighlightSelectionMode());
	}

}
