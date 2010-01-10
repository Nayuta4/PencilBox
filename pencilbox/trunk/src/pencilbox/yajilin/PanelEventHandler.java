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

	private int currentState = Board.OUTER; // �h���b�O���̕ӂ̏�Ԃ�\��

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
	protected void leftPressed(Address pos) {
	}

	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void leftReleased(Address pos) {
		if (isOn(pos)) {
			if (currentState == Board.OUTER) {
				toggleState(pos, Board.BLACK);
			}
		}
		currentState = Board.OUTER;
	}

	protected void rightPressed(Address pos) {
		toggleState(pos, Board.WHITE);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightReleased(Address pos) {
		currentState = Board.OUTER;
	}

	/**
	 * �}�X�̏�Ԃ� �����st �Ő؂�ւ���
	 * @param pos �}�X���W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(Address pos, int st) {
		if (board.isNumber(pos))
			return;
		if (st == board.getNumber(pos)) {
			st = Board.BLANK;
		}
		if (st == Board.BLACK) {
			board.eraseLinesAroundA(pos);
		}
		board.changeStateA(pos, st);
		currentState = st;
	}

	/**
	 * �}�X�̏�Ԃ� currentState �ɐݒ肷��B�������A
	 * �E�����}�X�͕ύX���Ȃ�
	 * �E�m�蔒�}�X�͍��}�X���㏑�����Ȃ�
	 * @param pos
	 */
	private void sweepState(Address pos) {
		int st = board.getNumber(pos);
		if (st >=0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		if (currentState == Board.WHITE && st == Board.BLACK)
			return;
		board.changeStateA(pos, currentState);
	}
	/**
	 * �n�_�}�X�ƏI�_�}�X�����񂾐���̏�Ԃ��w��̏�ԂɕύX����
	 * �n�_�̕ӂ̌��݂̏�Ԃ��w��̏�Ԃł���΁C����ɕύX����
	 * @param pos0 �n�_�}�X�̍��W
	 * @param pos1 �I�_�}�X�̍��W
	 * @param st �ύX��̏��
	 */
	private void changeLineState(Address pos0, Address pos1, int st) {
		int direction = pos0.getDirectionTo(pos1);
		if (direction < 0) {
			currentState = st;
			return;
		}
		SideAddress side = SideAddress.get(pos0, direction);
		if (currentState == Board.OUTER) {
			if (board.getState(side) == st) {
				currentState = Board.UNKNOWN;
			} else {
				currentState = st;
			}
		}
		for (Address p = pos0; !p.equals(pos1); p.move(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				if (!board.hasNumberOrBlack(side) || st == Board.UNKNOWN)
					board.changeStateA(side, currentState);
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
