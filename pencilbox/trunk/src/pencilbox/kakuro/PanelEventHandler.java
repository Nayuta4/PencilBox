package pencilbox.kakuro;

import pencilbox.common.core.Address;
import pencilbox.common.core.BoardBase;
import pencilbox.common.gui.CellCursor;
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

	public CellCursor createCursor() {
		return new KakuroCursor(this);
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
	protected void numberEntered(Address pos, int num) {
		if (isProblemEditMode()) {
			if (getKCursor().getStair() == KakuroCursor.LOWER)
				board.setSumV(pos.r(), pos.c(), num);
			else if (getKCursor().getStair() == KakuroCursor.UPPER)
				board.setSumH(pos.r(), pos.c(), num);
		} else if (isCursorOn()){
			if (!board.isWall(pos.r(), pos.c()))
				board.enterNumberA(pos.r(), pos.c(), num);
		}
	}
	
	protected void spaceEntered(Address pos) {
		if (isProblemEditMode()) {
			board.removeWall(pos.r(), pos.c());
		} else if (isCursorOn()){
			if (!board.isWall(pos.r(), pos.c()))
				board.enterNumberA(pos.r(), pos.c(), 0);
		}
	}
	
	protected void minusEntered(Address pos) {
		if (isProblemEditMode()) {
			if (getKCursor().getStair() == KakuroCursor.LOWER)
				board.setSumV(pos.r(), pos.c(), 0);
			else if (getKCursor().getStair() == KakuroCursor.UPPER)
				board.setSumH(pos.r(), pos.c(), 0);
		}
	}

	/**
	 * @return the kcursor
	 */
	KakuroCursor getKCursor() {
		return (KakuroCursor) getCellCursor();
	}
	

}
