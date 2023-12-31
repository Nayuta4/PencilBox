package pencilbox.hitori;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import pencilbox.common.gui.Letters;
import pencilbox.common.gui.MenuBase;
import pencilbox.resource.Messages;


/**
 * 「ひとりにしてくれ」メニュークラス
 */
public class Menu extends MenuBase {

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
		addToViewMenu(selectLetterItem = makeJMenuItem(Messages.getString("Menu.selectLetterItem"), 'T')); //$NON-NLS-1$
		selectLetterItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectLetter();
				getPanel().repaint();
			}
		});
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.indicateErrorItem"), 'E', false)); //$NON-NLS-1$
		addToViewMenu(hideSoleNumberItem = makeCheckBoxCommandMenuItem(Messages.getString("Menu.hideSoleNumberItem"), 'H', false)); //$NON-NLS-1$
//		addToViewMenu(indicateRedundantNumberItem = makeCheckBoxCommandMenuItem("重複する数字を赤で示す(R)", 'R', false));
	}

	/**
	 * 「文字種類の設定」メニュー項目を処理する
	 */
	void selectLetter() {
		String[] options =
		{ "1", "A", "Α", "А", "ア", "い", "☆", "鱸" };
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
		getPanelBase().setLetters(Letters.getLetterSeries(selection));
	}

}
