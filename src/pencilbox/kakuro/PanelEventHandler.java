package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandlerBase;


  /**
   * �u�J�b�N���v�}�E�X�^�L�[���쏈���N���X
   */
public class PanelEventHandler extends PanelEventHandlerBase {

	private Board board;

	/**
	 * 
	 */
	public PanelEventHandler() {
	}

	protected void setBoard(BoardBase aBoard) {
		board = (Board) aBoard; 
		setMaxInputNumber(9);
	}

	/*
	 * ���[�h�ɂ����͉\�����قȂ�B
	 */
	protected int getMaxInputNumber() {
		if (isProblemEditMode())
			return 45;
		else
			return 9;
	}

	/*
	 * �u�J�b�N���v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos)) {
				int n = board.getNumber(pos);
				if (n >= board.getMaxNumber())
					board.enterNumberA(pos, 0); 
				else if (n >= 0)
					board.enterNumberA(pos, n + 1);
			}
		}
	}
	
	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos)) {
				int n = board.getNumber(pos);
				if (n == 0) 
					board.enterNumberA(pos, board.getMaxNumber()); 
				else if (n > 0)
					board.enterNumberA(pos, n - 1);
			}
		}
	}
	/*
	 * �u�J�b�N���v�L�[����
	 */
	protected void arrowKeyEntered(int direction) {
		super.arrowKeyEntered(direction);
		if (direction == Direction.LT)
			getKKCursor().setStair(KakuroCursor.UPPER);
		else if (direction == Direction.UP)
			getKKCursor().setStair(KakuroCursor.LOWER);
	}
	
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (getKKCursor().getStair() == KakuroCursor.LOWER)
				board.setSumV(pos, num);
			else if (getKKCursor().getStair() == KakuroCursor.UPPER)
				board.setSumH(pos, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (!board.isWall(posS))
						board.setWall(posS, 0, 0);
			}
		} else if (isCursorOn()){
			if (!board.isWall(pos))
				board.enterNumberA(pos, num);
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			if (pos.r() ==0 || pos.c() == 0)
				return;
			board.removeWall(pos);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (board.isWall(posS))
						board.removeWall(posS);
			}
		} else if (isCursorOn()){
			if (!board.isWall(pos))
				board.enterNumberA(pos, 0);
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			if (getKKCursor().getStair() == KakuroCursor.LOWER)
				board.setSumV(pos, 0);
			else if (getKKCursor().getStair() == KakuroCursor.UPPER)
				board.setSumH(pos, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS) && !posS.equals(pos))
					if (!board.isWall(posS))
						board.setWall(posS, 0, 0);
			}
		}
	}

	/**
	 * @return the KakuroCursor
	 */
	KakuroCursor getKKCursor() {
		return (KakuroCursor) getCellCursor();
	}

	/**
	 * �_�Ώ̈ʒu�̍��W���擾����B �J�b�N���p�B
	 * @param pos�@�����W
	 * @return pos�Ɠ_�Ώ̂Ȉʒu�̍��W
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-pos.r(), board.cols()-pos.c());
	}
}
