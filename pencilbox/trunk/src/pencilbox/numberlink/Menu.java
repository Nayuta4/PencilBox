package pencilbox.numberlink;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「ナンバーリンク」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem numberColorItem;
	private JMenuItem lineColorItem;
//	private JMenuItem indicateErrorItem;
	private JMenuItem separateLinkColorItem;
	private JMenuItem highlightSelectionItem;
	
	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
//		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		super.buildViewMenu();
		numberColorItem = addColorMenuItem("数字");
		lineColorItem = addColorMenuItem("線");
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem("線の色分け(R)", 'R', false));
		addToViewMenu(highlightSelectionItem = makeCheckBoxCommandMenuItem("選択数字ハイライト(H)", 'H', false));
		addRenewColorMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
//		if (target == indicateErrorItem)
//			getPanel().setIndicateErrorMode(target.isSelected());
		if (target == separateLinkColorItem)
			getPanel().setSeparateLinkColorMode(target.isSelected());
		else if (target == highlightSelectionItem)
			getPanel().setHighlightSelectionMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == lineColorItem)
			return getPanel().getLineColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
//		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateLinkColorItem.setSelected(getPanel().isSeparateLinkColorMode());
		highlightSelectionItem.setSelected(getPanel().isHighlightSelectionMode());
	}

}
