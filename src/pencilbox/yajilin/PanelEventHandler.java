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

	private int currentState = NULLSTATE; // �h���b�O���̕ӂ̏�Ԃ�\��
	private static final int NULLSTATE  = -9;
	private static final int PRE_BLACK = -19;

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
		if (board.getNumber(pos) == Board.BLACK)
			currentState = PRE_BLACK;
	}

	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (currentState == PRE_BLACK) {
			currentState = Board.BLACK;
		}
		if (currentState == Board.BLACK) {
			sweepState(dragEnd);
		} else {
			changeLineState(dragStart, dragEnd, Board.LINE);
		}
	}

	protected void leftReleased(Address pos) {
		if (isOn(pos)) {
			if (currentState == NULLSTATE) {
				toggleState(pos, Board.BLACK);
			} else if (currentState == PRE_BLACK) {
				toggleState(pos, Board.BLACK);
			}
		}
		currentState = NULLSTATE;
	}

	protected void rightPressed(Address pos) {
		toggleState(pos, Board.WHITE);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightReleased(Address pos) {
		currentState = NULLSTATE;
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
			board.eraseLinesAround(pos);
		}
		board.changeState(pos, st);
		currentState = st;
	}

	/**
	 * �}�X�̏�Ԃ� currentState �ɐݒ肷��B�������A
	 * �E�����}�X�͕ύX���Ȃ��B
	 * �E�m�蔒�}�X�͍��}�X���㏑�����Ȃ��B
	 * �E���}�X�͊m�蔒�}�X����ѐ����㏑�������A�^�e���R�ɘA�����Ȃ��B
	 * @param pos
	 */
	private void sweepState(Address pos) {
		int st = board.getNumber(pos);
		if (st >=0 || st == Board.UNDECIDED_NUMBER)
			return;
		if (st == currentState)
			return;
		if (currentState == Board.BLACK) {
			if (st == Board.WHITE)
				return;
			if (board.countLine(pos) > 0)
				return;
			if (board.isBlock(pos))
				return;
		} else if (currentState == Board.WHITE) {
			if (st == Board.BLACK)
				return;
		}
		board.changeState(pos, currentState);
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
		if (currentState == NULLSTATE) {
			if (board.getState(side) == st) {
				currentState = Board.UNKNOWN;
			} else {
				currentState = st;
			}
		}
		for (Address p = pos0; !p.equals(pos1); p = p.nextCell(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				if (!board.hasNumberOrBlack(side) || st == Board.UNKNOWN)
					board.changeState(side, currentState);
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
