/**
 * 
 */
package pencilbox.common.gui;


/**
 * 
 */
public class PreferencesCopierBase {
	
	/**
	 * ���j���[�I�����R�s�[����
	 * @param src �R�s�[���t���[���� MenuCommand �C���X�^���X
	 * @param dst �R�s�[��t���[���� MenuCommand �C���X�^���X
	 */
	public void copyPreferences(MenuCommand src, MenuCommand dst) {
		PanelBase panelS = src.getPanelBase();
		PanelBase panelD = dst.getPanelBase();
		PanelEventHandlerBase handlerS = src.getPanelEventHandlerBase();
		PanelEventHandlerBase handlerD = dst.getPanelEventHandlerBase();
		panelD.setDisplaySize(panelS.getCellSize());
		panelD.changeShowIndexMode(panelS.isShowIndexMode());
		panelD.setDisplayStyle(panelS.getDisplayStyle());
		panelD.changeShowIndexMode(panelS.isShowIndexMode());
		panelD.setCursorOn(panelS.isCursorOn());
		handlerD.setProblemEditMode(handlerS.isProblemEditMode());
		handlerD.setSymmetricPlacementMode(handlerS.isSymmetricPlacementMode());
	}
}
