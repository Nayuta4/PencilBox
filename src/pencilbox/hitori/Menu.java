package pencilbox.hitori;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;


/**
 * �u�ЂƂ�ɂ��Ă���v���j���[�N���X
 */
public class Menu extends MenuBase {

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
		circleColorItem = addColorMenuItem(Messages.getString("Menu.noPaintColorItem")); //$NON-NLS-1$
		addToViewMenu(selectLetterItem = makeCommandMenuItem(Messages.getString("Menu.selectLetterItem"), 'T')); //$NON-NLS-1$
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(hideSoleNumberItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.hideSoleNumberItem"), 'H', false)); //$NON-NLS-1$
//		addToViewMenu(indicateRedundantNumberItem = makeCheckBoxCommandMenuItem("�d�����鐔����ԂŎ���(R)", 'R', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == selectLetterItem)
			selectLetter();
		super.executeCommand2(target);
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
