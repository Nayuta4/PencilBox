package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�V�̃V���[�v�}�E�X�^�L�[���쏈���N���X
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

	public boolean isCursorOnBoard(Address pos) {
		return board.isOnStar(pos.r(), pos.c());
	}

	/**
	 * @return the draggingArea
	 */
	Area getDraggingArea() {
		return ((Panel) getPanel()).getDraggingArea();
	}

	/**
	 * @param draggingArea the draggingArea to set
	 */
	void setDraggingArea(Area draggingArea) {
		((Panel) getPanel()).setDraggingArea(draggingArea);
	}

	/*
	 * �u�V�̃V���[�v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		Area area = board.getArea(pos.r(), pos.c());
		if (area == null) {
			area = new Area();
			board.addCellToAreaA(pos.r(), pos.c(), area);
		}
		setDraggingArea(area);
	}

	protected void leftDragged(Address pos) {
		Area draggingArea = getDraggingArea();
		if (draggingArea == null)
			return;
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (oldArea != null && oldArea != draggingArea) {
			board.removeCellFromAreaA(pos.r(), pos.c(), oldArea);
			board.addCellToAreaA(pos.r(), pos.c(), draggingArea);
		} else if (oldArea != null && oldArea == draggingArea) {
		} else if (oldArea == null) {
			board.addCellToAreaA(pos.r(), pos.c(), draggingArea);
		}
	}

	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (oldArea != null) {
			board.removeCellFromAreaA(pos.r(), pos.c(), oldArea);
		}
	}
	
	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}
	
	protected void leftDragFixed(Address dragEnd) {
		setDraggingArea(null);
	}
	
	protected void dragFailed() {
		setDraggingArea(null);
	}
	/*
	 * �}�E�X�ł̓J�[�\���ړ����Ȃ�
	 */
	protected void moveCursor(Address pos) {
	}

	/*
	 * �u�V�̃V���[�v�L�[����
	 */
	protected void numberEntered(Address p, int n) {
		if (isProblemEditMode())
			if (n == Board.BLACKSTAR || n == Board.WHITESTAR)
				board.setStar(p.r(), p.c(), n);
	}

	protected void spaceEntered(Address p) {
		if (isProblemEditMode())
			board.setStar(p.r(), p.c(), Board.NOSTAR);
	}
}
