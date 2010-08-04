package pencilbox.hakyukoka;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�g�y���ʁv�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int dragState = 0;

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
		setMaxInputNumber(9);   // �b��I
	}

	protected int getMaxInputNumber() {
		Address pos = getCellCursor().getPosition();
		Area area = board.getArea(pos);
		if (area != null) {
			int n = area.size();
			if (n > 9)
				return n;
			else
				return 9;
		}
		return 9;
	}

	private void setSelectedNumber(int n) {
		((Panel) getPanel()).setSelectedNumber(n);
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
	 * �u�g�y���ʁv�}�E�X����
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
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos)) {
					int n = board.getState(pos);
					if (n >= getMaxInputNumber()) 
						board.changeAnswerNumber(pos, 0);
					else if (n >= 0)
						board.changeAnswerNumber(pos, n + 1);
				}
			}
			setSelectedNumber(board.getNumberOrState(pos));
		}
	}

	protected void leftDragged(Address oldPos, Address pos) {
		if (isProblemEditMode()) {
//			moveCursor(pos);
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
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos)) {
					int n = board.getState(pos);
					if (n > 0) 
						board.changeAnswerNumber(pos, n - 1);
				}
			}
			setSelectedNumber(board.getNumberOrState(pos));
		}
	}

	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			rightPressed(pos);
		}
	}

	/*
	 * �u�g�y���ʁv�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isStable(posS)) {
					board.changeFixedNumber(posS, Board.UNDETERMINED);
				}
			}
		} else if (isCursorOn()){
			if (num >= 0) {
				if (!board.isStable(pos)) {
					board.changeAnswerNumber(pos, num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isStable(posS)) {
					board.changeFixedNumber(posS, Board.BLANK);
				}
			}
		} else if (isCursorOn()){
			if (!board.isStable(pos)) {
				board.changeAnswerNumber(pos, Board.UNKNOWN);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeFixedNumber(pos, Board.UNDETERMINED);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isStable(posS)) {
					board.changeFixedNumber(posS, Board.UNDETERMINED);
				}
			}
		}
	}
}
