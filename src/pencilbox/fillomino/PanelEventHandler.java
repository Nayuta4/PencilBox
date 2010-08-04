package pencilbox.fillomino;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;

/**
 * �u�t�B���I�~�m�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int state; // �h���b�O���̐�����\��
	private int currentState = -1; // �h���b�O���̕ӂ̏�Ԃ�\��
	private int dragState = 0; // �h���b�O���̕ӂ̏�Ԃ�\��
	private Address pos3 = Address.NOWHERE;
	private Address selectedPos = Address.NOWHERE;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * �u�t�B���I�~�m�v�}�E�X����
	 * 
	 * �}�X�Ń{�^���������Ƃ��̃}�X�̐������o���āC ���̌�h���b�O������ʉ߂����}�X�����̐����ɍ��킹��
	 * �}�X����ړ������Ƀ{�^���𗣂�����C������1�����₵�Ă����B
	 * ���̃}�X�Ɉړ�����Ɛ����̓��Z�b�g�����
	 */
	protected void leftPressed(Address pos) {
		state = board.getNumberOrState(pos);
		dragState = 1; // �h���b�O�J�n
	}

	protected void leftDragged(Address oldPos, Address pos) {
		if (!board.isStable(pos))
			board.changeAnswerNumber(pos, state);
		dragState = 2; // �h���b�O��
	}

	protected void leftReleased(Address pos) {
		if (dragState == 1 && isOn(pos)) {
			if (!board.isStable(pos)) {
				int n = board.getState(pos);
				if (n == 0 || selectedPos.equals(pos)) {
					board.changeAnswerNumber(pos, n + 1);
				} else {
					board.changeAnswerNumber(pos, 0);
				}
				selectedPos = pos;
			}
		}
		dragState = 0;
	}

	public void mouseMoved(MouseEvent e) {
		Address p = pointToAddress(e);
		if (! p.equals(selectedPos)) {
			selectedPos = Address.NOWHERE;
		}
	}

//	protected void rightPressed(Address pos) {
//		if (!board.isStable(pos))
//			board.changeAnswerNumber(pos, 0);
//	}

//	protected void rightDragged(Address oldPos, Address pos) {
//		if (!board.isStable(pos))
//			board.changeAnswerNumber(pos, 0);
//	}

	protected void rightPressed3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
//		System.out.println(sa.toString());
		this.pos3 = sa;
	}

	protected void rightDragged3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		if (pos3.equals(sa))
			return;
		if (pos3.r() != sa.r() && pos3.c() != sa.c())
			return;
		int dir = pos3.getDirectionTo(sa);
		Address sb = Address.nextCell(pos3, dir);
		SideAddress b = superAddress2SideAddress(sb);
		if (! isSideOn(b))
			return;
		sweepEdgeState(b, Board.LINE);
		this.pos3 = sa;
	}

	protected void rightReleased(Address pos) {
		currentState = -1;
	}

	private void sweepEdgeState(SideAddress side, int st) {
		if (currentState == -1) {
			if (board.getEdge(side) == Board.LINE) {
				currentState = Board.NOLINE;
			} else {
				currentState = Board.LINE;
			}
		}
		board.changeEdge(side, currentState);
	}

	/*
	 * �u�t�B���I�~�m�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeFixedNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isStable(posS)) {
						board.changeFixedNumber(posS, Board.UNDETERMINED);
					}
				}
			}
		} else if (isCursorOn()) {
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
		} else if (isCursorOn()) {
			if (!board.isStable(pos)) {
				board.changeAnswerNumber(pos, 0);
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
