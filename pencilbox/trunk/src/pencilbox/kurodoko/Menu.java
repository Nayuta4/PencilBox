package pencilbox.kurodoko;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u���}�X�͂ǂ����v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem warnWrongWallItem;
	private JMenuItem showNumberHintItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		paintColorItem = addColorMenuItem("���}�X");
		circleColorItem = addColorMenuItem("���}�X");
		addToViewMenu(warnWrongWallItem = makeCheckBoxCommandMenuItem("����Ԃŕ\��(W)", 'W', false));
		addToViewMenu(showNumberHintItem = makeCheckBoxCommandMenuItem("�����̐F����(H)", 'H', false));
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongWallItem)
			getPanel().setWarnWrongWall(target.isSelected());
		else if (target == showNumberHintItem)
			getPanel().setShowNumberHint(target.isSelected());
	}
	
	public Color getColor(JMenuItem target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else
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
		warnWrongWallItem.setSelected(getPanel().isWarnWrongWall());
		showNumberHintItem.setSelected(getPanel().isShowNumberHint());
	}

}
