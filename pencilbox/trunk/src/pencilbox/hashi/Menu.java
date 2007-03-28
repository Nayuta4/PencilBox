package pencilbox.hashi;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;


/**
 * 「橋をかけろ」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem lineColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem separateLinkColorItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		lineColorItem = addColorMenuItem("橋");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("正誤を色で示す(E)", 'E', false));
		addToViewMenu(separateLinkColorItem = makeCheckBoxCommandMenuItem("橋の色分け(R)", 'R', false));
		addRenewColorMenu();
		addSymmetricPlacementMenuItem();
	}
	
	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == separateLinkColorItem)
			getPanel().setSeparateLinkColorMode(target.isSelected());
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
	}

}
