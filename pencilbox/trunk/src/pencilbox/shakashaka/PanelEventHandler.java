package pencilbox.shakashaka;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�V���J�V���J�v�}�E�X�^�L�[���쏈���N���X
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
	 * �u�V���J�V���J�v�}�E�X����
	 */
	private int currentState = Board.UNKNOWN;
	private Address currentStatePos = null;
	private int oldCorner = 0;

	public void mousePressed(MouseEvent e) {
		Address newPos = pointToAddress(e);
		if (!isOn(newPos)) {
			currentState = Board.UNKNOWN;
			return;
		}
		int button = getMouseButton(e);
		if (button == 1) {
			leftPressed(newPos, getCorner(e));
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
			currentStatePos = pos;
		} else {
			board.changeState(pos, st);
			currentState = st;
			currentStatePos = pos;
		}
	}

	public void mouseDragged(MouseEvent e) {
		mouseDragged3(e);
		Address newPos = pointToAddress(e);
		if (!isOn(newPos)) {
			oldPos = Address.nowhere();
			return;
		}

		int button = getMouseButton(e);
		if (button == 1 || button == 3) {
			if (Board.LTUP <= currentState && currentState <= Board.RTUP) {
				int corner = getCorner(e);

				if (newPos.equals(oldPos)) {
					// ���̏ꍇ�̂݁A�����}�X���Ɏ~�܂�C�x���g�𖳎����Ȃ�
					if (corner == oldCorner)
						// �ł������p�̗̈悩��o�Ȃ��ꍇ�ɂ̓C�x���g�𖳎�����
						return;

					sweepState(newPos, corner);
					oldCorner = corner;
					// ���̏ꍇ�̂݁A�����}�X���Ȃ̂Ō��݈ʒu���X�V���Ȃ�
				} else {
					sweepState(newPos, corner);
					oldCorner = corner;

					moveCursor(newPos);
					oldPos = newPos; // ���݈ʒu���X�V
				}
			} else {
				if (newPos.equals(oldPos))
					return; // �����}�X���Ɏ~�܂�C�x���g�͖���

				sweepState(newPos, currentState);

				moveCursor(newPos);
				oldPos = newPos; // ���݈ʒu���X�V
			}
		} else {
			if (newPos.equals(oldPos))
				return; // �����}�X���Ɏ~�܂�C�x���g�͖���

			moveCursor(newPos);
			oldPos = newPos; // ���݈ʒu���X�V
		}

		repaint();
	}

	private int getCorner(MouseEvent e) {
		int corner;
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
		return corner;
	}

	private void sweepState(Address pos, int corner) {
		if (Board.isNumber(board.getNumber(pos)))
			return;

		if (Board.LTUP <= currentState && currentState <= Board.RTUP) {
			int dX = currentStatePos.c() - pos.c();
			int dY = currentStatePos.r() - pos.r();
			int cX = 1 - (corner & 2);
			int cY = 1 - ((corner + 1) & 0x2);

			if ((corner == currentState && ((dX == cX && dY + cY == 0) || (dX + cX == 0 && dY == cY))) ||
					(dX == 0 && dY == cY && corner == (currentState ^ 0x1)) ||
					(dY == 0 && dX == cX && corner == (currentState ^ 0x3))) {
				currentStatePos = pos;
				currentState = corner;
			} else {
				return;
			}
		}

		board.changeState(pos, currentState);
	}

	/*
	 * �u�V���J�V���J�v�L�[����
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
