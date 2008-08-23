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
	private JMenuItem numberColorItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem hideSoleNumberItem;
//	private JMenuItem indicateRedundantNumberItem;

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
		numberColorItem = addColorMenuItem(Messages.getString("Menu.numberColorItem")); //$NON-NLS-1$
		paintColorItem = addColorMenuItem(Messages.getString("Menu.paintColorItem")); //$NON-NLS-1$
		circleColorItem = addColorMenuItem(Messages.getString("Menu.circleColorItem")); //$NON-NLS-1$
		addToViewMenu(selectLetterItem = makeCommandMenuItem(Messages.getString("Menu.selectLetterItem"), 'T')); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(hideSoleNumberItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.hideSoleNumberItem"), 'H', false)); //$NON-NLS-1$
//		addToViewMenu(indicateRedundantNumberItem = makeCheckBoxCommandMenuItem("�d�����鐔����ԂŎ���(R)", 'R', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem)
			getPanel().setIndicateErrorMode(target.isSelected());
		else if (target == hideSoleNumberItem)
			getPanel().setHideSoleNumberMode(target.isSelected());
//		else if (target == indicateRedundantNumberItem)
//			getPanel().setIndicateRedundantNumberMode(target.isSelected());
		else if (target == selectLetterItem)
			selectLetter();
	}

	public Color getColor(JMenuItem target) {
		if (target == paintColorItem)
			return getPanel().getPaintColor();
		else if (target == circleColorItem)
			return getPanel().getCircleColor();
		else if (target == numberColorItem)
			return getPanel().getNumberColor();
		else
			return super.getColor(target);
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == paintColorItem)
			getPanel().setPaintColor(color);
		else if (target == circleColorItem)
			getPanel().setCircleColor(color);
		else if (target == numberColorItem)
			getPanel().setNumberColor(color);
		else
			super.setColor(target, color);
	}
	
	/**
	 * �u������ނ̐ݒ�v���j���[���ڂ���������
	 */
	void selectLetter() {
		String[] options =
		{ "1", "A", "��", "�@", "�A", "��", "��", "��" };
		String message = "";
		String title = Messages.getString("Menu.SelectLetterDialog"); //$NON-NLS-1$
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
		getPanel().setLetters(Letters.getLetterSeries(selection));
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		hideSoleNumberItem.setSelected(getPanel().isHideSoleNumberMode());
//		indicateRedundantNumberItem.setSelected(getPanel().isIndicateRedundantNumberMode());
	}

}
