package pencilbox.hitori;

import java.awt.Color;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pencilbox.common.gui.MenuBase;


/**
 * �u�ЂƂ�ɂ��Ă���v���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem selectLetterItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem warnWrongWallItem;
	private JMenuItem hideSingleModeItem;
	private JMenuItem warnMultipleNumberItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		paintColorItem = addColorMenuItem("���}�X");
		circleColorItem = addColorMenuItem("���}�X");
		addToViewMenu(selectLetterItem = makeCommandMenuItem("������ނ̐ݒ�(L)...", 'L'));
		addToViewMenu(selectLetterItem);
		addToViewMenu(warnWrongWallItem = makeCheckBoxCommandMenuItem("����Ԃŕ\��(W)", 'W', false));
		addToViewMenu(hideSingleModeItem = makeCheckBoxCommandMenuItem("�ŏ�����ЂƂ�̐������B��(S)", 'S', false));
		addToViewMenu(warnMultipleNumberItem = makeCheckBoxCommandMenuItem("�ЂƂ�łȂ�������ԂŎ���(H)", 'H', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongWallItem)
			getPanel().setWarnWrongWall(target.isSelected());
		else if (target == hideSingleModeItem)
			getPanel().setHideSingleMode(target.isSelected());
		else if (target == warnMultipleNumberItem)
			getPanel().setWarnMultipleNumber(target.isSelected());
		else if (target == selectLetterItem)
			selectLetter();
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
	
	/**
	 * �u������ނ̐ݒ�v���j���[���ڂ���������
	 */
	void selectLetter() {
		String[] options =
		{ "1", "A", "��", "�@", "�A", "��", "��", "��" };
		String message = "";
		String title = "������ނ̐ݒ�";
		int selection = JOptionPane.showOptionDialog(
					null,
					message,
					title,
					JOptionPane.PLAIN_MESSAGE,
					JOptionPane.DEFAULT_OPTION,
					null,
					options,
					options[0]);
		if (selection == JOptionPane.CLOSED_OPTION)
			return;
		getPanel().setLetter(selection);
		getPanelBase().repaint();
	}

}
