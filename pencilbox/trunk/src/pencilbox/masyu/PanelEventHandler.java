package pencilbox.masyu;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�܂���v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(2);
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �܂��� �ӂɑ΂���}�E�X����
	 */
	protected void leftClickedEdge(SideAddress pos) {
		toggleState(pos, Board.LINE);
	}

	protected void rightClickedEdge(SideAddress pos) {
		toggleState(pos, Board.NOLINE);
	}
	/*
	 * �܂��� �}�X�ɑ΂���}�E�X����
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.UNKNOWN);
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
				board.changeStateA(side, st);
		}
	}

	/*
	 * �u�܂���v�L�[���� 
	 */
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode()) {
			if(n == 1 || n == 2) {
				board.setNumber(pos, n);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.setNumber(posS, Board.GRAY_PEARL);
				}
			}
		}
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.NO_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.NO_PEARL);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, Board.GRAY_PEARL);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.setNumber(posS, Board.GRAY_PEARL);
			}
		}
	}
}
