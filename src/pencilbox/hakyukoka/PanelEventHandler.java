package pencilbox.hakyukoka;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�g�y���ʁv�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {
	
	private Board board;

	private Area draggingArea;
	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(9);   // �b��I
	}
	
	private void setSelectedNumber(int n) {
		((Panel) getPanel()).setSelectedNumber(n);
	}

	/*
	 * �u�g�y���ʁv�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (draggingArea == null) {
				//	������ if ����L���ɂ���΁C������Area���������L���邱�Ƃ��ł���
				//	�������Cundo �Ɛ������ǂ����邩�����				
				//				if (oldArea != null)
				//					draggingArea = oldArea;
				//				else
				draggingArea = new Area();
			}
			if (oldArea != null && oldArea != draggingArea) {
				board.removeArea(oldArea);
			}
			board.setArea(pos.r(), pos.c(), draggingArea);
			draggingArea.add(pos);
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.increaseNumber(pos.r(), pos.c());
			}
			setSelectedNumber(board.getNumber(pos.r(), pos.c()));
		}
	}
	
	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Area oldArea = board.getArea(pos.r(), pos.c());
			if (oldArea != null) {
				board.removeArea(oldArea);
			}
		} else {
			if (!isCursorOn() || getCellCursor().isAt(pos)) {
				if (!board.isStable(pos.r(), pos.c()))
					board.decreaseNumber(pos.r(), pos.c());
			}
			setSelectedNumber(board.getNumber(pos.r(), pos.c()));
		}
	}
	
	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
//			moveCursor(pos);
			leftPressed(pos);
		}
	}
	
	protected void rightDragged(Address pos) {
		if (isProblemEditMode())
			rightPressed(pos);
	}
	
	protected void leftDragFixed(Address dragEnd) {
		if (draggingArea == null)
			return;
		board.addArea(draggingArea);
		draggingArea = null;
	}
	
	protected void rightDragFixed(Address dragStart, Address dragEnd) {
		//			board.removeSquare(dragStart.r, dragStart.c, dragEnd.r, dragEnd.c);
		draggingArea = null;
	}
	
	protected void dragFailed() {
		if (draggingArea == null)
			return;
		board.addArea(draggingArea);
		draggingArea = null;
	}
	//		protected boolean dragIneffective(Address oldPos, Address newPos) {
	//			if (newPos.isNextTo(oldPos)) return false; // �אڃ}�X�ȊO�̃C�x���g�͖���
	//			else return true;
	//		}
	//	}
	/*
	 * �u�g�y���ʁv�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos.r(), pos.c(), num);
				board.setState(pos.r(), pos.c(), Board.STABLE);
			}
		} else if (isCursorOn()){
			if (num > 0) {
				if (!board.isStable(pos.r(), pos.c())) {
					board.enterNumberA(pos.r(), pos.c(), num);
				}
			}
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), 0);
			board.setState(pos.r(), pos.c(), Board.UNSTABLE);
		} else if (isCursorOn()){
			if (!board.isStable(pos.r(), pos.c())) {
				board.enterNumberA(pos.r(), pos.c(), 0);
			}
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.setNumber(pos.r(), pos.c(), Board.UNKNOWN);
			board.setState(pos.r(), pos.c(), Board.STABLE);
		}
	}
}
