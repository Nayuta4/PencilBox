package pencilbox.bijutsukan;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * 「美術館」メニュークラス
 */
public class Menu extends MenuBase {

	private JMenuItem illuminationColorItem;
	private JMenuItem noilluminationColorItem;
	private JMenuItem lluminatedColorItem;
	private JMenuItem warnWrongIlluminationItem;
	private JMenuItem showBeamItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		illuminationColorItem = addColorMenuItem("照明");
		noilluminationColorItem = addColorMenuItem("照明なし");
		lluminatedColorItem = addColorMenuItem("被照明マス");
		addToViewMenu(warnWrongIlluminationItem = makeCheckBoxCommandMenuItem("誤りを赤で表示(W)", 'W', false));
		addToViewMenu(showBeamItem = makeCheckBoxCommandMenuItem("光線表示(B)", 'R', false));
		addSymmetricPlacementMenuItem();
		addTrimAnswerMenuItem();
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongIlluminationItem) {
			getPanel().setWarnWrongIllumination(target.isSelected());
		} else if (target == showBeamItem) {
			getPanel().setShowBeamMode(target.isSelected());
		}
	}

	public Color getColor(JMenuItem target) {
		if (target == illuminationColorItem)
			return getPanel().getIlluminationColor();
		else if (target == noilluminationColorItem)
			return getPanel().getNoilluminationColor();
		else if (target == lluminatedColorItem)
			return getPanel().getIlluminatedColor();
		return null;
	}

	public void setColor(JMenuItem target, Color color) {
		if (target == illuminationColorItem)
			getPanel().setIlluminationColor(color);
		else if (target == noilluminationColorItem)
			getPanel().setNoilluminationColor(color);
		else if (target == lluminatedColorItem)
			getPanel().setIlluminatedColor(color);
	}

}

