package pencilbox.shikaku;

import java.awt.event.MouseEvent;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.SideAddress;
import pencilbox.common.gui.PanelEventHandlerBase;


/**
 * �u�l�p�ɐ؂�v�}�E�X�^�L�[���쏈���N���X
 */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	private int pivotR = -1;  // �h���b�O���ɌŒ肷�钸�_�̍s���W
	private int pivotC = -1;  // �h���b�O���ɌŒ肷�钸�_�̗���W
//	private Square draggingSquare; // �h���b�O���č��܂��ɕ`�����Ƃ��Ă���l�p
	private int dragState = 0;
	private int currentState = -1;
	private Address pos3 = Address.NOWHERE;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard;
	}

	/*
	 * �u�l�p�ɐ؂�v�}�E�X����
	 * 
	 * ���h���b�O�{�^���𗣂��Ďl�p���m�肷��
	 * �����̎l�p���Ȃ���΁C�V�����l�p�����
	 * �����̎l�p�������Ă͂��߂̃}�X���瓮�����Ƀ{�^���𗣂����Ȃ�C���̎l�p������
	 * �����̎l�p�������Ă͂��߂̃}�X�ƕʂ̃}�X�Ń{�^���𗣂����Ȃ�C�l�p��ύX����
	 * �����̎l�p�������Ă͂��߂̃}�X�ɖ߂��ă{�^���𗣂����Ȃ�C�������Ȃ�
	 */
	protected void leftPressed(Address pos) {
		Square draggingSquare;
		Square sq = board.getSquare(pos);
		if (sq == null) { // �n�_�Ɏl�p���Ȃ��ꍇ�C�V�����l�p�����
			draggingSquare = new Square(pos, pos);
		} else { // �n�_�Ɋ����̎l�p������ꍇ�C���̎l�p��ύX����
			draggingSquare = new Square(sq);
			dragState = 1; // �h���b�O�J�n
		}
		fixPivot(draggingSquare, pos);
		setDraggingSquare(draggingSquare);
	}

	protected void leftDragged(Address pos) {
		Square draggingSquare = getDraggingSquare();
		if (draggingSquare == null) {
			return;
		}
		if (pivotR >= 0 && pivotC >= 0) {
			draggingSquare.set(pivotR, pivotC, pos.r(), pos.c());
		} else if (pivotR >= 0 && pivotC == -1) {
			draggingSquare.set(pivotR, draggingSquare.c0(), pos.r(), draggingSquare.c1());
		} else if (pivotR == -1 && pivotC >= 0) {
			draggingSquare.set(draggingSquare.r0(), pivotC, draggingSquare.r1(), pos.c());
		} else if (pivotR == -1 && pivotC == -1) {
//			draggingSquare.set(draggingSquare.r0, draggingSquare.c0, draggingSquare.r1, drggingSquare.c1());
		}
		dragState = 2; //�h���b�O��
		fixPivot(draggingSquare, pos); // �������ݍ��W���l�p�̒[�ł���΁C�Œ蒸�_���X�V
	}

	/**
	 * �h���b�O����Ƃ��̌Œ肳���_��ݒ肷��B
	 * �������h���b�O�����}�X�������̎l�p�̊O���Ȃ�΁C�����瑤�������C���Α����Œ�ƂȂ�B
	 * ���łɌŒ肳���_�����܂��Ă���΁C�������Ȃ��B
	 * @param s
	 * @param p
	 */
	private void fixPivot(Square s, Address p) {
		if (pivotR == -1) {
			if (p.r() == s.r0()) {
				pivotR = s.r1();
			} else if (p.r() == s.r1()) {
				pivotR = s.r0();
			}
		}
		if (pivotC == -1) {
			if (p.c() == s.c0()) {
				pivotC = s.c1();
			} else if (p.c() == s.c1()) {
				pivotC = s.c0();
			}
		}
	}

	protected void leftReleased(Address pos) {
		Square draggingSquare = getDraggingSquare();
		if (draggingSquare == null)
			return;
		int rp = pivotR >= 0 ? pivotR : draggingSquare.r0();
		int cp = pivotC >= 0 ? pivotC : draggingSquare.c0();
		Square sq = board.getSquare(rp, cp);
		if (sq == null) {
			board.removeOverlappedSquares(draggingSquare, null);
			board.addSquare(new Square(draggingSquare));
		} else {
			if (dragState == 1 && isOn(pos)) {
				board.removeSquare(sq);
			} else if (sq.equals(draggingSquare)) {
				;
			} else {
				board.removeOverlappedSquares(draggingSquare, sq);
				board.changeSquare(sq, draggingSquare.p0(), draggingSquare.p1());
			}
		}
		setDraggingSquare(null);
		resetPivot();
		dragState = 0;
	}

//	protected void rightPressed(Address pos) {
//		Square s = board.getSquare(pos);
//		if(s != null) {
//			board.removeSquare(s);
//		}
//	}

//	protected void rightDragged(Address pos) {
//		Square s = board.getSquare(pos);
//		if(s != null) {
//			board.removeSquare(s);
//		}
//	}

	private void resetPivot() {
		pivotR = -1;
		pivotC = -1;
	}

	protected void rightPressed3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		this.pos3 = sa;
	}

	protected void rightDragged3(MouseEvent e) {
		Address sa = pointToSuperAddress(e, 0.5);
		if (pos3.equals(sa))
			return;
		else if (pos3.r() != sa.r() && pos3.c() != sa.c())
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
	 * �u�l�p�ɐ؂�v�L�[����
	 */
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (num > 0) {
				board.changeNumber(pos, num);
				if (isSymmetricPlacementMode()) {
					Address posS = getSymmetricPosition(pos);
					if (!board.isNumber(posS))
						board.changeNumber(posS, Board.UNDECIDED_NUMBER);
				}
			}
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (board.isNumber(posS))
					board.changeNumber(posS, 0);
			}
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			board.changeNumber(pos, Board.UNDECIDED_NUMBER);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (!board.isNumber(posS))
					board.changeNumber(posS, Board.UNDECIDED_NUMBER);
			}
		}
	}

	/**
	 * @param draggingSquare the draggingSquare to set
	 */
	void setDraggingSquare(Square draggingSquare) {
		((Panel)getPanel()).setDraggingSquare(draggingSquare);
	}

	/**
	 * @return the draggingSquare
	 */
	Square getDraggingSquare() {
		return ((Panel)getPanel()).getDraggingSquare();
	}
}
