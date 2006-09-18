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

	/**
	 * �����̓��[�h�̂��肩��
	 * @param editable
	 */
	public void setProblemEditMode(boolean editable) {
		// �����̓��[�h�ɓ������Ƃ�
		if (editable) {
			setMaxInputNumber(45);
		}else{
			setMaxInputNumber(9);
		}
		super.setProblemEditMode(editable);
	}

	/*
	 * �u�J�b�N���v�}�E�X����
	 */
	protected void leftPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos.r(), pos.c())) {
				board.increaseNumber(pos.r(), pos.c());
			}
		}
	}
	
	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos.r(), pos.c())) {
				board.decreaseNumber(pos.r(), pos.c());
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
				board.setSumV(pos.r(), pos.c(), num);
			else if (getKKCursor().getStair() == KakuroCursor.UPPER)
				board.setSumH(pos.r(), pos.c(), num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (!board.isWall(posS.r(), posS.c()))
						board.setWall(posS.r(), posS.c(), 0, 0);
			}
		} else if (isCursorOn()){
			if (!board.isWall(pos.r(), pos.c()))
				board.enterNumberA(pos.r(), pos.c(), num);
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			if (pos.r() ==0 || pos.c() == 0)
				return;
			board.removeWall(pos.r(), pos.c());
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (board.isWall(posS.r(), posS.c()))
						board.removeWall(posS.r(), posS.c());
			}
		} else if (isCursorOn()){
			if (!board.isWall(pos.r(), pos.c()))
				board.enterNumberA(pos.r(), pos.c(), 0);
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			if (getKKCursor().getStair() == KakuroCursor.LOWER)
				board.setSumV(pos.r(), pos.c(), 0);
			else if (getKKCursor().getStair() == KakuroCursor.UPPER)
				board.setSumH(pos.r(), pos.c(), 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS) && !posS.equals(pos))
					if (!board.isWall(posS.r(), posS.c()))
						board.setWall(posS.r(), posS.c(), 0, 0);
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
