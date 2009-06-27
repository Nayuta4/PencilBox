package pencilbox.slitherlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�X���U�[�����N�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int currentState = Board.OUTER; // �h���b�O���̕ӂ̏�Ԃ�\��

	/**
	 * 
	 */
	public PanelEventHandler() {
		super();
		setMaxInputNumber(3);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}
	
	public boolean isCursorOnBoard(Address pos) {
		return board.isNumberOn(pos);
	}

	/*
	 * �u�X���U�[�����N�v�}�E�X����2
	 */
	protected void leftPresseddEdge(SideAddress pos) {
//		toggleState(pos, Board.LINE);
	}

	protected void rightPressedEdge(SideAddress pos) {
		toggleState(pos, Board.NOLINE);
	}

	/*
	 * �u�X���U�[�����N�v�}�E�X����
	 */
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
		if (st == board.getState(pos))
			st = Board.UNKNOWN;
		board.changeStateA(pos, st);
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
		for (Address p = pos0; !p.equals(pos1); p.move(direction)) {
			side = SideAddress.get(p, direction);
			if (board.getState(side) != currentState)
				board.changeStateA(side, currentState);
		}
	}

	/*
	 * �}�E�X�ł̓J�[�\���ړ����Ȃ�
	 */
	protected void moveCursor(Address pos) {
	}

	/*
	 * �u�X���U�[�����N�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.setNumber(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.NONUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.NONUMBER);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	/**
	 * SL�p�_�Ώ̈ʒu�̍��W���擾����B
	 * @param pos�@�����W
	 * @return pos�Ɠ_�Ώ̂Ȉʒu�̍��W
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-2-pos.r(), board.cols()-2-pos.c());
	}

}
