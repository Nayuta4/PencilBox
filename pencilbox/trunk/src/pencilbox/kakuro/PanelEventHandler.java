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

	/**
	 * 問題入力モードのきりかえ
	 * @param editable
	 */
	public void setProblemEditMode(boolean editable) {
		// 問題入力モードに入ったとき
		if (editable) {
			setMaxInputNumber(45);
		}else{
			setMaxInputNumber(9);
		}
		super.setProblemEditMode(editable);
	}

	/*
	 * 「カックロ」マウス操作
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
	 * 点対称位置の座標を取得する。 カックロ用。
	 * @param pos　元座標
	 * @return posと点対称な位置の座標
	 */
	public Address getSymmetricPosition(Address pos) {
		return new Address(board.rows()-pos.r(), board.cols()-pos.c());
	}
}
