package pencilbox.numberlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�i���o�[�����N�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;
//
//	private Link selectedLink = null;
//	private int selectedNumber = 0;  // �I������Ă��Ȃ��Ƃ��� 0

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
	 * �ӂɑ΂��đ��������
	 */
//	protected void leftClicked(int dir, Address pos) {
//		board.toggleState(dir, pos.r, pos.c, Board.LINE);
//	}
//	protected void rightClickedEdge(int dir, Address pos) {
//		board.toggleState(dir, pos.r, pos.c, Board.NOLINE);
//	}

	/*
	 * �u�i���o�[�����N�v�}�E�X����
	 */
	protected void leftDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r() == dragEnd.r() || dragStart.c() == dragEnd.c()) {
			board.determineInlineState(dragStart, dragEnd, Board.LINE);
		}
	}
	protected void rightDragged(Address dragStart, Address dragEnd) {
		if (dragStart.r() == dragEnd.r() || dragStart.c() == dragEnd.c()) {
			board.determineInlineState(dragStart, dragEnd, Board.UNKNOWN);
		}
	}

	/*
	 * �N���b�N�����}�X�̐����n�C���C�g����� �����P�x�N���b�N����ƃn�C���C�g������
	 */
	protected void leftClicked(Address pos) {

		Link link = board.getLink(pos.r(), pos.c());
		int newNumber = 0;

		if (board.isNumber(pos.r(), pos.c()))
			newNumber = board.getNumber(pos.r(), pos.c());
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
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), num);
	}
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
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
