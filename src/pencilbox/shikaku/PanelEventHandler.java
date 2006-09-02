package pencilbox.shikaku;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�l�p�ɐ؂�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

//	private Square draggingArea; // �h���b�O���č��܂��ɕ`�����Ƃ��Ă���l�p

	/**
	 * �p�l���𐶐�����
	 */
	public PanelEventHandler() {
	}
	
	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	/*
	 * �u�l�p�ɐ؂�v�}�E�X����
	 */
	private Address dragStart = new Address(Address.NOWEHER);

	protected void leftPressed(Address pos) {
		dragStart.set(pos);
		setDraggingArea(new Square(dragStart.r(), dragStart.c(), pos.r(), pos.c()));
	}

	protected void rightPressed(Address dragEnd) {
		board.removeSquareIncluding(dragEnd);
	}
	
	protected void leftDragged(Address dragEnd) {
		if (getDraggingArea() == null)
			return;
		getDraggingArea().set(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c());
	}
	
	protected void leftDragFixed(Address dragEnd) {
		if (getDraggingArea() == null)
			return;
		setDraggingArea(null);
		board.addSquareSpanning(dragStart, dragEnd);
		dragStart.setNowhere();
	}
	
	protected void dragFailed() {
		setDraggingArea(null);
		dragStart.setNowhere();
	}

	/*
	 * �u�l�p�ɐ؂�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			if (num > 0)
			board.setNumber(pos.r(), pos.c(), num);
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), 0);
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}

	/**
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Square draggingArea) {
		((Panel)getPanel()).setDraggingArea(draggingArea);
	}

	/**
	 * @return the draggingArea
	 */
	Square getDraggingArea() {
		return ((Panel)getPanel()).getDraggingArea();
	}
}
