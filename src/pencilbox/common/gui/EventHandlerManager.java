package pencilbox.common.gui;

import pencilbox.common.core.BoardBase;
import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;
import pencilbox.common.factory.PencilType;

/**
 * �p�l���ɑ΂���}�E�X�C�L�[�{�[�h�̃C�x���g�������s���N���X
 */
public class EventHandlerManager {

	private PanelEventHandlerBase handler;

	/**
	 * PanelEventHandler�𐶐�����
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

	public void setEditMode(int m) {
		handler.setProblemEditMode(m == PanelBase.PROBLEM_INPUT_MODE);
	}

}
