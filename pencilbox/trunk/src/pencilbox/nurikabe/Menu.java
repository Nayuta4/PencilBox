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
//	private JMenuItem indicateErrorItem;
	private JMenuItem countAreaSizeItem;
	private JMenuItem separateAreaColorItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		paintColorItem = addColorMenuItem("黒マス");
		circleColorItem = addColorMenuItem("白マス");
//		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem("壁の色分け(R)", 'R', false));
		addToViewMenu(countAreaSizeItem = makeCheckBoxCommandMenuItem("シマのマス数を数える(C)",'C', false));
		addRenewColorMenuItem();	
	}

	public void executeCommand2(JMenuItem target) {
//		if (target == indicateErrorItem)
//			getPanel().setIndicateError(target.isSelected());
//		else
		if (target == countAreaSizeItem)
			getPanel().setCountAreaSizeMode(target.isSelected());
		else if (target == separateAreaColorItem)
			getPanel().setSeparateAreaColorMode(target.isSelected());
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

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
//		indicateErrorItem.setSelected(getPanel().isIndicateError());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
		countAreaSizeItem.setSelected(getPanel().isCountAreaSizeMode());
	}

}
