package pencilbox.numberlink;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「ナンバーリンク」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem lineColorItem;
	private JMenuItem warnBranchedLinkItem;
	private JMenuItem colorForEachLinkItem;
	private JMenuItem highlightSelectedLinkItem;
	
	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	
	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		lineColorItem = addColorMenuItem("線");
		addToViewMenu(warnBranchedLinkItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(colorForEachLinkItem = makeCheckBoxCommandMenuItem("線の色分け(D)", 'D', false));
		addToViewMenu(highlightSelectedLinkItem = makeCheckBoxCommandMenuItem("選択数字ハイライト(H)", 'H', false));
		addRenewColorMenu();
//		addTrimAnswerMenuItem();
		addSymmetricPlacementMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnBranchedLinkItem)
			getPanel().setWarnBranchedLink(target.isSelected());
		else if (target == colorForEachLinkItem)
			getPanel().setColorForEachLink(target.isSelected());
		else if (target == highlightSelectedLinkItem)
			getPanel().setHighlightSelectedLink(target.isSelected());
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

	protected void buildGridStyleMenu() {
		super.buildGridStyleMenu();
		makeDisplayStyleItem(0, "通常表示").setSelected(true);
		makeDisplayStyleItem(1, "経路表示");
		makeDisplayStyleItem(2, "罫線非表示");
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		warnBranchedLinkItem.setSelected(getPanel().isWarnBranchedLink());
		colorForEachLinkItem.setSelected(getPanel().isColorForEachLink());
		highlightSelectedLinkItem.setSelected(getPanel().isHighlightSelectedLink());
	}

}
