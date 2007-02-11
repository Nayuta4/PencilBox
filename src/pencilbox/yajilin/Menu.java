package pencilbox.yajilin;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「ヤジリン」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem lineColorItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem warnBranchedLinkItem;
	private JMenuItem colorForEachLinkItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}
	
	protected void buildIndividualMenu() {
		lineColorItem = addColorMenuItem("線");
		paintColorItem = addColorMenuItem("黒マス");
		circleColorItem = addColorMenuItem("白マス");
		addToViewMenu(warnBranchedLinkItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(colorForEachLinkItem = makeCheckBoxCommandMenuItem("線の色分け(D)", 'D', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		addNoPaintMarkStyleMenu();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnBranchedLinkItem)
			getPanel().setWarnBranchedLink(target.isSelected());
		else if (target == colorForEachLinkItem)
			getPanel().setColorForEachLink(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == lineColorItem)
			return getPanel().getLineColor();
		else if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		warnBranchedLinkItem.setSelected(getPanel().isWarnBranchedLink());
		colorForEachLinkItem.setSelected(getPanel().isColorForEachLink());
	}

}
