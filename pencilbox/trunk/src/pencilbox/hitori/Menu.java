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
	private JMenuItem warnWrongWallItem;
	private JMenuItem hideSingleModeItem;
	private JMenuItem warnMultipleNumberItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		super.buildIndividualMenu();
		paintColorItem = addColorMenuItem("黒マス");
		circleColorItem = addColorMenuItem("白マス");
		addToViewMenu(selectLetterItem = makeCommandMenuItem("文字種類の設定(L)...", 'L'));
		addToViewMenu(selectLetterItem);
		addToViewMenu(warnWrongWallItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(hideSingleModeItem = makeCheckBoxCommandMenuItem("最初からひとりの数字を隠す(S)", 'S', false));
		addToViewMenu(warnMultipleNumberItem = makeCheckBoxCommandMenuItem("ひとりでない数字を赤で示す(H)", 'H', false));
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
		getPanel().setLetter(selection);
		getPanelBase().repaint();
	}

}
