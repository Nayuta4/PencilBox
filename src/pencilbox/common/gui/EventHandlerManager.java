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
 * �p�l���ɑ΂���}�E�X�C�L�[�{�[�h�̃C�x���g�������s���N���X
 */
public class EventHandlerManager {

	private PanelBase panel;
//	private BoardBase board;

	private PanelEventHandlerBase handler;
	private RegionEditHandler regionEditHandler;
	
	/**
	 * PanelEventHandler�𐶐�����
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
	 * �������𔻒胂�[�h�̏ꍇ�ɁC�����ςݏ�Ԃ��疢������Ԃɖ߂��B
	 */
	public void resetImmediateAnswerCheckMode() {
		handler.resetImmediateAnswerCheckMode();
	}

	/**
	 * 	�������𔻒�
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
	 * �}�E�X���X�i�[�C�L�[���X�i�[���p�l���ɓo�^����B
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
	 * �}�E�X���X�i�[�C�L�[���X�i�[���p�l������O���B
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
