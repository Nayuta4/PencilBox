package pencilbox.heyawake;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「へやわけ」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem warnWrongWallItem;
	private JMenuItem showContinuousRoomItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		paintColorItem = addColorMenuItem("黒マス");
		circleColorItem = addColorMenuItem("白マス");
		addToViewMenu(warnWrongWallItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(showContinuousRoomItem = makeCheckBoxCommandMenuItem("３部屋連続警告(C)", 'C', false));
		addTrimAnswerMenuItem();
		addNoPaintMarkStyleMenu();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongWallItem)
			getPanel().setWarnWrongWall(target.isSelected());
		else if (target == showContinuousRoomItem)
			getPanel().setShowContinuousRoom(target.isSelected());
		getPanelBase().repaint();
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
	}
	
	public Color getColorS(AbstractButton target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else
			return null;
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		warnWrongWallItem.setSelected(getPanel().isWarnWrongWall());
		showContinuousRoomItem.setSelected(getPanel().isShowContinuousRoom());
	}

}
