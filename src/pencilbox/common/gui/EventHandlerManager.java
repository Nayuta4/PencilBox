package pencilbox.common.gui;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;

import pencilbox.common.core.BoardBase;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * パネルに対するマウス，キーボードのイベント処理を行うクラス
 */
public class EventHandlerManager {

	private PanelBase panel;
//	private BoardBase board;

	private PanelEventHandlerBase handler;
	private RegionEditHandler regionEditHandler;
	
	/**
	 * PanelEventHandlerを生成する
	 */
	public EventHandlerManager(PencilType pencilType) throws PencilBoxClassException {
		this.handler = (PanelEventHandlerBase) ClassUtil.createInstance(pencilType, ClassUtil.PANEL_EVENT_HANDLER_CLASS);
		this.regionEditHandler = new RegionEditHandler();
	}

	public void setup(PanelBase panel, BoardBase board) {
		this.panel = panel;
//		this.board = board;
		handler.setup(panel, board);
		regionEditHandler.setup(panel, board, this);
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

	public void setEditMode(int mode) {
		int currentMode = panel.getEditMode();
		if (currentMode == PanelBase.PROBLEM_INPUT_MODE || currentMode == PanelBase.ANSWER_INPUT_MODE) {
			removeListenerFromPanel(handler);
		} else if (currentMode == PanelBase.REGION_EDIT_MODE) {
			removeListenerFromPanel(regionEditHandler);
		}
		if (mode == PanelBase.PROBLEM_INPUT_MODE || mode == PanelBase.ANSWER_INPUT_MODE) {
			addListenerToPanel(handler);
			handler.resetPreviousInput();
			resetImmediateAnswerCheckMode();
		} else if (mode == PanelBase.REGION_EDIT_MODE) {
			addListenerToPanel(regionEditHandler);
			regionEditHandler.init();
		}
		panel.setEditMode(mode);
	}

	/**
	 * マウスリスナー，キーリスナーをパネルに登録する。
	 * @param l
	 */
	private void addListenerToPanel(EventListener l) {
		if (l instanceof MouseListener)
			panel.addMouseListener((MouseListener) l);
		if (l instanceof MouseMotionListener)
			panel.addMouseMotionListener((MouseMotionListener) l);
		if (l instanceof KeyListener);
			panel.addKeyListener((KeyListener) l);
	}

	/**
	 * マウスリスナー，キーリスナーをパネルから外す。
	 * @param l
	 */
	private void removeListenerFromPanel(EventListener l) {
		if (l instanceof MouseListener)
			panel.removeMouseListener((MouseListener) l);
		if (l instanceof MouseMotionListener)
			panel.removeMouseMotionListener((MouseMotionListener) l);
		if (l instanceof KeyListener);
			panel.removeKeyListener((KeyListener) l);
	}

}
