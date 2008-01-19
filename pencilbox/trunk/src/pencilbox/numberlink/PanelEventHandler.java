package pencilbox.numberlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�i���o�[�����N�v�}�E�X�^�L�[���쏈���N���X
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
	}
	
	/*
	 * �u�i���o�[�����N�v�}�E�X����
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.LINE);
	}

	protected void rightDragged(Address dragStart, Address dragEnd) {
		changeLineState(dragStart, dragEnd, Board.UNKNOWN);
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
	 * �N���b�N�����}�X�̐����n�C���C�g����� �����P�x�N���b�N����ƃn�C���C�g������
	 */
	protected void leftClicked(Address pos) {

		Link link = board.getLink(pos);
		int newNumber = 0;

		if (board.isNumber(pos))
			newNumber = board.getNumber(pos);
		else {
			if(link!=null)
				newNumber = link.getNumber();
		}

		if (newNumber == getSelectedNumber() && getSelectedLink() == link) {
			setSelectedLink(null);
			setSelectedNumber(0);
		} else {
			setSelectedLink(link);
			setSelectedNumber(newNumber);
		}
	}

	/*
	 * �u�i���o�[�����N�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.setNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.setNumber(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.setNumber(posS, Board.BLANK);
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

	private Link getSelectedLink() {
		return ((Panel) getPanel()).getSelectedLink();
	}

	private void setSelectedLink(Link l) {
		((Panel) getPanel()).setSelectedLink(l);
	}
	
	private int getSelectedNumber() {
		return ((Panel) getPanel()).getSelectedNumber();
	}
	
	private void setSelectedNumber(int n) {
		((Panel) getPanel()).setSelectedNumber(n);
	}

}
