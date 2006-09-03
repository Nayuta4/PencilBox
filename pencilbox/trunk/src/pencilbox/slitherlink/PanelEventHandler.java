package pencilbox.slitherlink;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�X���U�[�����N�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

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

	/**
	 * �X���U�[�����N��p�J�[�\������
	 */
	public CellCursor createCursor() {
		return new SlitherLinkCursor(this);
	}

	/*
	 * �u�X���U�[�����N�v�}�E�X����2
	 */
	protected void leftClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.LINE);
	}
	protected void rightClickedEdge(SideAddress pos) {
		board.toggleState(pos.d(), pos.r(), pos.c(), Board.NOLINE);
	}

	/*
	 * �u�X���U�[�����N�v�}�E�X����
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
	 * �}�E�X�ł̓J�[�\���ړ����Ȃ�
	 */
	protected void moveCursor(Address pos) {
	}
	/*
	 * �u�X���U�[�����N�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), num);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.NONUMBER);
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode())
			board.setNumber(pos.r(), pos.c(), Board.UNDECIDED_NUMBER);
	}
}