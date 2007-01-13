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
		panelD.changeShowIndexMode(panelS.isShowIndexMode());
		panelD.setGridStyle(panelS.getGridStyle());
		panelD.changeShowIndexMode(panelS.isShowIndexMode());
		panelD.setCursorOn(panelS.isCursorOn());
		handlerD.setProblemEditMode(handlerS.isProblemEditMode());
		handlerD.setSymmetricPlacementMode(handlerS.isSymmetricPlacementMode());
		handlerD.setImmediateAnswerCheckMode(handlerS.isImmediateAnswerCheckMode());
	}
}
