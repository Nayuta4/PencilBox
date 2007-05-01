/**
 * 
 */
package pencilbox.common.gui;


/**
 * 
 */
public class PreferencesCopierBase {
	
	/**
	 * メニュー選択をコピーする
	 * @param src コピー元フレームの MenuCommand インスタンス
	 * @param dst コピー先フレームの MenuCommand インスタンス
	 */
	public void copyPreferences(MenuCommand src, MenuCommand dst) {
		PanelBase panelS = src.getPanelBase();
		PanelBase panelD = dst.getPanelBase();
		PanelEventHandlerBase handlerS = src.getPanelEventHandlerBase();
		PanelEventHandlerBase handlerD = dst.getPanelEventHandlerBase();
		panelD.setDisplaySize(panelS.getCellSize());
		panelD.changeIndexMode(panelS.isIndexMode());
		panelD.setGridStyle(panelS.getGridStyle());
		panelD.setMarkStyle(panelS.getMarkStyle());
		panelD.setCursorMode(panelS.isCursorMode());
		handlerD.setProblemEditMode(handlerS.isProblemEditMode());
		handlerD.setSymmetricPlacementMode(handlerS.isSymmetricPlacementMode());
		handlerD.setImmediateAnswerCheckMode(handlerS.isImmediateAnswerCheckMode());
	}
}
