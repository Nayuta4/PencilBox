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
		toggleState(pos, Board.BLACK);
		currentState = board.getState(pos);
	}

	protected void rightPressed(Address pos) {
		toggleState(pos, Board.WHITE);
		currentState = board.getState(pos);
	}

	protected void leftDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	/**
	 * �}�X�̏�Ԃ� ���� �� st �ƕύX����
	 * @param pos �}�X���W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(Address pos, int st) {
		if (st == board.getState(pos)) {
			st = Board.UNKNOWN;
		}
		board.changeState(pos, st);
	}

	private void sweepState(Address pos) {
		int st = board.getState(pos);
		if (currentState == st)
			return;
		if (currentState == Board.BLACK && board.isBlock(pos))
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * �u�ЂƂ�ɂ��Ă���v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
				board.changeNumber(pos, num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.changeNumber(pos, 0);
	}

//	protected void starEntered(Address pos) {
//		if (problemEditMode)
//			board.changeNumber(pos.r, pos.c, Board.UNDEFINED_NUMBER);
//	}

}
