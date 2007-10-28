package pencilbox.common.core;

import pencilbox.common.factory.ClassUtil;
import pencilbox.common.factory.PencilBoxClassException;


/**
 * 盤面複製クラスの親クラス
 */
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

	/**
	 * 領域消去。
	 * 具体的な処理はサブクラスで記述する。
	 * @param board 編集する盤面
	 * @param region 消去領域
	 */
	public void eraseRegion(BoardBase board, Area region) {
	}

	/**
	 * 領域複写。
	 * 具体的な処理はサブクラスで記述する。
	 * @param srcBoard 複写元盤面
	 * @param board 複写先盤面
	 * @param region 複写元領域
	 * @param from 複写元領域原点
	 * @param to 原点の複写先
	 * @param rotation 回転
	 */
	public void copyRegion(BoardBase srcBoard, BoardBase board, Area region, Address from, Address to, int rotation) {
	}

	/**
	 * 領域複写。
	 * 初めに盤面全体を複製してから複写処理を行う。
	 * @param board 編集する盤面
	 * @param region 複写元領域
	 * @param from 複写元領域原点
	 * @param to 原点の複写先
	 * @param rotation 回転
	 */
	public void copyRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
		try {
			BoardBase srcBoard = duplicateBoard(board);
			copyRegion(srcBoard, board, region, from, to, rotation);
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 領域移動。
	 * 初めに盤面全体を複製してから複写処理を行い，移動後の領域を消去する。
	 * @param board 編集する盤面
	 * @param region 移動元領域
	 * @param from 移動元領域原点
	 * @param to 原点の移動先
	 * @param rotation 回転
	 */
	public void moveRegion(BoardBase board, Area region, Address from, Address to, int rotation) {
		try {
			BoardBase srcBoard = duplicateBoard(board);
			eraseRegion(board, region);
			copyRegion(srcBoard, board, region, from, to, rotation);
		} catch (PencilBoxClassException e) {
			e.printStackTrace();
		}
	}

}
