package pencilbox.slalom;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�X�����[���v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = Board.OUTER; // �h���b�O���̕ӂ̏�Ԃ�\��
	private int currentGate = Board.GATE_HORIZ;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(99);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �u�X�����[���v�}�E�X����
	 */
	protected void leftPressedEdge(SideAddress side) {
//		toggleState(side, Board.LINE);
	}

	protected void rightPressedEdge(SideAddress side) {
		toggleState(side, Board.NOLINE);
	}

	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void leftReleased(Address pos) {
		currentState = Board.OUTER;
	}

	/**
	 * �ӂ̏�Ԃ� �����st �Ő؂�ւ���
	 * @param pos �Ӎ��W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(SideAddress pos, int st) {
		if (st == board.getState(pos)) {
			st = Board.UNKNOWN;
		}
		if (!board.hasWall(pos) || st == Board.UNKNOWN) {
			board.changeState(pos, st);
		}
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
		if (direction < 0)
			return;
		SideAddress side = SideAddress.get(pos0, direction);
		if (currentState == Board.OUTER) {
			if (board.getState(side) == st) {
				currentState = Board.UNKNOWN;
			} else {
				currentState = st;
			}
		}
		for (Address p = pos0; !p.equals(pos1); p = p.nextCell(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				if (!board.hasWall(side) || currentState == Board.UNKNOWN)
					board.changeState(side, currentState);
		}
	}

	/*
	 * �u�X�����[���v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, num);
			if (num >= 0) {
				board.eraseLinesAround(pos);
			}
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isWall(posS)) {
					board.changeNumber(posS, Board.UNDECIDED_NUMBER);
					if (num >= 0) {
						board.eraseLinesAround(pos);
					}
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isWall(posS))
					board.changeNumber(posS, Board.BLANK);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			int number = board.getNumber(pos);
			if (number == Board.GATE_HORIZ) {
				currentGate = Board.GATE_VERT;
			} else if (number == Board.GATE_VERT) {
				currentGate = Board.GATE_HORIZ;
			}
			board.changeNumber(pos, currentGate);
		}
	}

	protected void starEntered(Address pos) {
	}

	protected void plusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.GOAL);
		}
	}
}
