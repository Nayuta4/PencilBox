package pencilbox.common.core;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;


public class BoardCopierBase {
	
	/**
	 * 盤面を複製する。
	 * @param src 複製元の盤面
	 * @return 複製した盤面
	 */
	public BoardBase duplicateBoard(BoardBase src) throws PencilBoxClassException {
		return duplicateBoard(src, 0);
	}
	/**
	 * 盤面を回転して複製する。
	 * @param src 複製元の盤面
	 * @return 複製した盤面
	 */
	public BoardBase duplicateBoard(BoardBase src, int n) throws PencilBoxClassException {
		BoardBase dst = (BoardBase) ClassUtil.createInstance(this.getClass(), ClassUtil.BOARD_CLASS);
		if (Rotator.isTransposed(n)){
			dst.setSize(new Size(src.cols(), src.rows()));
		} else {
			dst.setSize(new Size(src.rows(), src.cols()));
		}
		copyBoardStates(src, dst, n);
		return dst;
	}
	
	/**
	 * 盤面をサイズ変更して複製する。
	 * @param src 複製元の盤面
	 * @param size 変更後のサイズ
	 * @return 複製した盤面
	 * @throws PencilBoxClassException
	 */
	public BoardBase duplicateBoard(BoardBase src, Size size) throws PencilBoxClassException {
		BoardBase dst = (BoardBase) ClassUtil.createInstance(this.getClass(), ClassUtil.BOARD_CLASS);
		dst.setSize(size);
		copyBoardStates(src, dst, 0);
		return dst;
	}
	
	/**
	 * 盤面状態を回転して複製する。
	 * 各サブクラスで実装する。
	 * @param src 複製元の盤面
	 * @param dst 複製先の盤面
	 * @param n 回転番号
	 */
	protected void copyBoardStates(BoardBase src, BoardBase dst, int n) {
	}

	public void copyRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
	}
	
	public void moveRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
	}

	public void eraseRegion(BoardBase board, Area region) {
	}
	
}
