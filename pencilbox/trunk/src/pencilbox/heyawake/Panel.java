package pencilbox.heyawake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandler;


/**
 *  �u�ւ�킯�v�p�l���N���X
 */
public class Panel extends PanelEventHandler {

	private int maxNumber = 9;  // �L�[���͉\����9�܂łƂ���

	private Board board;

	private boolean warnWrongWall = false;
	private boolean showContinuousRoom = false;

	private Color paintColor = Color.BLUE;
	private Color circleColor = Color.MAGENTA;
	private Color noRoomColor = new Color(0xCCCCCC);
	private Color roomBorderColor = Color.BLACK;
	private Color continuousRoomColor = new Color(0x800000);
	private Color errorColor = Color.RED;
	private Color showContinuousWhiteColor = Color.RED;

	/**
	 * 
	 */
	public Panel() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(maxNumber);   // �b��I
	}

	/**
	 * @param showContinuousRoom The showContinuousRoom to set.
	 */
	public void setShowContinuousRoom(boolean showContinuousRoom) {
		this.showContinuousRoom = showContinuousRoom;
	}

	/**
	 * @param warnWrongWall The warnWrongWall to set.
	 */
	public void setWarnWrongWall(boolean warnWrongWall) {
		this.warnWrongWall = warnWrongWall;
	}

	/**
	 * @return Returns the circleColor.
	 */
	public Color getCircleColor() {
		return circleColor;
	}

	/**
	 * @param circleColor The circleColor to set.
	 */
	public void setCircleColor(Color circleColor) {
		this.circleColor = circleColor;
	}

	/**
	 * @return Returns the paintColor.
	 */
	public Color getPaintColor() {
		return paintColor;
	}

	/**
	 * @param paintColor The paintColor to set.
	 */
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}

	public void drawPanel(Graphics g) {
		paintBackground(g);
		drawIndex(g);
		drawBoard(g);
		drawGrid(g);
		drawBorder(g);
		if (isProblemEditMode())
			drawDragging(g);
		if (getCellCursor() != null)
			drawCursor(g);
	}
	/**
	 * �Ֆʂ�`�悷��
	 * �ł���΁C����}�X�ɐ������\�������悤�ɂ��������E�E�E
	 * @param g
	 */
	void drawBoard(Graphics g) {
		
		int st;
		Square square;
		g.setFont(getNumberFont());

		if (showContinuousRoom) {
			for (int r = 0; r < board.rows(); r++) {
				for (int c = 0; c < board.cols(); c++) {
					g.setColor(continuousRoomColor); 
					if (board.contWH[r][c] >= 3) {
						g.setColor(showContinuousWhiteColor);
					}
					if (board.contH[r][c] >= 3) {
						placeMidline(g,r,c,Direction.HORIZ);
					}
					g.setColor(continuousRoomColor); 
					if (board.contWV[r][c] >= 3) {
						g.setColor(showContinuousWhiteColor);
					}
					if (board.contV[r][c] >= 3) {
						placeMidline(g,r,c,Direction.VERT);
					}
				}
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				st = board.getState(r, c);
				if (board.getSquare(r,c)  == null) {
					g.setColor(noRoomColor);
					paintCell(g, r, c);
				}
				if (st == Board.BLACK) {
//					g.setColor(paintColor);
					g.setColor(paintColor);
					if (warnWrongWall && (board.isBlock(r,c) || board.chain[r][c]==-1)) {
						g.setColor(errorColor);
					}
					paintCell(g, r, c);
				}
			}
		}
		for (int r = 0; r < board.rows(); r++) {
			for (int c = 0; c < board.cols(); c++) {
				st = board.getState(r, c);
				if (st == Board.WHITE) {
//					g.setColor(circleColor);
					g.setColor(circleColor);
//					if (board.hcontw[r][c] >= 3) {
//						g.setColor(errorCircleColor);
//					}
//					if (board.vcontw[r][c] >= 3) {
//						g.setColor(errorCircleColor);
//					}
					placeCircle(g, r, c);
				}
			}
		}

		for (Iterator itr = board.getSquareListIterator(); itr.hasNext();) {
			g.setColor(roomBorderColor);
			square = (Square) itr.next();
			placeSquare(g, square.r0, square.c0, square.r1, square.c1);
			g.setColor(getNumberColor());
			if (square.getNumber() >= 0) {
				if (warnWrongWall && square.getNumber() < square.getNBlack()) {
					g.setColor(Color.WHITE);
					placeFilledCircle(g, square.r0, square.c0);
					g.setColor(errorColor);
			}
				placeNumber(g, square.r0, square.c0, square.getNumber());
			}
		}
	}
	/**
	 *  �h���b�O���̎l�p��`�悷��
	 * @param g
	 */
	void drawDragging(Graphics g) {
		Square square = draggingSquare;
		if (square == null)
			return;
		g.setColor(roomBorderColor);
		placeSquare(g, square.r0, square.c0, square.r1, square.c1);
	}

	/*
	 * �u�ւ�킯�v�}�E�X����
	 * �𓚓��͗p
	 * ���v���X�F����̍��}�X
	 * �E�v���X�F����̔��}�X
	 * �E�h���b�O�F�͂��߂Ƀ{�^�����������}�X�Ƃ��̏�Ԃɂ��킹��
	 */
	/*
	 * �u�ւ�킯�v�}�E�X����
	 * �����͗p
	 * ���h���b�O�F�h���b�O�n�_�ƏI�_���Q�̒��_�Ƃ��钷���`��`��
	 * �E�v���X�C�h���b�O�F���̃}�X���܂ޒ����`����������
	 */

	private Square draggingSquare;
	private Address dragStart = new Address(-1, -1);
	private int currentState = Board.UNKNOWN;
	protected void leftPressed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			dragStart.set(dragEnd);
			draggingSquare =
				new Square(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c());
		} else {
			board.toggleState(pos.r(), pos.c(), Board.BLACK);
		}
	}
	protected void rightPressed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			//			dragStart.set(dragEnd);
			board.removeSquareIncluding(dragEnd.r(), dragEnd.c());
		} else {
			board.toggleState(pos.r(), pos.c(), Board.WHITE);
			currentState = board.getState(pos.r(), pos.c());
		}
	}
	protected void leftDragged(Address pos) {
		if (isProblemEditMode()) {
			if (draggingSquare == null)
				return;
			draggingSquare.set(dragStart.r(), dragStart.c(), pos.r(), pos.c());
		}
	}
	protected void leftDragFixed(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			if (draggingSquare == null)
				return;
			draggingSquare = null;
			board.addSquareSpanning(dragStart.r(), dragStart.c(), dragEnd.r(), dragEnd.c());
			dragStart.setNowhere();
		}
	}
	protected void rightDragged(Address pos) {
		if (isProblemEditMode()) {
			Address dragEnd = pos;
			board.removeSquareIncluding(dragEnd.r(), dragEnd.c());
		} else {
			int st = board.getState(pos.r(), pos.c());
			if (st == currentState)
				return;
			board.changeStateA(pos.r(), pos.c(), currentState);
		}
	}
	protected void dragFailed() {
		draggingSquare = null;
		dragStart.setNowhere();
	}
	/*
	 * �u�ւ�킯�v�L�[����
	 * 
	 * �����̓��[�h�̂Ƃ��̂݁C0�ȏ�̐������͂ɂ�蕔���̐����ݒ�
	 */
	protected void numberEntered(Address pos, int num) {
		if (!isProblemEditMode())
			return;
		Square square = board.getSquare(pos.r(), pos.c());
		if (square != null)
			square.setNumber(num);
	}
	protected void spaceEntered(Address pos) {
		if (!isProblemEditMode())
			return;
		Square square = board.getSquare(pos.r(), pos.c());
		if (square != null)
			square.setNumber(Square.ANY);
	}
}

