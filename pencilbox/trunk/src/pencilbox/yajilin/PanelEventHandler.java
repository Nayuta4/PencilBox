package pencilbox.yajilin;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u���W�����v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(9);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �u���W�����v�}�E�X����
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.UNKNOWN);
	}

	protected void leftClicked(Address pos) {
		toggleState(pos, Board.BLACK);
	}

	protected void rightClicked(Address pos) {
		toggleState(pos, Board.WHITE);
	}
	
	/**
	 * �}�X�̏�Ԃ� �����st �Ő؂�ւ���
	 * @param pos �}�X���W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(Address pos, int st) {
		if (board.isNumber(pos))
			return;
		if (st == board.getNumber(pos))
			st = Board.BLANK;
		if (st == Board.BLACK) {
			board.eraseLinesAroundA(pos);
		}
		board.changeStateA(pos, st);
	}

	/**
	 * �n�_�}�X�ƏI�_�}�X�����񂾐���̏�Ԃ��w��̏�ԂɕύX����
	 * �n�_�}�X�ƏI�_�}�X�͓����s�܂��͓�����ɂȂ���΂Ȃ�Ȃ�
	 * @param pos0 �n�_�}�X�̍��W
	 * @param pos1 �I�_�}�X�̍��W
	 * @param st �ύX��̏��
	 */
	private void changeLineState(Address pos0, Address pos1, int st) {
		int direction = pos0.getDirectionTo(pos1);
		if (direction < 0)
			return;
		for (Address p = pos0; !p.equals(pos1); p.move(direction)) {
			SideAddress side = SideAddress.get(p, direction);
			if (board.getState(side) != st)
				if (!board.hasNumberOrBlack(side) || st == Board.UNKNOWN)
					board.changeStateA(side, st);
		}
	}

	/*
	 * �u���W�����v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.enterNumber(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.enterNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.eraseNumber(pos);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.eraseNumber(posS);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.enterNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.enterNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}
}
