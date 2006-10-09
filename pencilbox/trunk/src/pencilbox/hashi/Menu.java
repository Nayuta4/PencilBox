package pencilbox.hashi;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;


/**
 * 「橋をかけろ」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem bridgeColorItem;
	private JMenuItem showNumberHintItem;
	private JMenuItem colorForEachLinkItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		bridgeColorItem = addColorMenuItem("橋");
		addGridStyleMenu();
		addToViewMenu(showNumberHintItem = makeCheckBoxCommandMenuItem("数字の色分け(H)", 'H', false));
		addToViewMenu(colorForEachLinkItem = makeCheckBoxCommandMenuItem("橋の色分け(D)", 'D', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
	}
	
	public void executeCommand2(JMenuItem target) {
		if (target == showNumberHintItem)
			getPanel().setShowNumberHint(target.isSelected());
		else if (target == colorForEachLinkItem)
			getPanel().setColorForEachLink(target.isSelected());
	}
	
	public Color getColor(JMenuItem target) {
		if (target == bridgeColorItem)
			return getPanel().getBridgeColor();
		else
			return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == bridgeColorItem)
			getPanel().setBridgeColor(color);
	}

	protected void buildGridStyleMenu() {
		super.buildGridStyleMenu();
		makeDisplayStyleItem(0, "通常表示");
		makeDisplayStyleItem(2, "罫線非表示").setSelected(true);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		showNumberHintItem.setSelected(getPanel().isShowNumberHint());
		colorForEachLinkItem.setSelected(getPanel().isColorForEachLink());
	}

}
