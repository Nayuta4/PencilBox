package pencilbox.fillomino;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「フィルオミノ」メニュークラス
 */
public class Menu extends MenuBase {
	
	private JMenuItem numberColorItem;
	private JMenuItem inputColorItem;
	private JMenuItem areaBorderColorItem;
	private JMenuItem showAreaBorderItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateAreaColorItem;
	
	public Panel getPanel() {
		return (Panel)getPanelBase();
	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addCursorMenuItem();
		super.buildViewMenu();
		numberColorItem = addColorMenuItem("問題数字");
		inputColorItem = addColorMenuItem("解答数字");
		areaBorderColorItem = addColorMenuItem("領域境界");
		addToViewMenu(showAreaBorderItem = makeCheckBoxCommandMenuItem("境界線表示(B)", 'B', true));
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("正誤を色で示す(E)", 'E', false));
		addToViewMenu(separateAreaColorItem = makeCheckBoxCommandMenuItem("領域の色分け(R)", 'R', false));
		addRenewColorMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == showAreaBorderItem) {
			getPanel().setShowAreaBorderMode(target.isSelected());
		} else if (target == indicateErrorItem) {
			getPanel().setIndicateErrorMode(target.isSelected());
		} else if (target == separateAreaColorItem) {
			getPanel().setSeparateAreaColorMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == inputColorItem)
			return getPanel().getInputColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == inputColorItem)
			getPanel().setInputColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else
			super.setColor(target, color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		showAreaBorderItem.setSelected(getPanel().isShowAreaBorderMode());
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		separateAreaColorItem.setSelected(getPanel().isSeparateAreaColorMode());
	}

}
