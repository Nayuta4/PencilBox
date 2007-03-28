package pencilbox.hitori;

import java.awt.Color;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import pencilbox.common.gui.MenuBase;


/**
 * 「ひとりにしてくれ」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem selectLetterItem;
	private JMenuItem paintColorItem;
	private JMenuItem circleColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem hideSoleNumberItem;
//	private JMenuItem indicateRedundantNumberItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		paintColorItem = addColorMenuItem("黒マス");
		circleColorItem = addColorMenuItem("白マス");
		addToViewMenu(selectLetterItem = makeCommandMenuItem("文字種類の設定(T)...", 'T'));
		addToViewMenu(selectLetterItem);
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(hideSoleNumberItem = makeCheckBoxCommandMenuItem("初めからひとりの数字を隠す(H)", 'H', false));
//		addToViewMenu(indicateRedundantNumberItem = makeCheckBoxCommandMenuItem("重複する数字を赤で示す(R)", 'R', false));
		addTrimAnswerMenuItem();
		addNoPaintMarkStyleMenu();
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
	 * 「文字種類の設定」メニュー項目を処理する
	 */
	void selectLetter() {
		String[] options =
		{ "1", "A", "Α", "А", "ア", "い", "☆", "鱸" };
		String message = "";
		String title = "文字種類の設定";
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
		getPanelBase().repaint();
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		hideSoleNumberItem.setSelected(getPanel().isHideSoleNumberMode());
//		indicateRedundantNumberItem.setSelected(getPanel().isIndicateRedundantNumberMode());
	}

}
