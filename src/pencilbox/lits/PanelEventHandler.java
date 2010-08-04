package pencilbox.lits;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�k�h�s�r�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int dragState = 0;
	private int currentState = Board.UNKNOWN;

	private static final int INIT = 0;           // �������
	private static final int PRESS_NEW = 1;      // �V�̈�쐬
	private static final int PRESS_EXISTING = 2; // �����̈�I��
	private static final int DRAG_ADD = 3;       // �̈�g�呀�� 
	private static final int DRAG_REMOVE = 4;   // �̈�k������ 

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
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
	 * �u�k�h�s�r�v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Area area = board.getArea(pos);
			if (area == null) {
				area = new Area();
				board.addCellToArea(pos, area);
				dragState = PRESS_NEW;
			} else {
				dragState = PRESS_EXISTING;
			}
			setDraggingArea(area);
		} else {
			toggleState(pos, Board.BLACK);
			currentState = board.getState(pos);
		}
	}

	protected void leftDragged(Address oldPos, Address pos) {
		if (isProblemEditMode()) {
			Area draggingArea = getDraggingArea();
			if (draggingArea == null)
				return;
			Area oldArea = board.getArea(pos);
			if (dragState == PRESS_NEW || dragState == PRESS_EXISTING) {
				if (oldArea == null || oldArea != draggingArea) {
					dragState = DRAG_ADD; // �̈�g�呀��
				} else {
					dragState = DRAG_REMOVE; // �̈�k������
				}
			}
			if (dragState == DRAG_ADD) {
				if (oldArea != null && oldArea != draggingArea) {
					board.removeCellFromArea(pos, oldArea);
					board.addCellToArea(pos, draggingArea);
				} else if (oldArea != null && oldArea == draggingArea) {
				} else if (oldArea == null) {
					board.addCellToArea(pos, draggingArea);
				}
			} else if (dragState == DRAG_REMOVE) {
				if (!isOn(oldPos))
					return;
				Area oldoldArea = board.getArea(oldPos);
				if (oldoldArea!= null) {
					board.removeCellFromArea(oldPos, oldoldArea);
				}
			}
		} else {
			sweepState(pos);
		}
	}

	protected void leftReleased(Address pos) {
		if (isProblemEditMode()) {
			if (dragState == PRESS_EXISTING) {
				board.removeCellFromArea(pos, board.getArea(pos));
			}
			setDraggingArea(null);
			dragState = INIT;
		}
	}

	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
//			Area oldArea = board.getArea(pos);
//			if (oldArea != null) {
//				board.removeCellFromArea(pos, oldArea);
//			}
		} else {
			toggleState(pos, Board.WHITE);
			currentState = board.getState(pos);
		}
	}

	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
//			rightPressed(pos);
		} else {
			sweepState(pos);
		}
	}

	/**
	 * �}�X�̏�Ԃ� ���� �� st �ƕύX����
	 * @param pos �}�X���W
	 * @param st �؂�ւ�����
	 */
	private void toggleState(Address pos, int st) {
		if (st == board.getState(pos))
			st = Board.UNKNOWN;
		board.changeState(pos, st);
	}
	
	private void sweepState(Address pos) {
		int st = board.getState(pos);
		if (st == currentState)
			return;
		board.changeState(pos, currentState);
	}

	/*
	 * �u�k�h�s�r�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
		} else {
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
		} else {
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
		} else {
		}
	}
}
