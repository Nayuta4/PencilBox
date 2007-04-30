package pencilbox.bijutsukan;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「美術館」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem bulbColorItem;
	private JMenuItem noBulbColorItem;
	private JMenuItem lluminatedCellColorItem;
	private JMenuItem indicateErrorItem;
	private JMenuItem paintIlluminatedCellModeItem;
	private JMenuItem showBeamItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildEditMenu() {
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
		super.buildEditMenu();
	}

	protected void buildViewMenu() {
		buildMarkStyleMenu("照明なしの印(N)", 'N', new int[] {3, 4});
		super.buildViewMenu();
		bulbColorItem = addColorMenuItem("照明");
		noBulbColorItem = addColorMenuItem("照明なし");
		lluminatedCellColorItem = addColorMenuItem("照らされたマス");
		addToViewMenu(indicateErrorItem = makeCheckBoxCommandMenuItem("誤りを赤で示す(E)", 'E', false));
		addToViewMenu(paintIlluminatedCellModeItem = makeCheckBoxCommandMenuItem("照らされたマスを塗る(P)", 'P', true));
		addToViewMenu(showBeamItem = makeCheckBoxCommandMenuItem("光線表示(B)", 'B', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == indicateErrorItem) {
			getPanel().setIndicateErrorMode(target.isSelected());
		} else if (target == paintIlluminatedCellModeItem) {
			getPanel().setPaintIlluminatedCellMode(target.isSelected());
		} else if (target == showBeamItem) {
			getPanel().setShowBeamMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == bulbColorItem)
			return getPanel().getBulbColor();
		else if (target == noBulbColorItem)
			return getPanel().getNoBulbColor();
		else if (target == lluminatedCellColorItem)
			return getPanel().getIlluminatedCellColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == bulbColorItem)
			getPanel().setBulbColor(color);
		else if (target == noBulbColorItem)
			getPanel().setNoBulbColor(color);
		else if (target == lluminatedCellColorItem)
			getPanel().setIlluminatedCellColor(color);
	}

	public void updateCurrentMenuSelection() {
		super.updateCurrentMenuSelection();
		indicateErrorItem.setSelected(getPanel().isIndicateErrorMode());
		paintIlluminatedCellModeItem.setSelected(getPanel().isPaintIlluminatedCellMode());
		showBeamItem.setSelected(getPanel().isShowBeamMode());
	}

}
