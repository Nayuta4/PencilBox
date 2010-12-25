package pencilbox.shakashaka;

import java.util.Arrays;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�V���J�V���J�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;
	
	/**
	 * Panel �𐶐�����
	 */
	public PanelEventHandler() {
		setMaxInputNumber(5);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * �u�V���J�V���J�v�}�E�X����
	 */
	private int currentState = Board.UNKNOWN;

	protected void leftPressed(Address pos) {
		toggleState(pos, +1);
	}

	protected void rightPressed(Address pos) {
		toggleState(pos, -1);
	}

	protected void leftDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	private static int[] cycle = {Board.UNKNOWN, Board.LTUP, Board.LTDN, Board.RTDN, Board.RTUP, Board.WHITE};
	/**
	 * �}�X�̏�Ԃ� �O�p�`�����Ԃɐ؂�ւ���
	 * @param pos �}�X���W
	 * @param b �؂�ւ��鏇��
	 */
	private void toggleState(Address pos, int b) {
		if (Board.isNumber(board.getNumber(pos)))
			return;
		int st0 = board.getState(pos);
		int i = Arrays.binarySearch(cycle, st0);
		int st = cycle[(i + b + 6) % 6];
		currentState = st;
		board.changeState(pos, st);
	}

	private void sweepState(Address pos) {
		if (Board.isNumber(board.getNumber(pos)))
			return;
		if (currentState == board.getState(pos))
			return;
		if (currentState == Board.LTUP || currentState == Board.RTDN) {
			if (board.getState(Address.nextCell(pos, Direction.LTDN)) == currentState || board.getState(Address.nextCell(pos, Direction.RTUP)) == currentState) {
			} else {
				return;
			}
		} else if (currentState == Board.LTDN || currentState == Board.RTUP) {
			if (board.getState(Address.nextCell(pos, Direction.LTUP)) == currentState || board.getState(Address.nextCell(pos, Direction.RTDN)) == currentState) {
			} else {
				return;
			}
		}
		board.changeState(pos, currentState);
	}

	/*
	 * �u�V���J�V���J�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		keyEntered(pos, num);
	}
	protected void spaceEntered(Address pos) {
		keyEntered(pos, Board.BLANK);
	}
	protected void minusEntered(Address pos) {
		keyEntered(pos, Board.NONUMBER_WALL);
	}
	protected void plusEntered(Address pos) {
	}
	protected void starEntered(Address pos) {
		keyEntered(pos, Board.WHITE);
	}
	
	protected void keyEntered(Address pos, int key) {
		int n = 0;
		if (isProblemEditMode()) {
			if (key >= 0 && key <= 4) {
				n = key;
			} else if (key == 5) {
				n = Board.NONUMBER_WALL;
			} else if (key == Board.NONUMBER_WALL) {
				n = Board.NONUMBER_WALL;
			} else if (key == Board.BLANK) {
				n = Board.BLANK;
			}
			board.changeNumber(pos, n);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (n >= 0)
					n = Board.NONUMBER_WALL;
				if (n == Board.BLANK && Board.isNumber(board.getNumber(posS)))
					board.changeNumber(posS, Board.BLANK);
				else if (n == Board.NONUMBER_WALL && !Board.isNumber(board.getNumber(posS)))
					board.changeNumber(posS, Board.NONUMBER_WALL);
			}
		} else {
		}
	}
}
