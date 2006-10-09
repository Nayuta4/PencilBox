package pencilbox.slitherlink;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「スリザーリンク」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem lineColorItem;
	private JMenuItem crossColorItem;
	private JMenuItem warnBranchedLinkItem;
	private JMenuItem colorForEachLinkItem;
	
	public Panel getPanel() {
		return (Panel) getPanelBase();
	}
	
	protected void buildIndividualMenu() {
		lineColorItem = addColorMenuItem("線");
		crossColorItem = addColorMenuItem("×印");
		addToViewMenu(warnBranchedLinkItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(colorForEachLinkItem = makeCheckBoxCommandMenuItem("線の色分け(D)", 'D', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
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
		else if (target == crossColorItem)
			return getPanel().getCrossColor();
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == lineColorItem)
			getPanel().setLineColor(color);
		else if (target == crossColorItem)
			getPanel().setCrossColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		warnBranchedLinkItem.setSelected(getPanel().isWarnBranchedLink());
		colorForEachLinkItem.setSelected(getPanel().isColorForEachLink());
	}

}