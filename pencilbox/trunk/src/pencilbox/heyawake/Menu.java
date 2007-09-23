package pencilbox.heyawake;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u�ւ�킯�v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem numberColorItem;
	private JMenuItem areaBorderColorItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem indicateErrorItem;
//	private JMenuItem indicateContinuousRoomItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildEditMenu() {
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		addNoPaintMarkStyleMenu();
		super.buildViewMenu();
		areaBorderColorItem = addColorMenuItem("�̈拫�E");
		numberColorItem = addColorMenuItem("��萔��");
		paintColorItem = addColorMenuItem("���}�X");
		circleColorItem = addColorMenuItem("���}�X");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("����ԂŎ���(E)", 'E', false));
//		addToViewMenu(indicateContinuousRoomItem = makeCheckBoxCommandMenuItem("�R�����A���x��(C)", 'C', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
//		else if (target == indicateContinuousRoomItem)
//			getPanel().setIndicateContinuousRoomMode(target.isSelected());
	}

	public Color getColor(JMenuItem target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else if (target == areaBorderColorItem)
			return getPanel().getAreaBorderColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
		else if (target == areaBorderColorItem)
			getPanel().setAreaBorderColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}
	
	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
//		indicateContinuousRoomItem.setSelected(getPanel().isIndicateContinuousRoomMode());
	}

}
