package pencilbox.shakashaka;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * 「シャカシャカ」マウス／キー操作処理クラス
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	public PanelEventHandler() {
		setMaxInputNumber(5);
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * 「シャカシャカ」マウス操作
	 */
	private int currentState = Board.UNKNOWN;

	public void mousePressed(MouseEvent e) {
		Address newPos = pointToAddress(e);
		int corner;
		if (!isOn(newPos))
			return;
		int yy = (e.getY() - getPanel().getOffsety()) % getPanel().getCellSize();
		int xx = (e.getX() - getPanel().getOffsetx()) % getPanel().getCellSize();
		if (xx < getPanel().getHalfCellSize())
			if (yy < getPanel().getHalfCellSize())
				corner = Board.LTUP;
			else
				corner = Board.LTDN;
		else
			if (yy < getPanel().getHalfCellSize())
				corner = Board.RTUP;
			else
				corner = Board.RTDN;
		int button = getMouseButton(e);
		if (button == 1) {
			leftPressed(newPos, corner);
		} else if (button == 3) {
			rightPressed(newPos);
		}
		moveCursor(newPos);
		repaint();
	}

	protected void leftPressed(Address pos, int corner) {
		toggleStateCorner(pos, corner);
	}

	protected void rightPressed(Address pos) {
		toggleStateCorner(pos, Board.WHITE);
	}

	protected void toggleStateCorner(Address pos, int st) {
		if (Board.isNumber(board.getNumber(pos)))
			return;
		int st0 = board.getState(pos);
		if (st == st0) {
			board.changeState(pos, Board.UNKNOWN);
			currentState = Board.UNKNOWN;
		} else {
			board.changeState(pos, st);
			currentState = st;
		}
	}

	protected void leftDragged(Address pos) {
		sweepState(pos);
	}

	protected void rightDragged(Address pos) {
		sweepState(pos);
	}

	private void sweepState(Address pos) {
		if (Board.isNumber(board.getNumber(pos)))
			return;
		if (currentState == board.getState(pos))
			return;
		if (currentState == Board.LTUP || currentState == Board.RTDN) {
			if (board.getState(Address.nextCell(pos, Direction.LTDN)) == currentState || board.getState(Address.nextCell(pos, Direction.RTUP)) == currentState) {
			} else {
				return;
			}
		} else if (currentState == Board.LTDN || currentState == Board.RTUP) {
			if (board.getState(Address.nextCell(pos, Direction.LTUP)) == currentState || board.getState(Address.nextCell(pos, Direction.RTDN)) == currentState) {
			} else {
				return;
			}
		}
		board.changeState(pos, currentState);
	}

	/*
	 * 「シャカシャカ」キー操作
	 */
	protected void numberEntered(Address pos, int num) {
		keyEntered(pos, num);
	}
	protected void spaceEntered(Address pos) {
		keyEntered(pos, Board.BLANK);
	}
	protected void minusEntered(Address pos) {
		keyEntered(pos, Board.NONUMBER_WALL);
	}
	protected void plusEntered(Address pos) {
	}
	protected void starEntered(Address pos) {
		keyEntered(pos, Board.WHITE);
	}
	
	protected void keyEntered(Address pos, int key) {
		int n = 0;
		if (isProblemEditMode()) {
			if (key >= 0 && key <= 4) {
				n = key;
			} else if (key == 5) {
				n = Board.NONUMBER_WALL;
			} else if (key == Board.NONUMBER_WALL) {
				n = Board.NONUMBER_WALL;
			} else if (key == Board.BLANK) {
				n = Board.BLANK;
			}
			board.changeNumber(pos, n);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (n >= 0)
					n = Board.NONUMBER_WALL;
				if (n == Board.BLANK && Board.isNumber(board.getNumber(posS)))
					board.changeNumber(posS, Board.BLANK);
				else if (n == Board.NONUMBER_WALL && !Board.isNumber(board.getNumber(posS)))
					board.changeNumber(posS, Board.NONUMBER_WALL);
			}
		} else {
		}
	}
}
