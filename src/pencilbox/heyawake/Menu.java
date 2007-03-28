package pencilbox.heyawake;

import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�ւ�킯�v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem indicateErrorItem;
//	private JMenuItem indicateContinuousRoomItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		paintColorItem = addColorMenuItem("���}�X");
		circleColorItem = addColorMenuItem("���}�X");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("����ԂŎ���(E)", 'E', false));
//		addToViewMenu(indicateContinuousRoomItem = makeCheckBoxCommandMenuItem("�R�����A���x��(C)", 'C', false));
		addTrimAnswerMenuItem();
		addNoPaintMarkStyleMenu();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
//		else if (target == indicateContinuousRoomItem)
//			getPanel().setIndicateContinuousRoomMode(target.isSelected());
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
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
//		indicateContinuousRoomItem.setSelected(getPanel().isIndicateContinuousRoomMode());
	}

}
