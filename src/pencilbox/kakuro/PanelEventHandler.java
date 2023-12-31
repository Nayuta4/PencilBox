package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.core.Direction;
import pencilbox.common.gui.PanelEventHandlerBase;


  /**
   * 「カックロ」マウス／キー操作処理クラス
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
	 * モードにより入力可能数字異なる。
	 */
	protected int getMaxInputNumber() {
		if (isProblemEditMode())
			return 45;
		else
			return 9;
	}

	/*
	 * 「カックロ」マウス操作
	 */
	protected void leftPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos)) {
				int n = board.getNumber(pos);
				if (n >= board.getMaxNumber())
					board.changeAnswerNumber(pos, 0);
				else if (n >= 0)
					board.changeAnswerNumber(pos, n + 1);
			}
		}
	}

	protected void rightPressed(Address pos) {
		if (!isCursorOn() || getCellCursor().isAt(pos)) {
			if (!board.isWall(pos)) {
				int n = board.getNumber(pos);
				if (n == 0) 
					board.changeAnswerNumber(pos, board.getMaxNumber());
				else if (n > 0)
					board.changeAnswerNumber(pos, n - 1);
			}
		}
	}
	/*
	 * 「カックロ」キー操作
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
				board.changeSum(pos, Direction.VERT, num);
			else if (getKKCursor().getStair() == KakuroCursor.UPPER)
				board.changeSum(pos, Direction.HORIZ, num);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (!board.isWall(posS))
						board.changeWall(posS, 0);
			}
		} else if (isCursorOn()){
			if (!board.isWall(pos))
				board.changeAnswerNumber(pos, num);
		}
	}

	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			if (pos.r() ==0 || pos.c() == 0)
				return;
			board.changeWall(pos, Board.BLANK);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (board.isWall(posS))
						board.changeWall(posS, Board.BLANK);
			}
		} else if (isCursorOn()){
			if (!board.isWall(pos))
				board.changeAnswerNumber(pos, 0);
		}
	}

	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			if (getKKCursor().getStair() == KakuroCursor.LOWER)
				board.changeSum(pos, Direction.VERT, 0);
			else if (getKKCursor().getStair() == KakuroCursor.UPPER)
				board.changeSum(pos, Direction.HORIZ, 0);
			if (isSymmetricPlacementMode()) {
				Address posS = getSymmetricPosition(pos);
				if (isOn(posS))
					if (!board.isWall(posS))
						board.changeWall(posS, 0);
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
	 * 点対称位置の座標を取得する。 カックロ用。
	 * @param pos 元座標
	 * @return posと点対称な位置の座標
	 */
	public Address getSymmetricPosition(Address pos) {
		return Address.address(board.rows()-pos.r(), board.cols()-pos.c());
	}
}
