package pencilbox.bijutsukan;

import java.awt.Color;

import javax.swing.JMenuItem;

import pencilbox.common.gui.MenuBase;

/**
 * �u���p�فv���j���[�N���X
 */
public class Menu extends MenuBase {

	private JMenuItem illuminationColorItem;
	private JMenuItem noilluminationColorItem;
	private JMenuItem lluminatedColorItem;
	private JMenuItem warnWrongIlluminationItem;

	public Panel getPanel() {
		return (Panel) getPanelBase();
	}

	protected void buildIndividualMenu(){
		illuminationColorItem = addColorMenuItem("�Ɩ�");
		noilluminationColorItem = addColorMenuItem("�Ɩ��Ȃ�");
		lluminatedColorItem = addColorMenuItem("��Ɩ��}�X");
		addToViewMenu(warnWrongIlluminationItem = makeCheckBoxCommandMenuItem("����Ԃŕ\��(W)", 'W', false));
	}

	public void executeCommand2(JMenuItem target) {
		if (target == warnWrongIlluminationItem) {
			getPanel().setWarnWrongIllumination(target.isSelected());
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

