package pencilbox.nurikabe;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;


/**
 * 「ぬりかべ」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem showWrongWallItem;
	private JMenuItem showShimaSizeItem;
	private JMenuItem colorForEachWallItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu() {
		paintColorItem = addColorMenuItem("黒マス");
		circleColorItem = addColorMenuItem("白マス");
		addToViewMenu(showWrongWallItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(colorForEachWallItem = makeCheckBoxCommandMenuItem("壁の色分け(D)", 'D', false));
		addToViewMenu(showShimaSizeItem = makeCheckBoxCommandMenuItem("シマの数を数える(H)",'H', false));
		addRenewColorMenu();	
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == showWrongWallItem)
			getPanel().setShowWrongWall(target.isSelected());
		else if (target == showShimaSizeItem)
			getPanel().setShowShimaSize(target.isSelected());
		else if (target == colorForEachWallItem)
			getPanel().setColorForEachWall(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
	}
}
