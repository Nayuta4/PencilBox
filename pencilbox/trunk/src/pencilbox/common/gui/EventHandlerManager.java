package pencilbox.common.gui;

import pencilbox.common.core.BoardBase;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * パネルに対するマウス，キーボードのイベント処理を行うクラス
 */
public class EventHandlerManager {

	private PanelEventHandlerBase handler;

	/**
	 * PanelEventHandlerを生成する
	 */
	public EventHandlerManager(PencilType pencilType) throws PencilBoxClassException {
		this.handler = (PanelEventHandlerBase) ClassUtil.createInstance(pencilType, ClassUtil.PANEL_EVENT_HANDLER_CLASS);
	}

	public void setup(PanelBase panel, BoardBase board) {
		handler.setup(panel, board);
	}

	public void setup(BoardBase board) {
		handler.setup(board);
	}

	/**
	 * @return the symmetricPlacementMode
	 */
	public boolean isSymmetricPlacementMode() {
		return handler.isSymmetricPlacementMode();
	}
	/**
	 * @param b the symmetricPlacementMode to set
	 */
	public void setSymmetricPlacementMode(boolean b) {
		handler.setSymmetricPlacementMode(b);
	}

	/**
	 * @return the immediateAnswerCheckMode
	 */
	public boolean isImmediateAnswerCheckMode() {
		return handler.isImmediateAnswerCheckMode();
	}
	/**
	 * @param b the immediateAnswerCheckMode to set
	 */
	public void setImmediateAnswerCheckMode(boolean b) {
		handler.setImmediateAnswerCheckMode(b);
	}
	/**
	 * 即時正解判定モードの場合に，正解済み状態から未正解状態に戻す。
	 */
	public void resetImmediateAnswerCheckMode() {
		handler.resetImmediateAnswerCheckMode();
	}

	/**
	 * 	即時正解判定
	 */
	public void checkAnswer() {
		handler.checkAnswer();
	}

	public void setEditMode(int m) {
		handler.setProblemEditMode(m == PanelBase.PROBLEM_INPUT_MODE);
	}

}
