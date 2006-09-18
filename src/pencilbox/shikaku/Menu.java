package pencilbox.shikaku;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「四角に切れ」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem areaBorderColorItem;
	private JMenuItem areaPaintColorItem;
	private JMenuItem showAreaHintItem;
	private JMenuItem colorfulModeItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		areaBorderColorItem = addColorMenuItem("四角の枠");
		areaPaintColorItem = addColorMenuItem("四角の内部");
		addToViewMenu(showAreaHintItem = makeCheckBoxCommandMenuItem("面積で色分け(H)", 'H', false));
		addToViewMenu(colorfulModeItem = makeCheckBoxCommandMenuItem("ランダムに色分け(D)", 'D', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == showAreaHintItem) {
			getPanel().setShowAreaHint(target.isSelected());
		} else if (target == colorfulModeItem) {
			getPanel().setColorfulMode(target.isSelected());
		}
	}

	
	public Color getColor(JMenuItem target) {
		if (target == areaBorderColorItem)
			getPanel().getAreaBorderColor();
		else if (target == areaPaintColorItem)
			getPanel().getAreaPaintColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == areaPaintColorItem)
			getPanel().setAreaPaintColor(color);
	}
	
}
