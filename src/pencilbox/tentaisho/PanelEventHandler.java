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
		return board.isOnStar(pos);
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
		Area area = board.getArea(pos);
		if (area == null) {
			area = new Area();
			board.addCellToAreaA(pos, area);
		}
		setDraggingArea(area);
	}

	protected void leftDragged(Address pos) {
		Area draggingArea = getDraggingArea();
		if (draggingArea == null)
			return;
		Area oldArea = board.getArea(pos);
		if (oldArea != null && oldArea != draggingArea) {
			board.removeCellFromAreaA(pos, oldArea);
			board.addCellToAreaA(pos, draggingArea);
		} else if (oldArea != null && oldArea == draggingArea) {
		} else if (oldArea == null) {
			board.addCellToAreaA(pos, draggingArea);
		}
	}

	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos);
		if (oldArea != null) {
			board.removeCellFromAreaA(pos, oldArea);
		}
	}

	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}

	protected void leftReleased(Address dragEnd) {
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
	protected void numberEntered(Address pos, int n) {
		if (isProblemEditMode())
			if (n == Board.BLACKSTAR || n == Board.WHITESTAR)
				board.setStar(pos, n);
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode())
			board.setStar(pos, Board.NOSTAR);
	}
}
