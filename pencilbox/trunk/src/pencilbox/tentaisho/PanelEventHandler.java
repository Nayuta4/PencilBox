package pencilbox.tentaisho;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.CellCursor;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�V�̃V���[�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private Area draggingArea;

	/**
	 * 
	 */
	public PanelEventHandler() {
		setMaxInputNumber(2);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
	}

	public CellCursor createCursor() {
		return new TentaishoCursor(this);
	}

	/*
	 * �u�V�̃V���[�v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (draggingArea == null) {
			//  ������ if ����L���ɂ���΁C������Area���������L���邱�Ƃ��ł���
			//  �������Cundo �Ɛ������ǂ����邩�����				
//			if (oldArea != null)
//				draggingArea = oldArea;
//			else
			draggingArea = new Area();
		}
		if (oldArea != null && oldArea != draggingArea) {
			board.removeAreaA(oldArea);
		}
		board.setArea(pos.r(), pos.c(), draggingArea);
		draggingArea.add(pos);
	}
	
	protected void rightPressed(Address pos) {
		Area oldArea = board.getArea(pos.r(), pos.c());
		if (oldArea != null) {
			board.removeAreaA(oldArea);
		}
	}
	
	protected void leftDragged(Address pos) {
		leftPressed(pos);			
	}
	
	protected void rightDragged(Address pos) {
		rightPressed(pos);			
	}
	
	protected void leftDragFixed(Address dragEnd) {
		if (draggingArea == null)
			return;
		board.addAreaA(draggingArea);
		draggingArea = null;
	}
	
	protected void rightDragFixed(Address dragStart, Address dragEnd) {
		//			board.removeSquare(dragStart.r, dragStart.c, dragEnd.r, dragEnd.c);
		draggingArea = null;
	}

	protected void dragFailed() {
		if (draggingArea == null)
			return;
		board.addAreaA(draggingArea);
		draggingArea = null;
	}
	/*
	 * �}�E�X�ł̓J�[�\���ړ����Ȃ��i�b��j
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
