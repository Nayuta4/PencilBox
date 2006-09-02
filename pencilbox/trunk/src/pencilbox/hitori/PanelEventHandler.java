package pencilbox.hitori;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�ЂƂ�ɂ��Ă���v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
		setMaxInputNumber(board.getMaxNumber()); 
	}
	
	/*
	 * �u�ЂƂ�ɂ��Ă���v�}�E�X����
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.BLACK);
	}
	protected void rightPressed(Address pos) {
		board.toggleState(pos.r(), pos.c(), Board.WHITE);
		currentState = board.getState(pos.r(), pos.c());
	}

	protected void leftDragged(Address pos) {
		// �������Ȃ�
	}

	protected void rightDragged(Address pos) {
		int st = board.getState(pos.r(), pos.c());
		if (st == currentState)
			return;
		board.changeStateA(pos.r(), pos.c(), currentState);
	}

	/*
	 * �u�ЂƂ�ɂ��Ă���v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.setNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}

//	protected void starEntered(Address pos) {
//		if (problemEditMode)
//			board.setNumber(pos.r, pos.c, Board.UNDEFINED_NUMBER);
//	}

}
